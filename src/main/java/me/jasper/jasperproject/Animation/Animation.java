package me.jasper.jasperproject.Animation;

import com.ibm.icu.util.CodePointMap;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
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
import me.jasper.jasperproject.FileConfiguration.Editor;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class Animation {
    //i change this
    @Getter private static final HashMap<String, BukkitTask> runningTask = new HashMap<>();
    private static final Map<String, List<String>> animationNameTabCompleter = new HashMap<>();

    public static List<String> getOwnerAnimations(String name){
        return animationNameTabCompleter.get(name);
    }

    public static CompletableFuture<Suggestions> getOwnerAnimations(String name, SuggestionsBuilder builder){
        for (String s : animationNameTabCompleter.get(name)) {
            builder.suggest(s);
        }
        return builder.buildFuture();
    }

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

    public static void loadListFileTabCompleter(){
        File file = new File(JasperProject.getPlugin().getDataFolder()+"\\Animations");
        File[] files = file.listFiles();
        if(files==null) return;
        animationNameTabCompleter.clear();
//        Bukkit.getLogger().info(sender+" invoking GetAnimationConfig(TabCompleter in AnimationCommand.java)");
        for (File file1 : files) {
            File[] configs = file1.listFiles((dir, name) -> name.endsWith(".yml"));
            if(configs==null) continue;
            for (File config : configs) {
                final FileConfiguration configuration = YamlConfiguration.loadConfiguration(config);
                final String owner = configuration.getString("owner");
                List<String> member = configuration.getStringList("members");
                for (String s : member) {
                    animationNameTabCompleter.computeIfAbsent(owner, k -> new ArrayList<>()).
                            add(s);
                }
                animationNameTabCompleter.computeIfAbsent(owner, k -> new ArrayList<>()).
                        add(owner);
            }
        }
    }

    private static boolean isAnimationExist(@Nullable Player player, String name){
        boolean exist = Configurator.getConfig(name)!=null;
        if(!exist && player!=null){
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>name is not exist!</red>",Placeholder.unparsed("name", name)));
        }
        return exist;
    }

    public static int addFrame(Player player, String animation_name, String frame_name){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        final Region region = Animator.getRegions().get(player.getUniqueId());

        File schem = new File(Configurator.getFile(animation_name).getParentFile(),"\\"+frame_name+".schem");
        if(schem.exists()){
            player.sendMessage(ChatColor.RED+"this frame is already exist!");
            return Command.SINGLE_SUCCESS;
        }
        FileConfiguration config = Configurator.getConfig(animation_name);
        if(!config.contains("schem")) config.set("schem", new ArrayList<>());
        List<String> list = config.getStringList("schem");
        list.add(frame_name);
        config.set("schem", list);

        Configurator.save(config, animation_name);
        Animation.saveSchematic(player.getLocation(), region, schem);
        player.sendMessage(frame_name +" saved as schem");

        return Command.SINGLE_SUCCESS;
    }

    public static int addMember(Player player, String animation_name, String member){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        FileConfiguration config = Configurator.getConfig(animation_name);
        if(!config.contains("members")) config.set("members", new ArrayList<>());
        List<String> list = config.getStringList("members");
        list.add(member);
        config.set("members", list);
        Configurator.save(config, animation_name);

        player.sendMessage(MiniMessage.miniMessage().deserialize("<color:#fdff8c>you added <color:#c2ff33><reset>member</reset><color:#fdff8c> to your <color:#06aa00>animation</color> as member!</color>",
                Placeholder.unparsed("name", member),
                Placeholder.unparsed("animation", animation_name)));

        return Command.SINGLE_SUCCESS;
    }

    public static int loadFrame(Player player, String animation_name){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        final FileConfiguration config = Configurator.getConfig(animation_name);
        if(!config.contains("schem")) {
            config.set("schem", new ArrayList<>());
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>this animation has no frames!</red>"));
            Configurator.save(config, animation_name);
            return Command.SINGLE_SUCCESS;
        }
        final List<String> frames = config.getStringList("schem");
        player.sendMessage(MiniMessage.miniMessage().deserialize("<aqua>list frames of</aqua> <b><green>name :</green></b>",Placeholder.unparsed("name", animation_name)));
        for (int i = 0; i < frames.size(); i++) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(i+".) <b>frame </b><red>[<click:run_command:'/animate edit name delete frame'><hover:show_text:'<dark_red>Click to delete this frame</dark_red>'>delete</hover></click>]</red> <green><click:run_command:'/animate edit name load frame'>[<hover:show_text:'<dark_green>click to load this frame</dark_green>'>load</hover>]</click></green>",
                    Placeholder.unparsed("frame", frames.get(i)),
                    Placeholder.unparsed("name", animation_name)
                    ));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int deleteFrame(Player player, String animation_name, String frame_name){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        FileConfiguration config = Configurator.getConfig(animation_name);
        if(!config.contains("schem")) {
            config.set("schem", new ArrayList<>());
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>this animation has no frames!</red>"));
            Configurator.save(config, animation_name);
            return Command.SINGLE_SUCCESS;
        }
        File[] files = Configurator.getFile(animation_name).getParentFile().listFiles((dir, name) -> name.equals(frame_name+".schem"));
        for (File file : files) {
            file.delete();
        }
        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>- deleted frame</red>",Placeholder.unparsed("frame", frame_name)));
        List<String> schem = config.getStringList("schem");
        schem.remove(frame_name);
        config.set("schem", schem);
        Configurator.save(config, animation_name);
        return Command.SINGLE_SUCCESS;
    }

    public static int clearFrame(Player player, String animation_name){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        FileConfiguration config = Configurator.getConfig(animation_name);
        if(!config.contains("schem")) {
            config.set("schem", new ArrayList<>());
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>this animation has no frames!</red>"));
            Configurator.save(config, animation_name);
            return Command.SINGLE_SUCCESS;
        }
        File[] files = Configurator.getFile(animation_name).getParentFile().listFiles((dir, name) -> !name.endsWith(".yml"));
        for (File file1 : files) {
            if(file1.delete()){
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>- deleted frame</red>",Placeholder.unparsed("frame", file1.getName())));
            }
        }
        config.set("schem", null);
        Configurator.save(config, animation_name);
        return Command.SINGLE_SUCCESS;
    }

    public static int setFPS(Player player, String animation_name, float v){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        FileConfiguration config = Configurator.getConfig(animation_name);
        config.set("FPS", v);
        Configurator.save(config, animation_name);
        return Command.SINGLE_SUCCESS;
    }

    public static int setLocation(Player player, String animation_name, Location v){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }

        FileConfiguration config = Configurator.getConfig(animation_name);
        config.set("location", v);
        Configurator.save(config, animation_name);
        return Command.SINGLE_SUCCESS;
    }
    public static int setRegion(Player player, String animation_name, Region v){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }

        FileConfiguration config = Configurator.getConfig(animation_name);
        config.set("location", v);
        Configurator.save(config, animation_name);
        return Command.SINGLE_SUCCESS;
    }
}
