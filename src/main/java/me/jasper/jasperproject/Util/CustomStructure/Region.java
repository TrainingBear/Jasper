package me.jasper.jasperproject.Util.CustomStructure;

import org.bukkit.util.BlockVector;

public class Region {
    BlockVector pos1;
    BlockVector pos2;
    public Region(BlockVector pos1, BlockVector pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }
    public BlockVector maxPoint(){
        return new BlockVector(Math.max(pos1.getX(), pos2.getX()),
                Math.max(pos1.getY(), pos2.getY()),
                Math.max(pos1.getZ(), pos2.getZ()));
    }
    public BlockVector minPoint(){
        return new BlockVector(Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ()));
    }
}