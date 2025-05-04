package me.jasper.jasperproject.JMinecraft.Player.Ability;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.Util.JKey;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.projectile.EntityWitherSkull;
import net.minecraft.world.entity.projectile.IProjectile;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftWitherSkull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.Map;

public class Mage extends PlayerAbility {
    @Getter
    public static class Shoot extends Mage {
        private Projectile projectile;
        private ItemStack weapon;

        public Shoot(Player player, IProjectile projectile, ItemStack weapon){
        }
        public Shoot(Player player, Projectile projectile, ItemStack weapon){
            this.weapon = weapon;
            projectile.setShooter(player);
            this.projectile = projectile;
        }

        @EventHandler
        public void onShoot(Shoot e){
            ItemStack weapon = e.getWeapon();
            Projectile projectile = e.getProjectile();
            if(!(projectile.getShooter() instanceof Player shooter)) return;
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
            if (TRIGGER.Interact.RIGHT_CLICK(e) || TRIGGER.Interact.LEFT_CLICK(e)){
                Player player = e.getPlayer();
                PersistentDataContainer pdc = player.getPersistentDataContainer();
                if (pdc.has(JKey.Ability) && pdc.get(JKey.Ability, PersistentDataType.TAG_CONTAINER).has(this.key)){
//                    Shoot shoot = new Shoot(player, new EntityWitherSkull(EntityTypes.br, ((CraftWorld) player.getWorld()).getHandle()));
                }
            }
        }
    }
}
