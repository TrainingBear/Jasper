package me.jasper.jasperproject.Animation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.Getter;
import me.jasper.jasperproject.Util.FileConfiguration.Configurator;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import me.jasper.jasperproject.Util.Logger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
    @Getter private static final Map<String, List<String>> animationNameTabCompleter = new HashMap<>();
    private static final Plugin plugin = JasperProject.getPlugin();


    ///                 UTIL
    private static Configurator getCompound(String name){
        return JasperProject.getAnimationConfig().getCompound(name);
    }
    public static List<String> getOwnerAnimations(String name){
        return animationNameTabCompleter.get(name);
    }
    public static CompletableFuture<Suggestions> SUGGEST(String name, SuggestionsBuilder builder){
        for (String s : animationNameTabCompleter.get(name)) {
            builder.suggest(s);
        }
        return builder.buildFuture();
    }
    public static void loadConfig(File file){
        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if(!configuration.contains("owner")) return;
        final String owner = configuration.getString("owner");
        final String filename = Configurator.getFileName(file);
        plugin.getLogger().info("[JasperProject] [Animation] "+owner +" has "+file.getName());

        animationNameTabCompleter.computeIfAbsent(owner, k -> new ArrayList<>()).
                add(filename);

        if(configuration.getBoolean("isRunning")){
            plugin.getLogger().info("[JasperProject] "+filename+" is running!");
            Animation.play(filename);
        }

        if(!configuration.contains("members")) return;
        List<String> members = configuration.getStringList("members");
        for (String member : members) {
            animationNameTabCompleter.computeIfAbsent(member, k -> new ArrayList<>()).
                    add(filename);
        }
    }
    private static boolean isAnimationExist(@Nullable Player player, String name){
        Configurator comp = getCompound(name);
        boolean exist = (comp!=null) && (comp.getConfig(name)!=null);
        Logger log = new Logger(player);
        if(!exist) log.info("<red><name> is not exist!</red>",Placeholder.unparsed("name", name));
        return exist;
    }
    public static int repair(Player player, String animation_name){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }

        Configurator comp = getCompound(animation_name);
        comp.edit(animation_name, e->{
            e.set("owner", player.getName());
            e.set("isRunning", false);
            e.set("schem", new ArrayList<>());
            e.set("location", player.getLocation());
        });
        loadConfig(comp.getFile(animation_name));
        return Command.SINGLE_SUCCESS;
    }

    ///                     MEDIA PLAYER
    public static int stop(Player player, String name){
        if(!isAnimationExist(player, name)){
            return Command.SINGLE_SUCCESS;
        }
        getCompound(name).edit(name, e -> {
            e.set("isRunning", false);
            
        });
        Logger log = new Logger(player);
        log.info("<green>You stopped</green> <dark_green><name></dark_green>",
                Placeholder.unparsed("name", name)
        );
        if(runningTask.containsKey(name)){
            runningTask.remove(name).cancel();
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int update(Player player, String animationName){
        if(!isAnimationExist(player, animationName)){
            return Command.SINGLE_SUCCESS;
        }
        stop(player, animationName);
        play(player, animationName);
        return Command.SINGLE_SUCCESS;
    }
    public static int play(String animationName){
        return play(null, animationName);
    }
    public static int play(@Nullable Player player, String animationName){
        if(!isAnimationExist(player, animationName)){
            return Command.SINGLE_SUCCESS;
        }
        Configurator compound = getCompound(animationName);
        FileConfiguration config = compound.getConfig(animationName);
        Logger log = new Logger(player);

        boolean isRunning = config.getBoolean("isRunning");
        if(isRunning) {
            log.info(("<red>This animation is already running!</red>"));
            log.info(" <red>please use <click:suggest_command:'/animate stop <animation>'><hover:show_text:'<red>Click to stop this animation</red>'>/animate stop <animation></hover></click> to stop your animation!</red>",
                Placeholder.unparsed("animation", animationName));
            return Command.SINGLE_SUCCESS;
        }
        compound.edit(animationName, e -> {
            e.set("isRunning", true);
            
        });

        File[] files = compound.getParent().listFiles((dir, name) -> !name.endsWith(".yml"));
        if(files==null){
            log.info("This animation schema is empty!");
            return Command.SINGLE_SUCCESS;
        }
        Location pasteloc = config.getLocation("location", player.getLocation());
        if(pasteloc==null) plugin.getLogger().warning("[JasperProject] [Animation] Something wrong when loading "+animationName);

        boolean loop = config.getBoolean("loop");
        long tick = 20 / config.getInt("fps", 1);
        double radius = config.getDouble("radius", 20);
        List<String> schems = config.getStringList("schem");
        List<Runnable> schemPasterTasks = new ArrayList<>();
        Collection<Player> audiences = pasteloc.getWorld().getNearbyPlayers(pasteloc, radius);
        for (String schem : schems) {
            File schema = Arrays.stream(files).filter(f -> !f.getName().equals("region") && f.getName().contains(schem)).findFirst().orElse(null);
            if (schema==null) continue;
            Runnable task = () -> Structure.render(schema, pasteloc, audiences);
            schemPasterTasks.add(task);
        }
        class temp{
            Iterator<Runnable> iterator;
        }
        temp Paster = new temp();
        Paster.iterator = schemPasterTasks.iterator();
        log.info("<green>Now Playing</green> <dark_green><animation></dark_green> <green>at</green> <yellow><x>, <y>, <z>!</yellow>",
                Placeholder.unparsed("x", String.valueOf(pasteloc.getBlockX())),
                Placeholder.unparsed("y", String.valueOf(pasteloc.getBlockY())),
                Placeholder.unparsed("z", String.valueOf(pasteloc.getBlockZ())),
                Placeholder.unparsed("animation", animationName)
                );
        BukkitTask btask = new BukkitRunnable() {
            @Override
            public void run() {
                if(!Paster.iterator.hasNext()){
                    if(!loop) {
                        this.cancel();
                        runningTask.remove(animationName);
                        getCompound(animationName).edit(animationName, e -> {
                            e.set("isRunning", false);
                            
                        });
                        return;
                    }
                    Paster.iterator = schemPasterTasks.iterator();
                }
                Paster.iterator.next().run();
            }
        }.runTaskTimerAsynchronously(JasperProject.getPlugin(), 0, tick);
        runningTask.put(animationName, btask);
        return Command.SINGLE_SUCCESS;
    }


    ///                     EDIT TOOLS
    public static int createNew(Player player, String animation_name){
        boolean exist = (getCompound(animation_name)!=null) && (getCompound(animation_name).getConfig(animation_name)!=null);
        Logger log = new Logger(player);
        if(exist){
            log.info("This animation is already exist! please input another name");
            return Command.SINGLE_SUCCESS;
        }
        Configurator animation = JasperProject.getAnimationConfig()
                .newCompound(animation_name).create(animation_name);
        animation.edit(animation_name, e -> {
            e.set("owner", player.getName());
            e.set("isRunning", false);
            e.set("schem", new ArrayList<>());
            e.set("location", player.getLocation());
            
        });
        animationNameTabCompleter.put(player.getName(), List.of(animation_name));
        return Command.SINGLE_SUCCESS;
    }
    public static int addFrame(Player player, String animation_name, String frame_name){
        return addFrame(player, animation_name, frame_name, false);
    }
    public static int addFrame(Player player, String animation_name, String frame_name, boolean flag){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }

        Logger log = new Logger(player);
        File schem = new File(getCompound(animation_name).getParent(),frame_name+".schem");
        if(schem.exists()){
            if(!flag){
                log.info(ChatColor.LIGHT_PURPLE+"You just replaced "+animation_name+" to new version");
            }else {
                log.info(ChatColor.RED+"this frame is already exist! -flag to override it");
                return Command.SINGLE_SUCCESS;
            }
        }
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e -> {
            if(!e.contains("schem")) e.set("schem", new ArrayList<>());
            List<String> list = e.getStringList("schem");
            if(!list.contains(frame_name)) list.add(frame_name);
            e.set("schem", list);
            
        });
        Structure.saveFrame(player, getCompound(animation_name).getParent(), frame_name);
        log.info(frame_name +" saved as schem");

        return Command.SINGLE_SUCCESS;
    }

    public static int addMember(Player player, String animation_name, List<Player> members){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }

        Logger log = new Logger(player);
        Configurator config = getCompound(animation_name);

        config.edit(animation_name, e->{
            if(!e.contains("members")) e.set("members", new ArrayList<>());
            List<String> list = e.getStringList("members");
            for (Player member : members) {
                list.add(member.getName());
                member.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>You have been just added to </yellow> <dark_green><animation></dark_green> <yellow>animation group by <name></yellow>",
                        Placeholder.component("name", player.displayName()),
                        Placeholder.unparsed("animation", animation_name)
                        ));
                log.info("<color:#fdff8c>you added <color:#c2ff33><reset><name></reset><color:#fdff8c> to your <color:#06aa00><animation></color> as member!</color>",
                        Placeholder.component("name", member.displayName()),
                        Placeholder.unparsed("animation", animation_name));
            }
            e.set("members", list);
            
        });
        return Command.SINGLE_SUCCESS;
    }
    public static int addMember(Player player, String animation_name, String member){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Logger log = new Logger(player);
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e->{
            if(!e.contains("members")) e.set("members", new ArrayList<>());
            List<String> list = e.getStringList("members");
            list.add(member);
            e.set("members", list);
            
        });

        log.info("<color:#fdff8c>you added <color:#c2ff33><reset><name></reset><color:#fdff8c> to your <color:#06aa00><animation></color> as member!</color>",
                Placeholder.unparsed("name", member),
                Placeholder.unparsed("animation", animation_name));

        return Command.SINGLE_SUCCESS;
    }

    public static int loadFrame(Player player, String animation_name){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        final Configurator config = getCompound(animation_name);
        config.edit(animation_name, e -> {
            if(!e.contains("schem")) e.set("schem", new ArrayList<>());
            
        });

        Logger log = new Logger(player);
        if(!config.getConfig(animation_name).contains("schem")) {
            log.info(("<red>this animation has no frames!</red>"));
            return Command.SINGLE_SUCCESS;
        }
        final List<String> frames = config.getConfig(animation_name).getStringList("schem");
        log.info("<aqua>list frames of</aqua> <b><green>name :</green></b>",
                Placeholder.unparsed("name", animation_name));
        for (int i = 0; i < frames.size(); i++) {
            log.info(i+".) <b><frame> </b><red>[<click:run_command:'/animate edit <name> delete <frame>'><hover:show_text:'<dark_red>Click to delete this <frame></dark_red>'>delete</hover></click>]</red> <green><click:run_command:'/animate edit <name> load <frame>'>[<hover:show_text:'<dark_green>click to load this <frame></dark_green>'>load</hover>]</click></green>",
                    Placeholder.unparsed("frame", frames.get(i)),
                    Placeholder.unparsed("name", animation_name)
                    );
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int deleteFrame(Player player, String animation_name, String frame_name){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e ->{
            if(!e.contains("schem")) e.set("schem", new ArrayList<>());
            
        });
        Logger log = new Logger(player);
        if(!config.getConfig(animation_name).contains("schem")) {
            log.info(("<red>this animation has no frames!</red>"));
            return Command.SINGLE_SUCCESS;
        }
        File[] files = config.getParent().listFiles((dir, name) -> name.equals(frame_name+".schem"));
        for (File file : files) {
            file.delete();
        }
        log.info("<red>- deleted frame</red>",
                Placeholder.unparsed("frame", frame_name));
        config.edit(animation_name, e->{
            List<String> schem = e.getStringList("schem");
            schem.remove(frame_name);
            e.set("schem", schem);
            
        });

        return Command.SINGLE_SUCCESS;
    }

    public static int clearFrame(Player player, String animation_name){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e->{
            if(!e.contains("schem")) e.set("schem", new ArrayList<>());
            
        });
        Logger log = new Logger(player);
        if(!config.getConfig(animation_name).contains("schem")) {
            log.info(("<red>this animation has no frames!</red>"));
            return Command.SINGLE_SUCCESS;
        }
        File[] files = config.getParent().listFiles((dir, name) -> !name.endsWith(".yml"));
        for (File file1 : files) {
            if(file1.delete()){
                log.info("<red>- deleted frame</red>",
                        Placeholder.unparsed("frame", file1.getName()));
            }
        }
        config.edit(animation_name, e ->{
            e.set("schem", new ArrayList<>());
            
        });
        return Command.SINGLE_SUCCESS;
    }

    public static int setFPS(Player player, String animation_name, float v){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e ->{
            e.set("fps", v);
            
        });
        Logger log = new Logger(player);
        log.info(animation_name +" fps has been set to " +v);
        return Command.SINGLE_SUCCESS;
    }

    public static int setLocation(Player player, String animation_name, Location v){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }

        Logger log = new Logger(player);
        Configurator config = getCompound(animation_name);
        config.edit(animation_name, e->{
            e.set("location", v);
            
        });
        log.info("<dark_green><name></dark_green> <green>location has been set to</green> <yellow><x>, <y>, <z>!</yellow>",
                Placeholder.unparsed("name", animation_name),
                Placeholder.unparsed("x", String.valueOf(v.getBlockX())),
                Placeholder.unparsed("y", String.valueOf(v.getBlockY())),
                Placeholder.unparsed("z", String.valueOf(v.getBlockZ()))
                );
        return Command.SINGLE_SUCCESS;
    }
    public static int setRegion(Player player, String animation_name){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }
        Logger log = new Logger(player);
        File region = new File(getCompound(animation_name).getParent(), "\\region.schem");
        Structure.save(player, region);
        log.info("Saved "+animation_name+" region to "+ player.getLocation());

        return Command.SINGLE_SUCCESS;
    }
    public static int setLocation(Player player, String animation_name){
        return setLocation(player, animation_name, player.getLocation());
    }
    public static int setRadius(Player player, String animation_name, double radius){
        if(!isAnimationExist(player, animation_name)){
            return Command.SINGLE_SUCCESS;
        }

        Logger log = new Logger(player);
        getCompound(animation_name).edit(animation_name, e ->{
            e.set("radius", radius);
            
        });
        log.info("<dark_green><name></dark_green> <green>radius has been set to</green> <yellow><value></yellow>",
                Placeholder.unparsed("name", animation_name),
                Placeholder.unparsed("value", String.valueOf(radius))
                );
        return Command.SINGLE_SUCCESS;
    }
}
