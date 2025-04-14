package me.jasper.jasperproject.TabCompleter;

import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.SummonCustomEntity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SummonItemDisplay implements TabCompleter, CommandExecutor {
    JasperProject plugin;

    public SummonItemDisplay(JasperProject plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> ItemDisplayMaterialList = new ArrayList<>();
        if(strings.length==1){
            String typed = strings[0].toLowerCase();
            for(Material material : Material.values()){
                if(material.isItem() && material.toString().toLowerCase().startsWith(typed)){
                    ItemDisplayMaterialList.add(material.toString().toLowerCase());
                }
            }
        }


        return ItemDisplayMaterialList;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player p)){
            commandSender.sendMessage("U need to be an player!");
            return false;
        }
        SummonCustomEntity jarumBesar = new SummonCustomEntity(plugin);
        Material material = Material.matchMaterial(strings[0].toUpperCase());
        if(material != null && material.isItem()){
            jarumBesar.spawnJarumJam(p,p.getLocation(),material,strings[1],Float.parseFloat(strings[2]),Float.parseFloat(strings[3]));
            return true;
        }
        if(strings.length!=4){
            return false;
        }
        p.sendMessage(ChatColor.RED+"Its not a Items!");
        return false;
    }
}
