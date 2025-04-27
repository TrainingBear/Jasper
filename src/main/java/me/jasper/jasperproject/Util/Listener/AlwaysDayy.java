package me.jasper.jasperproject.Util.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class AlwaysDayy implements Listener {
    @EventHandler
    public void alwaysday(TimeSkipEvent e){
        e.setCancelled(true);
    }
}
