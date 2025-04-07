package me.jasper.jasperproject.Util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import io.papermc.paper.annotation.DoNotUse;
import lombok.val;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.craftbukkit.v1_21_R1.block.CraftStructureBlock;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.BlockVector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Structure {
    private static final Map<UUID, Location> PLACED_BOX = new HashMap<>();

    public static void createBox(Player player){
        removeBox(player);
        Region region = Animator.getRegions().get(player.getUniqueId());


        CraftPlayer craftPlayer = (CraftPlayer) player;

        BlockVector3 minPoint = region.getMinimumPoint();
        BlockVector3 maxPoint = region.getMaximumPoint();
        val x = maxPoint.x() - minPoint.x() + 1;
        val y = maxPoint.y() - minPoint.y() + 1;
        val z = maxPoint.z() - minPoint.z() + 1;



        Location structureBlockLocation = new Location(player.getWorld(), minPoint.x(), Math.max(minPoint.y() - 48, -64), minPoint.z());
        craftPlayer.sendBlockChange(structureBlockLocation, Material.STRUCTURE_BLOCK.createBlockData());
        CraftStructureBlock block = (CraftStructureBlock) Material.STRUCTURE_BLOCK.createBlockData().createBlockState();

        block.setUsageMode(UsageMode.SAVE);
        block.setStructureSize(new BlockVector(Math.min(x, 48), Math.min(y, 48), Math.min(48, z)));
        block.setRelativePosition(new BlockVector(0, minPoint.y() - 48 > -64? 48 : minPoint.y()+64, 0));
        block.setBoundingBoxVisible(true);
        block.setStructureName("jasper");

        craftPlayer.sendBlockUpdate(structureBlockLocation, block);
        PLACED_BOX.put(player.getUniqueId(), structureBlockLocation);
    }


    private static void removeBox(Player player){
        if(!PLACED_BOX.containsKey(player.getUniqueId())) return;
        Location box = PLACED_BOX.remove(player.getUniqueId());
        player.sendBlockChange(box, box.getBlock().getBlockData());
    }

    public static void destroyBox(){
        for (UUID uuid : PLACED_BOX.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            Location box = PLACED_BOX.remove(player.getUniqueId());
            player.sendBlockChange(box, box.getBlock().getBlockData());
        }
    }
}
