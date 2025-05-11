package me.jasper.jasperproject.JMinecraft.Player.Ability;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Map;

public class Mage extends PlayerAbility {
    public final static NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), "Mage");
    @Getter
    public static class Shoot extends Mage implements Listener {
        public final static NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), "Mage-Shoot");
        @Setter private org.bukkit.entity.Projectile projectile;
        private Map<Stats, Float> player_stats;
        @Setter private boolean critical;

        public Shoot(org.bukkit.entity.Player player, Projectile projectile, Map<Stats, Float> stats){
            org.bukkit.entity.Projectile bukkitProjectile = (org.bukkit.entity.Projectile) projectile.getBukkitEntity();
            bukkitProjectile.setShooter(player);
            this.projectile = bukkitProjectile;
            this.player_stats = stats;
        }

        public Shoot() {

        }

        @EventHandler
        public void onShoot(Shoot e){
            org.bukkit.entity.Projectile projectile = e.getProjectile();
            if(!(projectile.getShooter() instanceof org.bukkit.entity.Player shooter)) return;
            Vector velocity = shooter.getEyeLocation().getDirection().multiply(1.5f+(e.getPlayer_stats().getOrDefault(Stats.ATTACK_SPEED, 0.1f)/100));
            DamageResult result = DamageResult.builder(e.getPlayer_stats())
                    .type(DamageType.MAGIC)
                    .critical(e.isCritical())
                    .build();
            PersistentDataContainer pdc = projectile.getPersistentDataContainer();
            pdc.set(JKey.DAMAGE, PersistentDataType.INTEGER, (int) result.getFinal_damage());
            if(e.getProjectile() instanceof Arrow arrow) {
                arrow.setCritical(e.isCritical());
            }else {
                pdc.set(JKey.DAMAGE_ISCRITICAL, PersistentDataType.BOOLEAN, e.isCritical());
            }
            projectile.spawnAt(shooter.getEyeLocation());
            projectile.setVelocity(velocity);
        }

        @EventHandler
        public void onTriger(PlayerInteractEvent e){
            org.bukkit.entity.Player bukkitPlayer = e.getPlayer();
            if (TRIGGER.Interact.LEFT_CLICK(e) || TRIGGER.Interact.RIGHT_CLICK(e)){
                PersistentDataContainer pdc = bukkitPlayer.getPersistentDataContainer();
                Level nmsWorld = ((CraftWorld) bukkitPlayer.getWorld()).getHandle();

                }
            }
        }
}
