package me.jasper.jasperproject.JMinecraft.Player;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorEquipEvent;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorType;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
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
        return attack(target, type, damageType, critical, 1f);
    }
    public DamageResult attack(@Nullable LivingEntity target, ArmorType type, DamageType damageType, boolean critical, float modifier){
        Player bukkitPlayer = getBukkitPlayer();
        Map<Stats, Float> player_stats;
        ItemStack offHand = bukkitPlayer.getInventory().getItemInOffHand();

        if(type.equals(ArmorType.MAIN_HAND)){
            ItemStack mainHand = bukkitPlayer.getInventory().getItemInMainHand();
            boolean valid = mainHand.hasItemMeta() && mainHand.getItemMeta().getPersistentDataContainer().has(JKey.Category) && ItemType.isMelee(mainHand.getItemMeta().getPersistentDataContainer().get(JKey.Category, PersistentDataType.STRING));
            player_stats = Stats.fromPlayer(bukkitPlayer, valid ? mainHand : null);
            DamageResult result;
            if(damageType.equals(DamageType.MELEE)){
                boolean isCritical = bukkitPlayer.isSneaking()&& critical&& ( !bukkitPlayer.hasPotionEffect(PotionEffectType.BLINDNESS) || !bukkitPlayer.hasPotionEffect(PotionEffectType.NAUSEA) || !bukkitPlayer.hasPotionEffect(PotionEffectType.POISON) || !bukkitPlayer.hasPotionEffect(PotionEffectType.WITHER) || !bukkitPlayer.isFrozen());
                result = DamageResult.builder(player_stats)
                        .critical(isCritical)
                        .type(DamageType.MELEE)
                        .build();
                if(target!=null)target.damage(result.getFinal_damage()*modifier, DamageSource.builder(org.bukkit.damage.DamageType.FALLING_BLOCK) .withCausingEntity(bukkitPlayer) .withDirectEntity(bukkitPlayer) .build() );
                return result;
            }
            if(damageType.equals(DamageType.MAGIC)){
            }
        }
        return DamageResult.patch((float) 0, null, DamageType.ABSTRACT);
    }

    public Player getBukkitPlayer(){
        return Bukkit.getPlayer(this.UUID);
    }

    public void setLastItems(ArmorType type, ItemStack item){
        lastItems.put(type, item);
    }

    public @Nullable ItemStack getLastItems(ArmorType type){ return lastItems.get(type); }

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
        boolean critical = !bukkitPlayer.hasPotionEffect(PotionEffectType.BLINDNESS) ||
                !bukkitPlayer.hasPotionEffect(PotionEffectType.NAUSEA) ||
                !bukkitPlayer.hasPotionEffect(PotionEffectType.POISON) ||
                !bukkitPlayer.hasPotionEffect(PotionEffectType.WITHER) ||
                !bukkitPlayer.isFrozen();
        DamageResult result = DamageResult.builder(player_stats)
                .force(e.getForce())
                .type(DamageType.PROJECTILE)
                .critical(critical)
                .build();
        if(e.getProjectile() instanceof Arrow arrow){
            arrow.setCritical(true);
            arrow.setDamage(result.getFinal_damage());
        }
    }
}
