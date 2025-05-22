package me.jasper.jasperproject.Util.CustomStructure;

import com.fastasyncworldedit.core.nbt.FaweCompoundTag;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Locatable;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ForwardExtentPacket implements Extent {
    private final Extent parent;
    private final List<Player> audience; /// for packet
    private final Location location;
    private final Consumer<BlockState> consumer; /// block filter

    public ForwardExtentPacket(Extent parent, List<Player> players, Location location){
        this(parent, players, location, null);
    }
    public ForwardExtentPacket(Extent parent, Location location, Consumer<BlockState> consumer){
        this(parent, null, location, consumer);
    }
    public ForwardExtentPacket(Extent parent, List<Player> players, Location location, Consumer<BlockState> consumer){
        this.parent = parent;
        this.audience = players;
        this.location = location;
        this.consumer = consumer;
    }

    @Override
    public <B extends BlockStateHolder<B>> boolean setBlock(int x, int y, int z, B block) throws WorldEditException {
        Location location1 = new Location(location.getWorld(), x, y, z);
        BlockData adapt = BukkitAdapter.adapt(block.toBaseBlock());
        if(audience!=null){
            for (Player player : audience) {
                player.sendBlockChange(location1, adapt);
            }
        }
        else{
            Block block1 = location1.getBlock();
            block1.setBlockData(adapt, false);
            if(consumer!=null) consumer.accept(block1.getState());
            block1.getState().update();
        }
        return true;
    }

    @Override
    public BlockVector3 getMinimumPoint() {
        return parent.getMinimumPoint();
    }

    @Override
    public BlockVector3 getMaximumPoint() {
        return parent.getMaximumPoint();
    }

    @Override
    public boolean tile(int x, int y, int z, FaweCompoundTag tile) throws WorldEditException {
        return parent.tile(x, y, z, tile);
    }
}
