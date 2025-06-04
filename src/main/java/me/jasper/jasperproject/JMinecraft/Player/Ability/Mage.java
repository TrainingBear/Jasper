package me.jasper.jasperproject.JMinecraft.Player.Ability;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import net.minecraft.world.entity.projectile.Projectile;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Mage extends PlayerAbility {
    public final static NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), "Mage");

    @Getter
    public static class Shoot extends Mage implements Listener {
        public final static NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), "Mage-Shoot");
        @Setter
        private org.bukkit.entity.Projectile projectile;
        private Map<Stats, Float> player_stats;
        @Setter
        private boolean critical;

        public Shoot(org.bukkit.entity.Player player, Projectile projectile, Map<Stats, Float> stats) {
            org.bukkit.entity.Projectile bukkitProjectile = (org.bukkit.entity.Projectile) projectile.getBukkitEntity();
            bukkitProjectile.setShooter(player);
            this.projectile = bukkitProjectile;
            this.player_stats = stats;
        }

        public Shoot() {

        }

        @EventHandler
        public void onShoot(Shoot e) {
            org.bukkit.entity.Projectile projectile = e.getProjectile();
            if (!(projectile.getShooter() instanceof org.bukkit.entity.Player shooter))
                return;
            Vector velocity = shooter.getEyeLocation().getDirection()
                    .multiply(1.5f + (e.getPlayer_stats().getOrDefault(Stats.ATTACK_SPEED, 0.1f) / 100));
            DamageResult result = DamageResult.builder(e.getPlayer_stats())
                    .type(DamageType.MAGIC)
                    .critical(e.isCritical())
                    .build();
            PersistentDataContainer pdc = projectile.getPersistentDataContainer();
            pdc.set(JKey.DAMAGE, PersistentDataType.INTEGER, (int) result.getFinal_damage());
            if (e.getProjectile() instanceof Arrow arrow) {
                arrow.setCritical(e.isCritical());
            } else {
                pdc.set(JKey.DAMAGE_ISCRITICAL, PersistentDataType.BOOLEAN, e.isCritical());
            }
            projectile.spawnAt(shooter.getEyeLocation());
            projectile.setVelocity(velocity);
        }

        // @EventHandler
        // public void onTriger(PlayerInteractEvent e) {
        // org.bukkit.entity.Player bukkitPlayer = e.getPlayer();
        // if (TRIGGER.Interact.LEFT_CLICK(e) || TRIGGER.Interact.RIGHT_CLICK(e)) {
        // PersistentDataContainer pdc = bukkitPlayer.getPersistentDataContainer();
        // Level nmsWorld = ((CraftWorld) bukkitPlayer.getWorld()).getHandle();

        // }
        // }
    }

    @Getter
    public static class Attack extends Mage implements Listener {
        private LivingEntity attacker;
        private Location target;
        private Map<Stats, Float> player_stats;
        /** the list when along the beam can damage {@link LivingEntity} */
        private ItemStack used_Weapon;
        private float modifier;

        /**
         * @param stats    the {@link JPlayer} stats that attack
         * @param attacker the attacker of course
         * @param target   the target of course
         */
        public Attack(Map<Stats, Float> stats, LivingEntity attacker, ItemStack used_Weapon, Location target,
                float modifier) {
            this.target = target;
            this.attacker = attacker;
            this.player_stats = stats;
            this.used_Weapon = used_Weapon;
            this.modifier = modifier;
        }

        public Attack() {
        }

        @EventHandler
        public void onAttack(Attack e) {
            final Location start = e.getAttacker().getLocation().add(0, e.getAttacker().getHeight() / 2.0, 0);
            final Location end = e.getTarget().clone();
            final World wrld = start.getWorld();
            final float modif = e.getModifier();

            final Vector dir3D = end.clone().subtract(start).toVector();
            final double totalDistance = dir3D.length();
            if (totalDistance <= 0.0001)
                return;

            final Vector direction = dir3D.clone().normalize();
            Vector perp;
            if (Math.abs(direction.getX()) < 1e-4 && Math.abs(direction.getZ()) < 1e-4) // 1e-4 mean = 0.0001
                perp = direction.clone().crossProduct(new Vector(1, 0, 0));
            else
                perp = direction.clone().crossProduct(new Vector(0, 1, 0));
            perp.normalize();

            final double amplitude = 0.48; // tinggi puncak gelombang
            final double frequency = Math.PI / 3; // frekuensi gelombang

            Set<LivingEntity> attackedEntites = new HashSet<>();
            final double step = 0.9;
            for (double t = 0; t <= totalDistance; t += step) {
                Location loc = start.clone().add(direction.clone().multiply(t))
                        .add(perp.clone().multiply(amplitude * Math.sin(frequency * t)));

                attackedEntites.addAll(loc.getNearbyLivingEntities(0.5f));

                wrld.spawnParticle(
                        Particle.END_ROD, loc,
                        1, 0, 0, 0, 0//
                );
            }

            final LivingEntity ygNyerang = e.getAttacker();
            JPlayer jpler = null;
            if (ygNyerang instanceof Player pleryer) {
                jpler = JPlayer.getJPlayer(pleryer);
            }

            for (LivingEntity entitas : attackedEntites) {
                if (entitas == ygNyerang)
                    continue;
                if (jpler != null) {
                    jpler.attack(
                            entitas,
                            e.getUsed_Weapon() != null ? e.getUsed_Weapon() : new ItemStack(Material.AIR),
                            false,
                            modif);
                } else {
                    ygNyerang.attack(entitas);
                }
            }
        }
    }
}
