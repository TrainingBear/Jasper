package me.jasper.jasperproject.JMinecraft.Player;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorEquipEvent;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
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
    public int attack(AttackType type, float defence){
        Player bukkitPlayer = getBukkitPlayer();
        Map<Stats, Float> player_stats = Stats.fromPlayer(bukkitPlayer);
        return switch (type){
            case MELEE -> {
                float damage = player_stats.get(Stats.DAMAGE) + Stats.DAMAGE.getBaseValue();
                float strength = player_stats.get(Stats.STRENGTH) + Stats.STRENGTH.getBaseValue();
                float crit_damage = player_stats.get(Stats.CRIT_DAMAGE) + Stats.CRIT_DAMAGE.getBaseValue();
                boolean isCritical = bukkitPlayer.isSneaking()&&
                        !bukkitPlayer.isClimbing()&&
                        bukkitPlayer.isJumping() &&
                        (
                        !bukkitPlayer.hasPotionEffect(PotionEffectType.BLINDNESS) ||
                        !bukkitPlayer.hasPotionEffect(PotionEffectType.NAUSEA) ||
                        !bukkitPlayer.hasPotionEffect(PotionEffectType.POISON) ||
                        !bukkitPlayer.hasPotionEffect(PotionEffectType.WITHER) ||
                        !bukkitPlayer.isFrozen());
                int final_damage = (int) (damage * (
                        ((strength/100)+1)
                                * (isCritical? (crit_damage/100) + 1 : 1)
                ));
                final_damage = (int) (final_damage * (player_stats.get(Stats.MELEE_MODIFIER) + 1));
                yield (int) (final_damage/defence);
            }
            case MAGIC -> (int) (1000/defence);
            case PROJECTILE -> {
                float damage = player_stats.get(Stats.DAMAGE) + Stats.DAMAGE.getBaseValue();
                float strength = player_stats.get(Stats.STRENGTH) + Stats.STRENGTH.getBaseValue();
                float crit_damage = player_stats.get(Stats.CRIT_DAMAGE) + Stats.CRIT_DAMAGE.getBaseValue();
                float chance = player_stats.get(Stats.CRIT_CHANCE) + Stats.CRIT_CHANCE.getBaseValue();

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
                final_damage = (int) (final_damage * (player_stats.get(Stats.ARROW_MODIFIER) + 1));
                yield (int) (final_damage/defence);
            }
        };
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

    public enum AttackType{
        /// melee
        MELEE,
        /// magic
        MAGIC,
        /// Arrow, Bullet, Wind, etc
        PROJECTILE
    }
}
