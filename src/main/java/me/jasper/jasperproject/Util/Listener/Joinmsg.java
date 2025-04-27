package me.jasper.jasperproject.Util.Listener;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Joinmsg implements Listener {
    JasperProject plugin;
    public Joinmsg(JasperProject plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        // Delay setting the join message by 1 tick to ensure display name is set
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.getServer().broadcastMessage(player.getDisplayName()+ChatColor.DARK_AQUA+" Hop in to the game!" );
        }, 10L);

    }
    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent e){
        e.setQuitMessage(e.getPlayer().getDisplayName() + ChatColor.GRAY+" Disconected");
    }
}
