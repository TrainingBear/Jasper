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
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_21_R1.block.CraftStructureBlock;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.io.*;
import java.util.*;

public final class Structure {
    private static final Map<UUID, Location> PLACED_BOX = new HashMap<>();

    public synchronized static boolean save(Player player, File save_to){
        Map<UUID, Region> map = Animator.getRegions();
        if(!map.containsKey(player.getUniqueId())) return false;

        Location l = player.getLocation();
        Region region = map.get(player.getUniqueId());

        player.sendMessage("saved region with length of "+region.getLength());

        BlockVector3 to = BlockVector3.at(l.x(), l.y(), l.z());
        for (BlockVector3 block : region) {
            player.sendMessage("Saving -> " +block.toString());
        }
        return write(player, region, to, save_to);
    }

    public synchronized static boolean saveFrame(Player player, File save_to, String name) throws StructureException{
        File regionSchem = new File(save_to, "\\region.schem");
        if(!regionSchem.exists()) return false;
        player.sendMessage(regionSchem.getAbsolutePath());

        Region region;
        BlockVector3 to;
        try(Clipboard clipboard = getClip(regionSchem)){
            region = clipboard.getRegion();
            to = clipboard.getOrigin();
        }catch (IOException e){
            player.sendMessage(e.getMessage()+e.getCause());
            return false;
        }

        File file = new File(save_to, "\\"+name+".schem");
        for (BlockVector3 block : region) {
            player.sendMessage("Saving -> " +block.toString());
        }
        return write(player, region, to, file);
    }

    private static boolean write(Player player, Region region, BlockVector3 to, File file) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try(
                        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
                        ClipboardWriter clipboardWriter = BuiltInClipboardFormat.SPONGE_V3_SCHEMATIC.getWriter(new FileOutputStream(file));
                        EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(player.getWorld()))){
                          clipboard.setOrigin(to);
                    ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(session , region, clipboard, region.getMinimumPoint());
                    Operations.complete(forwardExtentCopy);
                    clipboardWriter.write(clipboard);

                    for (BlockVector3 block : region) {
                        player.sendMessage("Saved ->" + block.toString());
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(JasperProject.getPlugin());
        return true;
    }


    public static void render(File file, Location location, double radius)throws StructureException {
        org.bukkit.World bukkitWorld = location.getWorld();
        BlockVector3 pasteLocation = BlockVector3.at(-location.getX(), -location.getY(), -location.getZ());

        Bukkit.broadcastMessage("Loading "+file.getAbsolutePath());
        Collection<Player> players = null;
        try (Clipboard clipboard = getClip(file)) {
            Location world_pos = null;
            players = bukkitWorld.getNearbyPlayers(location, radius);
            for (BlockVector3 pos : clipboard.getRegion()) {
                val baseBlock = clipboard.getFullBlock(pos);
                world_pos = BukkitAdapter.adapt(bukkitWorld, pos.subtract(clipboard.getOrigin().add(pasteLocation)));

                for (Player player : players) {
//                    player.sendMessage(MiniMessage.miniMessage().deserialize("Played an animation at <x>, <y>, <z>",
//                            Placeholder.unparsed("x", String.valueOf(world_pos.getX())),
//                            Placeholder.unparsed("y", String.valueOf(world_pos.getY())),
//                            Placeholder.unparsed("z", String.valueOf(world_pos.getZ()))
//                    ));
                    player.sendBlockChange(world_pos, BukkitAdapter.adapt(baseBlock));
                }
            }
            for (Player player : players) {
                player.sendMessage("u have been recived "+file.getName());
            }
//            Bukkit.broadcast(MiniMessage.miniMessage().deserialize("Played an animation at <x>, <y>, <z>",
//                    Placeholder.unparsed("x", String.valueOf(world_pos.getX())),
//                    Placeholder.unparsed("y", String.valueOf(world_pos.getY())),
//                    Placeholder.unparsed("z", String.valueOf(world_pos.getZ()))
//                    ));

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

    private static Clipboard getClip(File file) throws IOException {
        return ClipboardFormats.findByFile(file).getReader(new FileInputStream(file)).read();
    }
}
