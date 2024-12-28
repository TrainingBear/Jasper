package me.jasper.jasperproject.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class PlayerFinder implements CommandExecutor, Listener {
    public static String title = ChatColor.DARK_GREEN+"Player Finder";
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player p)) {
            commandSender.sendMessage("u need to be a player to send this command!");
            return false;
        }
        PlayerListGUI(p);
        return true;
        }
    public static void PlayerListGUI(Player player){

        ArrayList<Player> playerlist = new ArrayList<>(player.getServer().getOnlinePlayers());
        Inventory banGUI = Bukkit.createInventory(player,45,title );
        for (int i = 0; i < playerlist.size() ; i++) {
            ItemStack playerHeadList = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta playerHeadListMeta = (SkullMeta) playerHeadList.getItemMeta();
            playerHeadListMeta.setDisplayName(playerlist.get(i).getName());
            OfflinePlayer targetSkull = Bukkit.getPlayer(playerlist.get(i).getName());
            playerHeadListMeta.setOwningPlayer(targetSkull);
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD+"");
            lore.add(ChatColor.WHITE+"Player Stats:");
            lore.add(ChatColor.GRAY+" - "+ChatColor.GREEN+"Level : ");
            lore.add(ChatColor.GRAY+" - "+ChatColor.GREEN+"Player health : "+playerlist.get(i).getHealth());
            lore.add(ChatColor.GRAY+" - "+ChatColor.GREEN+"Player exp : "+playerlist.get(i).getExp());
            lore.add(ChatColor.GRAY+" - "+ChatColor.GREEN+"Player stats :");
            lore.add(ChatColor.GRAY+" - "+ChatColor.GREEN+"Skill average : ");
            lore.add(ChatColor.GRAY+" - "+ChatColor.GREEN+"Jabatan : ");
            lore.add(ChatColor.GRAY+" - "+ChatColor.GREEN+"Networth : ");
            lore.add(ChatColor.GOLD+"");
            lore.add(ChatColor.YELLOW+"\"RIGHT CLICK\" to invite this player to the party");
            lore.add(ChatColor.YELLOW+"\"RIGHT LEFT\" to add this player to your friend");
            lore.add(ChatColor.YELLOW+"\"SHIFT + RIGHT\" CLICK to report this player");
            lore.add(ChatColor.YELLOW+"\"SHIFT + LEFT\" CLICK to ignore this player");
            playerHeadListMeta.setLore(lore);
            playerHeadList.setItemMeta(playerHeadListMeta);
            banGUI.setItem(i,playerHeadList);
    }
            player.openInventory(banGUI);
    }
    public static class PlayerListListener implements Listener {
        @EventHandler
        public void onMenuClick(InventoryClickEvent e){
            Player p = (Player) e.getWhoClicked();
            if(e.getView().getTitle().equals(title)){
            e.setCancelled(true);
                ClickType clickType = e.getClick();
                if ((e.getCurrentItem().getType() == Material.PLAYER_HEAD)&&(clickType==ClickType.RIGHT)){
                    Player target = p.getServer().getPlayerExact(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
//                    p.sendMessage("the target is "+ target.getDisplayName());
                    target.sendMessage(ChatColor.BLUE+"----------------------------------------------------\n"+ChatColor.RESET +p.getDisplayName() +ChatColor.YELLOW+ " Inviting you to join their party!\n"+ChatColor.BLUE+"----------------------------------------------------");
                } else if ((e.getCurrentItem().getType() == Material.PLAYER_HEAD)&&(clickType==ClickType.LEFT)) {
                    Player target = p.getServer().getPlayerExact(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    target.sendMessage(p.getDisplayName() +ChatColor.YELLOW+ " Sending you a friend request!");
                }else if ((e.getCurrentItem().getType() == Material.PLAYER_HEAD)&&(clickType==ClickType.SHIFT_RIGHT)) {
                    Player target = p.getServer().getPlayerExact(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    target.sendMessage(p.getDisplayName() +ChatColor.YELLOW+ " Nyepuin elu, karena lu haking!");
                }else if ((e.getCurrentItem().getType() == Material.PLAYER_HEAD)&&(clickType==ClickType.SHIFT_LEFT)) {
                    Player target = p.getServer().getPlayerExact(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    target.sendMessage(p.getDisplayName() +ChatColor.YELLOW+ " is ignoring you! -10000 aura");
                }
            }
        }
    }
}
