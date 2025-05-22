package me.jasper.jasperproject.Util.CustomStructure;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.extent.transform.BlockTransformExtent;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.val;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.Logger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.craftbukkit.v1_21_R3.block.CraftStructureBlock;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public final class Structure {
    private static final Map<UUID, Location> PLACED_BOX = new HashMap<>();

    public synchronized static boolean save(Player player, File save_to){
        return save(player, player.getLocation(), save_to);
    }
    public synchronized static boolean save(Player player, Location l, File save_to){
        Map<UUID, Region> map = Animator.getRegions();
        if(!map.containsKey(player.getUniqueId())) return false;

        Region region = map.get(player.getUniqueId());
        BlockVector3 to = BlockVector3.at(l.x(), l.y(), l.z());
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
        save_to.mkdirs();
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
                        EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(player.getWorld()))
                ){
                    clipboard.setOrigin(to);
                    ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(session , region, clipboard, region.getMinimumPoint());
                    Operations.complete(forwardExtentCopy);
                    clipboardWriter.write(clipboard);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(JasperProject.getPlugin());
        return true;
    }

    public static void render(File file, Location location, Consumer<BlockState> consumer){
        render(file, location, null, consumer);
    }
    public static void render(File file, Location location){
        render(file, location, null, null);
    }
    public static void render(File file, Location location, @Nullable Collection<Player> players, @Nullable Consumer<BlockState> consumer) throws StructureException {
        org.bukkit.World bukkitWorld = location.getWorld();
        BlockVector3 pasteLocation = BlockVector3.at(-location.getX(), -location.getY(), -location.getZ());
        Logger log = new Logger(players);
        long clip_last = System.currentTimeMillis();
        try (Clipboard clipboard = getClip(file)) {
            long clip_took = System.currentTimeMillis()-clip_last;
            long last = System.currentTimeMillis();
            World world = location.getWorld();
            for (BlockVector3 pos : clipboard.getRegion()) {
                val baseBlock = clipboard.getFullBlock(pos);
                Location world_pos = BukkitAdapter.adapt(bukkitWorld, pos.subtract(clipboard.getOrigin().add(pasteLocation)));

                BlockData adapt = BukkitAdapter.adapt(baseBlock);
                if(players==null){
                    if(world!=null){
                        Block block = world_pos.getBlock();
                        block.setBlockData(adapt, false);
                        if(consumer!=null) consumer.accept(block.getState());
                    }
                }
                else for (Player player : players) {
                    if(player==null) continue;
                    player.sendBlockChange(world_pos, adapt);
                }
            }
            long timetook = System.currentTimeMillis()-last;
            log.infoActionbar("<red><b><frame></b></red> <dark_red>-></dark_red> <light_purple>Clipboard:</light_purple> <dark_green><green><v1>ms </green></dark_green>| <gold>render:</gold> <dark_green><green><v2>ms</green></dark_green>",
                    Placeholder.unparsed("v1", String.valueOf(clip_took)),
                    Placeholder.unparsed("v2", String.valueOf(timetook)),
                    Placeholder.unparsed("frame", file.getName())
                    );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void renderWFawe(File file, Location location, Consumer<BlockState> filter, int rotationDegrees){
        renderWFawe(file, location, null, filter, rotationDegrees);
    }
    public static void renderWFawe(File file, Location location, List<Player> audiences, Consumer<BlockState> filter, int rotationDegrees){
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try ( FileInputStream fis = new FileInputStream(file);
                ClipboardReader reader = format.getReader(fis);
                Clipboard clipboard = reader.read();
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                EditSession ignored = WorldEdit.getInstance().newEditSessionBuilder()
                        .world(BukkitAdapter.adapt(location.getWorld()))
                        .build()
        ){
            AffineTransform transform = new AffineTransform();
            transform = transform.rotateY(-rotationDegrees);
            BlockTransformExtent blockTransformExtent = new BlockTransformExtent(clipboard, transform);
            ForwardExtentPacket forwardExtentPacket = new ForwardExtentPacket(blockTransformExtent, audiences, location, filter);
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    blockTransformExtent,
                    clipboard.getRegion(),
                    clipboard.getOrigin(),
                    forwardExtentPacket,
                    BukkitAdapter.asBlockVector(location)
            );
            forwardExtentCopy.setTransform(transform);
            Operations.complete(forwardExtentCopy);
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
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
