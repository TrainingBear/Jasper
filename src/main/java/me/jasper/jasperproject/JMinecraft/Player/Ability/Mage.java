package me.jasper.jasperproject.JMinecraft.Player.Ability;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.Util.JKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Map;

public class Mage extends PlayerAbility {
    @Getter
    public static class Shoot extends Mage{
        private Projectile projectile;
        private DamageResult result;

        public Shoot(Player player, Projectile projectile){
            projectile.setShooter(player);
            Map<Stats, Float> statsFloatMap = Stats.fromPlayer(player, player.getInventory().getItemInMainHand());

            this.projectile = projectile;
        }

        @EventHandler
        public void onShoot(Shoot e){
            ProjectileSource shooter = e.getProjectile().getShooter();
        }

        @EventHandler
        public void onTriger(PlayerInteractEvent e){
            if (TRIGGER.Interact.RIGHT_CLICK(e) || TRIGGER.Interact.LEFT_CLICK(e)){
                PersistentDataContainer pdc = e.getPlayer().getPersistentDataContainer();
                if (pdc.has(JKey.Ability)){

                }
            }
        }
    }
}
