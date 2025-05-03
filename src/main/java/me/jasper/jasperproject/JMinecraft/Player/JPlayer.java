package me.jasper.jasperproject.JMinecraft.Player;

import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorEquipEvent;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorType;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.Component;
import net.minecraft.world.damagesource.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Getter
public class JPlayer implements Listener {
    Map<ArmorType, ItemStack> lastItems = new HashMap<>(ArmorType.values().length);
    private UUID UUID;

    public JPlayer(){ }
    public JPlayer(Player player){
        this.UUID = player.getUniqueId();
    }

    /**
     * @return total damage amount
     */

    public DamageResult attack(@Nullable LivingEntity target, ArmorType type, DamageType damageType, boolean critical){
        Player bukkitPlayer = getBukkitPlayer();
        Map<Stats, Float> player_stats;
        ItemStack offHand = bukkitPlayer.getInventory().getItemInOffHand();

        if(type.equals(ArmorType.MAIN_HAND)){
            ItemStack mainHand = bukkitPlayer.getInventory().getItemInMainHand();
            PersistentDataContainerView mainPdc = mainHand.getPersistentDataContainer();
            player_stats = Stats.fromPlayer(bukkitPlayer,
                    mainPdc.has(JKey.Category) && ItemType.isMelee(mainPdc.get(JKey.Category, PersistentDataType.STRING)) ? mainHand : null
            );
            if(damageType.equals(DamageType.MELEE)){
                float damage = player_stats.get(Stats.DAMAGE);
                float strength = player_stats.get(Stats.STRENGTH);
                float crit_damage = player_stats.get(Stats.CRIT_DAMAGE);
                boolean isCritical = bukkitPlayer.isSneaking()&&
                        critical&&
                        (
                                !bukkitPlayer.hasPotionEffect(PotionEffectType.BLINDNESS) ||
                                        !bukkitPlayer.hasPotionEffect(PotionEffectType.NAUSEA) ||
                                        !bukkitPlayer.hasPotionEffect(PotionEffectType.POISON) ||
                                        !bukkitPlayer.hasPotionEffect(PotionEffectType.WITHER) ||
                                        !bukkitPlayer.isFrozen()
                        );
                int final_damage = (int) (damage * (
                        ((strength/100)+1)
                                * (isCritical? (crit_damage/100) + 1 : 1)
                ));
                final_damage = (int) (final_damage * (player_stats.get(Stats.MELEE_MODIFIER) + 1));
                return DamageResult.builder()
                        .damage(final_damage)
                        .critical(isCritical)
                        .type(DamageType.MELEE)
                        .build();
            }
            if(damageType.equals(DamageType.MAGIC)){
                float mana = mainPdc.get(Stats.MANA.getKey(), PersistentDataType.FLOAT);
            }
        }
        return DamageResult.builder().build();
    }

    public Player getBukkitPlayer(){
        return Bukkit.getPlayer(this.UUID);
    }

    public void setLastItems(ArmorType type, ItemStack item){
        lastItems.put(type, item);
    }

    public @Nullable ItemStack getLastItems(ArmorType type){ return lastItems.get(type); }
    public float getStat(Stats stats){ return Stats.fromPlayer(getBukkitPlayer()).get(stats); }

    @EventHandler
    public void onEquip(ArmorEquipEvent e){
        if(e.isCancelled()) return;
        Player player = e.getPlayer();
        Stats.apply(player, e.getNewArmorPiece(), e.getType());
    }

    @EventHandler
    public void onSwapHotbar(InventoryDragEvent e){
        JasperProject.getPlugin().getServer().broadcastMessage("Player is swapping");
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e){
        if(!(e.getEntity() instanceof Player bukkitPlayer)) return;

    Map<Stats, Float> player_stats = Stats.fromPlayer(bukkitPlayer, e.getBow());

    float damage = player_stats.get(Stats.DAMAGE);
    float strength = player_stats.get(Stats.STRENGTH);
    float crit_damage = player_stats.get(Stats.CRIT_DAMAGE);
    float chance = player_stats.get(Stats.CRIT_CHANCE);

    boolean crit = (chance >= new Random().nextInt(100)+1) && (
            !bukkitPlayer.hasPotionEffect(PotionEffectType.BLINDNESS) ||
                    !bukkitPlayer.hasPotionEffect(PotionEffectType.NAUSEA) ||
                    !bukkitPlayer.hasPotionEffect(PotionEffectType.POISON) ||
                    !bukkitPlayer.hasPotionEffect(PotionEffectType.WITHER) ||
                    !bukkitPlayer.isFrozen());

    int final_damage = (int) (damage * (
            ((strength/100)+1)
                    * (crit? (crit_damage/100) + 1 : 1)
    ));
    bukkitPlayer.sendMessage(Component.text("You are shooting with power of " + e.getForce()));
    final_damage = (int) (final_damage * (player_stats.get(Stats.ARROW_MODIFIER) + 1) * e.getForce());
    if(e.getProjectile() instanceof Arrow arrow){
        arrow.setDamage(final_damage);
    }
    }
}
