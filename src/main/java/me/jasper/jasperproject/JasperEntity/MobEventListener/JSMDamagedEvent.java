package me.jasper.jasperproject.JasperEntity.MobEventListener;

import me.jasper.jasperproject.JasperEntity.JasperEntity;
import me.jasper.jasperproject.JasperEntity.MobEvent.JasperMobDamagedEvent;
import me.jasper.jasperproject.JasperProject;
import net.minecraft.world.entity.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
    public void onDamaged(EntityDamageByEntityEvent e){
        if ((e.getDamager() instanceof Player player) && e.getEntity().getScoreboardTags().contains("JasperMob")){
            e.setCancelled(true);
            JasperEntity.getMob((LivingEntity) e.getEntity())
                    .updateHealthDisplay(e.getDamage())
                    .playHurtAnimation(player)
                    .damageThisMob(e.getDamage());
        }
    }
}
