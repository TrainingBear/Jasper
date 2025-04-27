package me.jasper.jasperproject.Util.Listener;

import me.jasper.jasperproject.Bazaar.util.ProductManager;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import java.sql.SQLException;

public class AutoSaveListener implements Listener {
    private long last_tick = System.currentTimeMillis();
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) throws SQLException {
        this.save();
    }

    boolean isSaving = false;
    public void save(){
        if(isSaving)return;
        isSaving = true;
        Bukkit.getScheduler().runTaskLater(JasperProject.getPlugin(), ()-> isSaving = false, 100);

        try{
            ProductManager.saveAll();
        } catch (SQLException e) {
            JasperProject.getPlugin().getLogger().warning("There something wrong when saving Bazaar Product");
            e.printStackTrace();
        }

        Bukkit.broadcastMessage("Saved! ("+(System.currentTimeMillis()-last_tick)/1000+" seconds from last tick)");
        last_tick = System.currentTimeMillis();
    }
}
