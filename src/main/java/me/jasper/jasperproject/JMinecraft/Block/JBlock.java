package me.jasper.jasperproject.JMinecraft.Block;

import me.jasper.jasperproject.JMinecraft.Item.JItem;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class JBlock extends Event implements Listener {
    private String id;
    private NamespacedKey key;

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Block block = e.getClickedBlock();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Block block = e.getBlock();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
    }
}
