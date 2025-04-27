package me.jasper.jasperproject.Util.Listener;

import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class ProjectileHit implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e){
        if(Boolean.TRUE.equals(e.getEntity().getPersistentDataContainer().get(JKey.removeWhenHit, PersistentDataType.BOOLEAN))) {
            new BukkitRunnable(){
                @Override
                public void run(){
                    e.getEntity().remove();
                    cancel();//failsafe
                }
            }.runTaskLater(JasperProject.getPlugin(),5);

        }
    }
}
