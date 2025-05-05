package me.jasper.jasperproject.JMinecraft.Player.Ability;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Cooldown;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Assassin extends PlayerAbility {
    public static final NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), "Assassin");
    public static class Stab extends Assassin implements Cooldown {
        private Player player;
        @Getter private EquipmentSlot weapon;
        public static final NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), "Assassin-Stab");

        public Stab(Player player, EquipmentSlot weapon){
            this.player = player;
            this.weapon=weapon;
        }

        @EventHandler
        public void stab(Stab e){
            if(e.isCancelled())return;
            if(hasCooldown(e)) return;
            Entity entity = e.getPlayer().getTargetEntity(e.getLevel()+25,false);
            if(entity==null){
                e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red><b>INVALID!</b> There's no entity on sight"));
                return;
            }
            Location entityLoc = entity.getLocation().clone();
            entityLoc.setPitch(0);
            Location lokasiTujuan = entityLoc.clone().add(entityLoc.getDirection().normalize().multiply(-1));
            if(lokasiTujuan.toBlockLocation().getBlock().isSolid() || lokasiTujuan.clone().toBlockLocation().add(0,1,0).getBlock().isSolid()){
                e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red><b>INVALID!</b> Location is obstructed"));
                return;
            }
            Player player= e.getPlayer();
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE,0.75f,1.5f);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL,0.75f,1.5f);
            player.getWorld().spawnParticle(
                    Particle.DUST, player.getLocation().add(0,1,0), 60
                    ,.3f,.4f,.3f,0
                    ,new Particle.DustOptions(Color.fromRGB(60,60,60),2f), false);

            player.setFallDistance(0);
            Util.teleportEntity(player,lokasiTujuan,false);
            player.attack(entity); //Damage logic.........
            player.swingHand(e.getWeapon());

            if(entity instanceof Player targetPlayer) targetPlayer.playSound(entity.getLocation(), Sound.ENTITY_GHAST_HURT,1f,0.85f);
            player.getWorld().spawnParticle(
                    Particle.DUST, player.getLocation().add(0,1,0), 60
                    ,.3f,.4f,.3f,0
                    ,new Particle.DustOptions(Color.fromRGB(60,60,60),2f), false);
            applyCooldown(e);
        }

        @EventHandler
        public void triger(PlayerInteractEvent e){
            ItemStack item = e.getItem();
            Player player = e.getPlayer();
            if(item==null || item.getType().equals(Material.AIR)) return;
            boolean isSword = isSword(item);
            if(player.isSneaking()){
                Stab stab = new Stab(player, EquipmentSlot.OFF_HAND);
                Bukkit.getPluginManager().callEvent(stab);
                return;
            }
            if (!isSword) {
                item = player.getInventory().getItemInOffHand();
                isSword = isSword(item);
                if(!isSword) return;

                if(player.isSneaking()){
                    Stab stab = new Stab(player, EquipmentSlot.OFF_HAND);
                    Bukkit.getPluginManager().callEvent(stab);
                    return;
                }
                Map<Stats, Float> combatStats = Stats.getCombatStats(player, item);
                DamageResult result = DamageResult.builder(combatStats).build();
                Entity targetEntity = player.getTargetEntity((int) (float) combatStats.get(Stats.SWING_RANGE));
                if(!(targetEntity instanceof LivingEntity livingEntity))return;
                float attackCooldown = player.getAttackCooldown();
                livingEntity.damage(result.getFinal_damage()*attackCooldown, DamageSource.builder(DamageType.FALLING_BLOCK).build());
            }
        }

        private boolean isSword(ItemStack item){
           return item.getType().equals(Material.IRON_SWORD) &&
                   item.getType().equals(Material.WOODEN_SWORD) &&
                   item.getType().equals(Material.GOLDEN_SWORD) &&
                   item.getType().equals(Material.DIAMOND_SWORD) &&
                   item.getType().equals(Material.NETHERITE_SWORD) &&
                   item.getType().equals(Material.STONE_SWORD);
        }

        @Override
        public float getCooldown() {
            return 60;
        }

        @Override
        public boolean isVisible() {
            return true;
        }

        @Override
        public Player getPlayer() {
            return player;
        }
    }
}
