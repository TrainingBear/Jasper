package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SummonCustomEntity {
    JasperProject plugin;

    public SummonCustomEntity(JasperProject plugin) {
        this.plugin = plugin;
    }

    public ItemDisplay spawnJarumJam(Player player, Location location, Material display , String tag , float height, float width) {
        ItemDisplay jarumJam = (ItemDisplay) player.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        jarumJam.getScoreboardTags().add(tag.toLowerCase());
        jarumJam.setItemStack(new ItemStack(display));
        jarumJam.setDisplayHeight(height);
        jarumJam.setDisplayWidth(width);
        player.sendMessage(String.valueOf(jarumJam.getDisplayHeight()));
        player.sendMessage(String.valueOf(jarumJam.getDisplayWidth()));
        player.sendMessage(String.valueOf(jarumJam.getItemDisplayTransform()));
        player.teleport(jarumJam);
        player.sendMessage(ChatColor.LIGHT_PURPLE+"A entity just spawned with tag: "+getTag(jarumJam));
        return jarumJam;
    }
    public static String getTag(ItemDisplay entity){
        String tag = entity.getScoreboardTags().toString();
        return tag;
    }

    public void killEntity(ItemDisplay entity,String tag) {
        ItemDisplay target = entity;
        if(target.getScoreboardTags().contains(tag)){
            target.remove();
        }
    }
}
