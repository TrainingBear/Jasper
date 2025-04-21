package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.Bazaar.util.ProductManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import java.sql.SQLException;

public class AutoSaveListener implements Listener {
    private long last_tick = System.currentTimeMillis();
    @EventHandler
    public void save(WorldSaveEvent event) throws SQLException {
        Bukkit.broadcastMessage("Saved! ("+(System.currentTimeMillis()-last_tick)/1000+" seconds from last tick)");
        last_tick = System.currentTimeMillis();
        ProductManager.saveAll();
    }
}
