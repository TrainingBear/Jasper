package me.jasper.jasperproject.Util.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class whenRainCancel implements Listener {
    @EventHandler
    public void onRain(WeatherChangeEvent e){
//     Bukkit.getServer().broadcastMessage("The world is changing weather"+Bukkit.getServer().getWorlds().toArray());
        e.setCancelled(true);
    }
}
