package me.jasper.jasperproject.Dungeon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class SetTheBlock {
    public static void setBlockAT(Location pos1, Location pos2){
        int minx = (int) Math.min(pos1.getX(),pos2.getX());
        int maxx = (int) Math.max(pos1.getX(),pos2.getX());

        int minz = (int) Math.min(pos1.getZ(),pos2.getZ());
        int maxz = (int) Math.max(pos1.getZ(),pos2.getZ());

        int miny = (int) Math.min(pos1.getY(),pos2.getY());
        int maxy = (int) Math.max(pos1.getY(),pos2.getY());

        for (int i = minx; i < maxx;i++){
            for (int j = minz; j < maxz;j++){
                for (int k = miny; k < maxy; k++) {
                    Location currentblockloc = new Location(Bukkit.getWorld("test"),i,k,j);
                    Block block = currentblockloc.getBlock();
                    block.setType(Material.GREEN_WOOL);
//
//                    block.setType(material);
                }
            }
        }
    }

}
