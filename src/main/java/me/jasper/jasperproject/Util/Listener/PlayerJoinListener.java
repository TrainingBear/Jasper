package me.jasper.jasperproject.Util.Listener;

import me.jasper.jasperproject.JMinecraft.Player.Stats;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void firstJoin(PlayerJoinEvent e){
        Stats.putIfAbsent(e.getPlayer());
    }
}
