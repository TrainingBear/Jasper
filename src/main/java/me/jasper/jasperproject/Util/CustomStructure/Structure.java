package me.jasper.jasperproject.Util.CustomStructure;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import lombok.val;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_21_R1.block.CraftStructureBlock;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

import java.io.*;
import java.util.*;

public final class Structure {
    private static final Map<UUID, Location> PLACED_BOX = new HashMap<>();

    public static void save(File config_file, String name) throws StructureException{
        FileConfiguration config = YamlConfiguration.loadConfiguration(config_file);
        if(config.contains("region") || config.contains("origin")) throw new StructureException("region can't be found!");

        Region region = config.getObject("region", Region.class);
        BlockVector3 to = config.getObject("origin", BlockVector3.class);
        File file = new File(config_file.getParentFile(), "\\"+name+".schem");
        World we_world = region.getWorld();

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        try(EditSession session = WorldEdit.getInstance().newEditSession(we_world);
            ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_V3_SCHEMATIC.getWriter(new FileOutputStream(file))

        ){
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(session , region, clipboard, to);
            Operations.complete(forwardExtentCopy);
            writer.write(clipboard);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void render(File file, Location location, double radius)throws StructureException{
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(!config.contains("origin")) throw new StructureException("Failed to render this animation, because region of this file is null");

        org.bukkit.World bukkitWorld = location.getWorld();
        BlockVector3 pasteLocation = BlockVector3.at(location.getX(), location.getY(), location.getZ());

        try(Clipboard clipboard = ClipboardFormats.findByFile(file).getReader(new FileInputStream(file)).read()) {
            for(BlockVector3 pos : clipboard.getRegion()){
                val baseBlock = clipboard.getFullBlock(pos);
                val world_pos = BukkitAdapter.adapt(bukkitWorld, pos.subtract(clipboard.getOrigin().add(pasteLocation)));

                Collection<Player> players = bukkitWorld.getNearbyPlayers(location, radius);
                for (Player player : players) {
                    player.sendBlockChange(world_pos, BukkitAdapter.adapt(baseBlock));
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
