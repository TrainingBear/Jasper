package me.jasper.jasperproject.Listener;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InvenAhhListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getWhoClicked() instanceof Player ){
            Player p = (Player) e.getWhoClicked();
            ItemStack clicki = e.getCurrentItem();

            if (clicki.getType() == Material.BEDROCK){
                p.sendMessage(ChatColor.RED.BOLD+"How did u get that item in your inventory!");

                if(e.isRightClick()){

                    clicki.setType(Material.DIRT);
                    ItemMeta mclicki = clicki.getItemMeta();
                    mclicki.setDisplayName(ChatColor.RED.BOLD+"HEY U CANT HAVE THAT >:(");
                    clicki.setItemMeta(mclicki);
                    p.sendMessage("You Right Clicked the bedrock!");
                }

            }
                else {
//                    p.sendMessage(ChatColor.BLUE+"U clicked "+clicki.getType()+"!");
                }
        }
    }

    public static class VanishJoinListener implements Listener {
        JasperProject plugin;
        public VanishJoinListener(JasperProject plugin){
            this.plugin = plugin;
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e){
            Player p = e.getPlayer();
            if(e.getPlayer().hasPermission("jasperproject.liat_setan")){
                e.getPlayer().sendMessage(ChatColor.DARK_RED+"You have ability to see setan!!");
            }else{
                e.getPlayer().hidePlayer(plugin, e.getPlayer());

            }
        }
    }
}
