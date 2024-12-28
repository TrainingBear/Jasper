package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityFinder {
    JasperProject plugin;

    public EntityFinder(JasperProject plugin) {
        this.plugin = plugin;
    }

    List<Entity> Clock = new ArrayList<>();
    List<Entity> MainClock = new ArrayList<>();
    List<Entity> Clock2 = new ArrayList<>();
    List<Entity> entityInTheBox = new ArrayList<>();

    public List<Entity> getEntitywTag(String tag){
        Location box_pos1 = new Location(Bukkit.getWorld("spawn"),26 ,174 ,10);
        Location box_pos2 = new Location(Bukkit.getWorld("spawn"),16 ,185, -1);
        for(Entity entity : Bukkit.getWorld("spawn").getNearbyEntities(new Location(Bukkit.getWorld("spawn"),27 ,174, 0),18 ,185 ,10)){
            if(entity.getScoreboardTags().contains(tag)){
                this.entityInTheBox.add(entity);
            }
        }
        return this.entityInTheBox;
    }

}
