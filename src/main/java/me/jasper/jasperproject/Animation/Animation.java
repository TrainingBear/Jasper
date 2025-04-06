package me.jasper.jasperproject.Animation;

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
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class Animation {
    @Getter private static final HashMap<String, BukkitTask> runningTask = new HashMap<>();
    @Getter private static final Plugin plugin = JasperProject.getPlugin();
    private static final Map<String, List<String>> animationNameTabCompleter = new HashMap<>();

    private static Component deserialize(String message, TagResolver... resolvers){
        return MiniMessage.miniMessage().deserialize(message, resolvers);
    }
//    private static Component deserialize(String message, TagResolver... resolvers){
//        return MiniMessage.miniMessage().deserialize(message, resolvers);
//    }
    private static Configurator getCompound(String name){
        return JasperProject.getAnimationConfig().getCompound(name);
    }
    public static List<String> getOwnerAnimations(String name){
        return animationNameTabCompleter.get(name);
    }
    public static CompletableFuture<Suggestions> getOwnerAnimations(String name, SuggestionsBuilder builder){
        for (String s : animationNameTabCompleter.get(name)) {
            builder.suggest(s);
        }
        return builder.buildFuture();
    }

    public static int stop(Player player, String name){
        if(isAnimationExist(player, name)){
            return Command.SINGLE_SUCCESS;
        }
        getCompound(name).edit(name, e -> {
            e.set("isRunning", false);
            return e;
        });
        player.sendMessage(ChatColor.GREEN+"You stopped "+name+" animation!");
        if(runningTask.containsKey(name)){
            runningTask.remove(name).cancel();
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int update(Player player, String animationName){
        if(isAnimationExist(player, animationName)){
            return Command.SINGLE_SUCCESS;
        }
        stop(player, animationName);
        play(player, animationName);
        return Command.SINGLE_SUCCESS;
    }
    public static int play(@Nullable Player player, String animationName){
        if(isAnimationExist(player, animationName)){
            return Command.SINGLE_SUCCESS;
        }

        FileConfiguration config = getCompound(animationName).getConfig(animationName);
        if(player!=null){
            player.sendMessage("your are " + config.get("owner") + " isn't?");
            player.sendMessage("is it running? " + config.getBoolean("isRunning"));
        }
        boolean isRunning = config.getBoolean("isRunning");
        if(isRunning) {

            if(player!=null){
                player.sendMessage(deserialize("<red>This animation is already running!</red>"));
                player.sendMessage(deserialize(" <red>please use <click:suggest_command:'/animate stop <animation>'><hover:show_text:'<red>Click to stop this animation</red>'>/animate stop <animation></hover></click> to stop your animation!</red>",
                        Placeholder.unparsed("animation", animationName)
                ));
            }
            return Command.SINGLE_SUCCESS;
        }
        getCompound(animationName).edit(animationName, e -> {
            e.set("isRunning", true);
            return e;
        });

        boolean loop = config.getBoolean("loop");
        long tick = config.getLong("fps");
        Location origin = config.getLocation("origin");
        List<String> schems = config.getStringList("schem");
        File[] files = getCompound(animationName).getParent().listFiles((dir, name) -> !name.endsWith(".yml"));

        if(files==null){
            if(player!=null){
                player.sendMessage(ChatColor.RED + "This animation schema is empty!");
            }
            return Command.SINGLE_SUCCESS;
        }

        List<Runnable> schemPasterTasks = new ArrayList<>();
        for (String schem : schems) {
            File schema = Arrays.stream(files).filter(f -> f.getName().contains(schem)).findFirst().orElse(null);
            if (schema==null) continue;
            Runnable task = () -> {
                player.sendMessage("running "+schem);
                try {
                    pasteSchematic(player, origin, schema);
                }catch (IOException e) {
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
        if(player!=null){
            player.sendMessage(ChatColor.GREEN + animationName + " have been played in " + origin.getX() + ", " + origin.getY() + ", " + origin.getZ());
        }
        BukkitTask btask = new BukkitRunnable() {
            @Override
            public void run() {
                if(!Paster.iterator.hasNext() && loop){
                    Paster.iterator = schemPasterTasks.iterator();
                }
                Paster.iterator.next().run();
            }
        }.runTaskTimerAsynchronously(JasperProject.getPlugin(), 0, tick);
        runningTask.put(animationName, btask);
        return Command.SINGLE_SUCCESS;
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
    static void pasteSchematic(@Nullable Player player, Location location, File file) throws IOException {

        if (!file.exists()) {
            if(player!=null){
                player.sendMessage(ChatColor.RED + "Schematic file not found");
            }return;
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
            if(player!=null){
                player.sendMessage("Clipboard is null! Make sure the schematic exists.");
            }return;
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

    public static void loadCommandTabCompleter(){
        File file = new File(JasperProject.getPlugin().getDataFolder(), "\\Animations");
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
                final String filename = Configurator.getFileName(config);

                animationNameTabCompleter.computeIfAbsent(owner, k -> new ArrayList<>()).
                            add(filename);
                plugin.getLogger().info(owner +" has "+animationNameTabCompleter.get(owner).getFirst());

                if(configuration.getBoolean("isRunning")){
                    String animation_name = Configurator.getFileName(config);
                    plugin.getLogger().info("[JasperProject] "+animation_name+" is running!");
                    Animation.play(null, animation_name);
                }
                plugin.getLogger().info("[JasperProject] Loaded "+filename);
                if(!configuration.contains("members")) continue;
                List<String> members = configuration.getStringList("members");
                for (String member : members) {
                    animationNameTabCompleter.computeIfAbsent(member, k -> new ArrayList<>()).
                            add(filename);
                }

            }
        }
    }

    public static int createNew(Player player, String animation_name, Region region){
        if(isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Configurator animation = JasperProject.getAnimationConfig()
                .newCompound(animation_name).create(animation_name);
        animation.edit(animation_name, e -> {
            e.set("owner", player.getName());
            e.set("region", region);
            return e;
        });
        animationNameTabCompleter.put(player.getName(), List.of(animation_name));
        return Command.SINGLE_SUCCESS;
    }

    private static boolean isAnimationExist(@Nullable Player player, String name){
        boolean exist = (getCompound(name)!=null) && (getCompound(name).getConfig(name)!=null);
        if(!exist && player!=null){
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><name> is not exist!</red>",Placeholder.unparsed("name", name)));
        }
        return !exist;
    }

    public static int addFrame(Player player, String animation_name, String frame_name){
        if(isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        final Region region = Animator.getRegions().get(player.getUniqueId());

        File schem = new File(getCompound(animation_name).getParent(),"\\"+frame_name+".schem");
        if(schem.exists()){
            player.sendMessage(ChatColor.RED+"this frame is already exist!");
            return Command.SINGLE_SUCCESS;
        }
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e -> {
            if(!e.contains("schem")) e.set("schem", new ArrayList<>());
            List<String> list = e.getStringList("schem");
            list.add(frame_name);
            e.set("schem", list);
            return e;
        });
        Animation.saveSchematic(player.getLocation(), region, schem);
        player.sendMessage(frame_name +" saved as schem");

        return Command.SINGLE_SUCCESS;
    }

    public static int addMember(Player player, String animation_name, String member){
        if(isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e->{
            if(!e.contains("members")) e.set("members", new ArrayList<>());
            List<String> list = e.getStringList("members");
            list.add(member);
            e.set("members", list);
            return e;
        });

        player.sendMessage(MiniMessage.miniMessage().deserialize("<color:#fdff8c>you added <color:#c2ff33><reset><name></reset><color:#fdff8c> to your <color:#06aa00><animation></color> as member!</color>",
                Placeholder.unparsed("name", member),
                Placeholder.unparsed("animation", animation_name)));

        return Command.SINGLE_SUCCESS;
    }

    public static int loadFrame(Player player, String animation_name){
        if(isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        final Configurator config = getCompound(animation_name);
        config.edit(animation_name, e -> {
            if(!e.contains("schem")) e.set("schem", new ArrayList<>());
            return e;
        });

        if(!config.getConfig(animation_name).contains("schem")) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>this animation has no frames!</red>"));
            return Command.SINGLE_SUCCESS;
        }
        final List<String> frames = config.getConfig(animation_name).getStringList("schem");
        player.sendMessage(MiniMessage.miniMessage().deserialize("<aqua>list frames of</aqua> <b><green>name :</green></b>",Placeholder.unparsed("name", animation_name)));
        for (int i = 0; i < frames.size(); i++) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(i+".) <b><frame> </b><red>[<click:run_command:'/animate edit <name> delete <frame>'><hover:show_text:'<dark_red>Click to delete this <frame></dark_red>'>delete</hover></click>]</red> <green><click:run_command:'/animate edit <name> load <frame>'>[<hover:show_text:'<dark_green>click to load this <frame></dark_green>'>load</hover>]</click></green>",
                    Placeholder.unparsed("frame", frames.get(i)),
                    Placeholder.unparsed("name", animation_name)
                    ));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int deleteFrame(Player player, String animation_name, String frame_name){
        if(isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e ->{
            if(!e.contains("schem")) e.set("schem", new ArrayList<>());
            return e;
        });
        if(!config.getConfig(animation_name).contains("schem")) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>this animation has no frames!</red>"));
            return Command.SINGLE_SUCCESS;
        }
        File[] files = config.getParent().listFiles((dir, name) -> name.equals(frame_name+".schem"));
        for (File file : files) {
            file.delete();
        }
        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>- deleted frame</red>",Placeholder.unparsed("frame", frame_name)));
        config.edit(animation_name, e->{
            List<String> schem = e.getStringList("schem");
            schem.remove(frame_name);
            e.set("schem", schem);
            return e;
        });

        return Command.SINGLE_SUCCESS;
    }

    public static int clearFrame(Player player, String animation_name){
        if(isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e->{
            if(!e.contains("schem")) e.set("schem", new ArrayList<>());
            return e;
        });
        if(!config.getConfig(animation_name).contains("schem")) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>this animation has no frames!</red>"));
            return Command.SINGLE_SUCCESS;
        }
        File[] files = config.getParent().listFiles((dir, name) -> !name.endsWith(".yml"));
        for (File file1 : files) {
            if(file1.delete()){
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>- deleted frame</red>",Placeholder.unparsed("frame", file1.getName())));
            }
        }
        config.edit(animation_name, e ->{
            e.set("schem", new ArrayList<>());
            return e;
        });
        return Command.SINGLE_SUCCESS;
    }

    public static int setFPS(Player player, String animation_name, float v){
        if(isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e ->{
            e.set("FPS", v);
            return e;
        });
        return Command.SINGLE_SUCCESS;
    }

    public static int setLocation(Player player, String animation_name, Location v){
        if(isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }

        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e->{
            e.set("location", v);
            return e;
        });
        return Command.SINGLE_SUCCESS;
    }
    public static int setRegion(Player player, String animation_name, Region v){
        if(isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }

        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e ->{
            e.set("location", v);
            return e;
        });
        return Command.SINGLE_SUCCESS;
    }

}
