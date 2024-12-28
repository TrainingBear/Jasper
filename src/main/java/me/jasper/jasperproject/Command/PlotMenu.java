package me.jasper.jasperproject.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlotMenu implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            Inventory inventory1 = Bukkit.createInventory(p,27, ChatColor.AQUA.BOLD+"Plot Menu");

            ItemStack g1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta mg1 = g1.getItemMeta();
            mg1.setDisplayName(ChatColor.GRAY+".");
            g1.setItemMeta(mg1);


            ItemStack i1 = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta mi1 = i1.getItemMeta();
            mi1.setDisplayName(ChatColor.GRAY+"Your Plot");
            List<String> lore1 = new ArrayList<String>();
            lore1.add(ChatColor.BLUE+"");
            lore1.add(ChatColor.BLUE+"Set your plot here");
            mi1.setLore(lore1);
            i1.setItemMeta(mi1);

            inventory1.setItem(1,g1);
            inventory1.setItem(2,g1);
            inventory1.setItem(3,g1);
            inventory1.setItem(4,g1);
            inventory1.setItem(5,g1);
            inventory1.setItem(6,g1);
            inventory1.setItem(7,g1);
            inventory1.setItem(8,g1);
            inventory1.setItem(0,g1);

            inventory1.setItem(18,g1);
            inventory1.setItem(19,g1);
            inventory1.setItem(20,g1);
            inventory1.setItem(21,g1);
            inventory1.setItem(22,g1);
            inventory1.setItem(23,g1);
            inventory1.setItem(24,g1);
            inventory1.setItem(25,g1);
            inventory1.setItem(26,g1);



            inventory1.setItem(13,i1);
            p.openInventory(inventory1);


            }


        return true;
    }
}
