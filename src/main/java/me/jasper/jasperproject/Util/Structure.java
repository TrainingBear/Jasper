package me.jasper.jasperproject.Util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.craftbukkit.v1_21_R1.block.CraftStructureBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

public class Structure {
    CraftStructureBlock structureBlock;

    public static void createStructure(Player player){
        Location location = player.getLocation();
        location.getBlock().setType(Material.STRUCTURE_BLOCK);
        CraftStructureBlock block = (CraftStructureBlock) location.getBlock().getBlockData();
        block.setStructureSize(new BlockVector(5, 5, 5));
    }
}
