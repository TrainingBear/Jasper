package me.jasper.jasperproject.Util.Listener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class whenRainCancel implements Listener {
    @EventHandler
    public void onRain(WeatherChangeEvent e){
//     Bukkit.getServer().broadcastMessage("The world is changing weather"+Bukkit.getServer().getWorlds().toArray());
        e.setCancelled(true);
    }
}
