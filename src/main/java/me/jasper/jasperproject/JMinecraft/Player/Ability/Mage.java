package me.jasper.jasperproject.JMinecraft.Player.Ability;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.Util.JKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Map;

public class Mage extends PlayerAbility {
    @Getter
    public static class Shoot extends Mage {
        private org.bukkit.entity.Projectile projectile;
        private ItemStack weapon;

        public Shoot(org.bukkit.entity.Player player, Projectile projectile, ItemStack weapon){
            this.weapon = weapon;
            org.bukkit.entity.Projectile bukkitProjectile = (org.bukkit.entity.Projectile) projectile.getBukkitEntity();
            bukkitProjectile.setShooter(player);
            this.projectile = bukkitProjectile;
        }


        @EventHandler
        public void onShoot(Shoot e){
            ItemStack weapon = e.getWeapon();
            org.bukkit.entity.Projectile projectile = e.getProjectile();
            if(!(projectile.getShooter() instanceof org.bukkit.entity.Player shooter)) return;
            Map<Stats, Float> combatStats = Stats.getCombatStats(shooter, weapon);
            Vector velocity = projectile.getVelocity();
            velocity.subtract(shooter.getTargetBlockExact(1).getLocation().toVector()).multiply(1+(combatStats.getOrDefault(Stats.ATTACK_SPEED, 0f)/100));
            boolean critical = Stats.roll(combatStats.get(Stats.CRIT_CHANCE));
            DamageResult result = DamageResult.builder(combatStats)
                    .type(DamageType.MAGIC)
                    .critical(critical)
                    .build();
            PersistentDataContainer pdc = projectile.getPersistentDataContainer();
            pdc.set(JKey.DAMAGE, PersistentDataType.INTEGER, (int) result.getFinal_damage());
            pdc.set(JKey.DAMAGE_ISCRITICAL, PersistentDataType.BOOLEAN, critical);
            projectile.setShooter(shooter);
            projectile.spawnAt(shooter.getEyeLocation());
            projectile.setVelocity(velocity);
        }

        @EventHandler
        public void onTriger(PlayerInteractEvent e){
            if (TRIGGER.Interact.LEFT_CLICK(e)){
                org.bukkit.entity.Player bukkitPlayer = e.getPlayer();
                PersistentDataContainer pdc = bukkitPlayer.getPersistentDataContainer();
                Level nmsWorld = ((CraftWorld) bukkitPlayer.getWorld()).getHandle();
                if (pdc.has(JKey.Ability) && pdc.get(JKey.Ability, PersistentDataType.TAG_CONTAINER).has(this.key)){
                    Shoot shoot = new Shoot(bukkitPlayer,
                            new WitherSkull(EntityType.WITHER_SKULL, nmsWorld),
                            bukkitPlayer.getInventory().getItemInMainHand()
                    );
                    Bukkit.getPluginManager().callEvent(shoot);
                }
            }
        }
    }
}
