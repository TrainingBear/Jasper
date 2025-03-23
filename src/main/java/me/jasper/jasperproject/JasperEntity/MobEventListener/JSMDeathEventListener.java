package me.jasper.jasperproject.JasperEntity.MobEventListener;

import me.jasper.jasperproject.JasperEntity.MobEvent.JasperMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class JSMDeathEventListener implements Listener {

    //this gonna be catch onDeath
    @EventHandler
    public void onDeath(JasperMobDeathEvent e){

    }

    @EventHandler
    public void onDeath(EntityDeathEvent e){
        if(e.getEntity().getScoreboardTags().contains("JasperMob")){
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), e.getEntity().getDeathSound(), 1, 0.5f);
            for(Entity name : e.getEntity().getPassengers()){
                name.remove();
            }
            Bukkit.broadcastMessage("Entity death by "+ e.getEntity().getKiller());
        }
    }
}
