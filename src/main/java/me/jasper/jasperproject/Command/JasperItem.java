package me.jasper.jasperproject.Command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class JasperItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player p)){commandSender.sendMessage("You need to be a player to execute this command!");}
        Player p = (Player) commandSender;

        if(strings[0].equalsIgnoreCase("give")) {

//            LAUNCH PAD ITEMSTACK
//            LAUNCHER
            ItemStack launchPad_pos1 = new ItemStack(Material.SLIME_BLOCK);
            ItemStack launchPad_pos2 = new ItemStack(Material.SLIME_BLOCK);
            ItemMeta launchPad_item_meta_pos1 = launchPad_pos1.getItemMeta();
            ItemMeta launchPad_item_meta_pos2 = launchPad_pos2.getItemMeta();
            List<String> loreLaunchPad = new ArrayList<>();
            loreLaunchPad.add(ChatColor.BLUE + "Very bouncy that make you fly so high!");
            loreLaunchPad.add(ChatColor.BLUE + "and Travel you far away");
            loreLaunchPad.add(ChatColor.GREEN + "");
            loreLaunchPad.add(ChatColor.GREEN +""+ChatColor.BOLD+ChatColor.MAGIC+" AHA " +ChatColor.RESET+""+ChatColor.GREEN+ChatColor.BOLD+"UNCOMMON ITEM"+""+ChatColor.MAGIC+" AHA " +ChatColor.RESET);
            launchPad_item_meta_pos1.setLore(loreLaunchPad);
            launchPad_item_meta_pos1.setDisplayName(ChatColor.GREEN + "Launch Pad (pos 1)");
            launchPad_item_meta_pos2.setLore(loreLaunchPad);
            launchPad_item_meta_pos2.setDisplayName(ChatColor.GREEN + "Launch Pad (pos 2)");
            launchPad_pos1.setItemMeta(launchPad_item_meta_pos1);
            launchPad_pos2.setItemMeta(launchPad_item_meta_pos2);
            if(strings[1].equalsIgnoreCase("launch_pad_pos1")){
                p.getInventory().addItem(launchPad_pos1);}
            if(strings[1].equalsIgnoreCase("launch_pad_pos2")){
                p.getInventory().addItem(launchPad_pos2);}


            //BARRIER BLOCK
        ItemStack item1 = new ItemStack(Material.BARRIER,1);
        ItemMeta mitem1 = item1.getItemMeta();
        mitem1.setDisplayName(ChatColor.RED.BOLD + "BARRIER");
        List<String> loreTest = new ArrayList<>();
        loreTest.add(ChatColor.BLUE+" How u could have this?");
        loreTest.add(ChatColor.BLUE+" Interisting");
        mitem1.setLore(loreTest);
        item1.setItemMeta(mitem1);

            if(strings[1].equalsIgnoreCase("barrier")){

        p.getInventory().addItem(item1);
            }

        //TEST ITEM
        ItemStack test1 = new ItemStack(Material.BEDROCK);
        ItemMeta mtest1 = test1.getItemMeta();
        mtest1.setDisplayName("THIS IS A BEDROCK");
        test1.setItemMeta(mtest1);
        p.getInventory().addItem(test1);
        test1.setType(Material.ACACIA_BOAT);
            if(strings[1].equalsIgnoreCase("test")){

        p.getInventory().addItem(test1);
            }
        }










        return true;
    }
}
