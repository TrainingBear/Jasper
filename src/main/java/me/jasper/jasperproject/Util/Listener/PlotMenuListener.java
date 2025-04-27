package me.jasper.jasperproject.Util.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlotMenuListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getView().getTitle().equals(ChatColor.AQUA.BOLD+"Plot Menu")){
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case GRASS_BLOCK:
                    p.sendMessage("Its just Grass Block");
                    break;
                case GRAY_STAINED_GLASS:
                    p.sendMessage("Just decoration");
                    break;
            }
        }
    }
}
