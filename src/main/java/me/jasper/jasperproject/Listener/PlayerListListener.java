package me.jasper.jasperproject.Listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerListListener implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
//        if(e.getView().getTitle().equalsIgnoreCase(PlayerFinder.title)){
//            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD){
//                Player target = p.getServer().getPlayerExact(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
//                p.sendMessage("the target is "+target.getName());
//                target.sendMessage("You clicked this item!");
//            }
////            e.setCancelled(true);
//        }
    }
}
