package me.jasper.jasperproject.JasperEntity.MobEventListener;

import me.jasper.jasperproject.JasperEntity.JasperEntity;
import me.jasper.jasperproject.JasperEntity.MobEvent.JasperMobDamagedEvent;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class JSMDamagedEvent implements Listener {
    JasperProject plugin;
    public JSMDamagedEvent(JasperProject plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamaged(JasperMobDamagedEvent e){

    }


    @EventHandler
    public void onDamagedByPlayer(EntityDamageByEntityEvent e){
        if (
                (e.getEntity() instanceof LivingEntity mob) &&
                (e.getDamager() instanceof Player player) &&
                mob.getScoreboardTags().contains("JasperMob")){
            e.setCancelled(true);
            JasperEntity.getMob(mob)
                    .damageThisMob( (int) e.getDamage())
                    .updateHealthDisplay()
                    .playHurtAnimation(player);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            event.setCancelled(true);
        }
    }
}
