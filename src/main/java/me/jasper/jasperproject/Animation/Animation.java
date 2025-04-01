package me.jasper.jasperproject.Animation;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import lombok.Getter;
import me.jasper.jasperproject.FileConfiguration.Configurator;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public final class Animation {
    @Getter private static final HashMap<String, BukkitTask> runningTask = new HashMap<>();

    public static void stop(Player player, String name){
        FileConfiguration copig = Configurator.getConfig(name);
        copig.set("isRunning", false);
        Configurator.save(copig, name);

        runningTask.get(name).cancel();
        player.sendMessage(ChatColor.GREEN+"You stopped "+name+" animation!");
    }

    public static boolean play(Player player, String animationName){
        FileConfiguration config = Configurator.getConfig(animationName);
        boolean isRunning = config.getBoolean("isRunning");

        if(isRunning) {
            player.sendMessage(ChatColor.RED+"This animation is already running!");
            player.sendMessage(ChatColor.RED+" please use /animate stop <Animation> to stop your animation!");
            return false;
        }
        config.set("isRunning", true);
        Configurator.save(config, animationName);


        long tick = config.getLong("fps");
        Location origin = config.getLocation("origin");
        List<String> schems = Configurator.getConfig(animationName).getStringList("schem");
        File[] files = Configurator.getFile(animationName).getParentFile().listFiles((dir, name) -> !name.endsWith(".yml"));
        if(files==null){
            player.sendMessage(ChatColor.RED+"This animation schema is empty!");
            return false;
        }

        List<Runnable> schemPasterTasks = new ArrayList<>();
        for (String schem : schems) {
            File schema = Arrays.stream(files).filter(f -> f.getName().contains(schem)).findFirst().orElse(null);
            Runnable task = () -> {
                try {
                    pasteSchematic(player, origin, schema);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            schemPasterTasks.add(task);
        }
        class temp{
            Iterator<Runnable> iterator;
        }
        temp Paster = new temp();
        Paster.iterator = schemPasterTasks.iterator();
        player.sendMessage(ChatColor.GREEN+animationName +" have been played in "+origin.getX()+", "+origin.getY()+", "+origin.getZ());

        BukkitTask btask = new BukkitRunnable() {
            @Override
            public void run() {
                if(!config.getBoolean("isRunning")) {
                    player.sendMessage(ChatColor.GREEN+animationName+" has been stopped!");
                    Paster.iterator.remove();
                    this.cancel();
                }
                if(!Paster.iterator.hasNext()){
                    Paster.iterator = schemPasterTasks.iterator();
                }
                Paster.iterator.next().run();
            }
        }.runTaskTimer(JasperProject.getPlugin(), 0, tick);
        runningTask.put(animationName, btask);
        return true;
    }

    public static boolean play(String animationName, boolean flag){
        FileConfiguration config = Configurator.getConfig(animationName);
        boolean isRunning = config.getBoolean("isRunning");

        if(isRunning && !flag) {
            return false;
        }
        config.set("isRunning", true);
        Configurator.save(config, animationName);


        long tick = config.getLong("fps");
        Location origin = config.getLocation("origin");
        List<String> schems = Configurator.getConfig(animationName).getStringList("schem");
        File[] files = Configurator.getFile(animationName).getParentFile().listFiles((dir, name) -> !name.endsWith(".yml"));
        if(files==null){
            return false;
        }

        List<Runnable> schemPasterTasks = new ArrayList<>();
        for (String schem : schems) {
            File schema = Arrays.stream(files).filter(f -> f.getName().contains(schem)).findFirst().orElse(null);
            Runnable task = () -> {
                try {
                    pasteSchematic(origin, schema);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            schemPasterTasks.add(task);
        }
        class ref{
            Iterator<Runnable> iterator;
        }
        ref Paster = new ref();
        Paster.iterator = schemPasterTasks.iterator();
        Bukkit.getLogger().info(" [JasperProject] "+animationName+" have been played in "+origin.getX()+", "+origin.getY()+", "+origin.getZ());

        BukkitTask btask = new BukkitRunnable() {
            @Override
            public void run() {
                if(!config.getBoolean("isRunning")) {
                    Paster.iterator.remove();
                    this.cancel();
                }
                if(!Paster.iterator.hasNext()){
                    Paster.iterator = schemPasterTasks.iterator();
                }
                Paster.iterator.next().run();
            }
        }.runTaskTimer(JasperProject.getPlugin(), 0, tick);
        runningTask.put(animationName, btask);
        return true;
    }

    public static void saveSchematic(Location location, Region region1, File file) {
        World world = region1.getWorld();

        BlockVector3 min = region1.getMinimumPoint();
        BlockVector3 max = region1.getMaximumPoint();

        BlockVector3 origin = BlockVector3.at(location.getX(),
                location.getY(),
                location.getZ());
        CuboidRegion region = new CuboidRegion(min, max);
        Clipboard clipboard = new BlockArrayClipboard(region);
        clipboard.setOrigin(origin);

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
            Operations.complete(copy);

            try (ClipboardWriter writer = BuiltInClipboardFormat.FAST.getWriter(new FileOutputStream(file))) {
                writer.write(clipboard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void pasteSchematic(Player player, Location location, File file) throws IOException {

        if (!file.exists()) {
            player.sendMessage(ChatColor.RED+"Schematic file not found");
            return;
        }

        BlockVector3 blockVector3 = BlockVector3.at(location.getX(), location.getY(), location.getZ());

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            Bukkit.broadcastMessage("Invalid schematic format.");
            return;
        }

        FileInputStream fis = new FileInputStream(file);
        ClipboardReader reader = format.getReader(fis);
        Clipboard clipboard = reader.read();


        if (clipboard == null) {
            player.sendMessage("Clipboard is null! Make sure the schematic exists.");
            return;
        }

        World world = BukkitAdapter.adapt(location.getWorld());

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(blockVector3)
                    .ignoreAirBlocks(false)
                    .build();

            Operations.complete(operation);
            fis.close();
        }
    }
    static void pasteSchematic(Location location, File file) throws IOException {

        if (!file.exists()) {
            Bukkit.getLogger().info("[JasperProject] Schematic file not found");
            return;
        }

        BlockVector3 blockVector3 = BlockVector3.at(location.getX(), location.getY(), location.getZ());

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            Bukkit.getLogger().info("[JasperProject] Invalid schematic format.");
            return;
        }

        FileInputStream fis = new FileInputStream(file);
        ClipboardReader reader = format.getReader(fis);
        Clipboard clipboard = reader.read();


        if (clipboard == null) {
            return;
        }

        World world = BukkitAdapter.adapt(location.getWorld());

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(blockVector3)
                    .ignoreAirBlocks(false)
                    .build();

            Operations.complete(operation);
            fis.close();
        }
    }
}
