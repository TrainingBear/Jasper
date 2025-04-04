package me.jasper.jasperproject.Animation;


import com.sk89q.worldedit.regions.Region;
import me.jasper.jasperproject.FileConfiguration.Configurator;
import me.jasper.jasperproject.FileConfiguration.JasperConfiguratorException;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.Items;
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AnimationCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
//        if(!(commandSender instanceof Player player)) return false;
//        if(strings.length==0){
//            player.sendMessage(ChatColor.RED+"Arguments required");
//        }
//        switch (strings[0]) {
//            case "wand":
//                Items.sendItems(player, Items.animate_wannd);
//                return true;
//            case "create":
//                if (strings[1].isEmpty()) {
//                    player.sendMessage(ChatColor.RED + "Please input your animation name!");
//                    return false;
//                }
//
//                File file = new File(JasperProject.getPlugin().getDataFolder(), "\\Animations\\" + strings[1]);
//                file.mkdirs();
//                File created_file;
//
//                try {
//                    created_file = Configurator.create(file, strings[1]);
//                } catch (JasperConfiguratorException e) {
//                    FileConfiguration configuration = Configurator.getConfig(strings[1]);
//                    player.sendMessage(ChatColor.RED+"Please choose another name, this already clamed by "+
//                            configuration.get("owner"));
//                    return false;
//                }
//
//                FileConfiguration new_config = Configurator.getConfig(strings[1]);
//                if (new_config == null) {
//                    player.sendMessage(ChatColor.RED + "This file isn't exist! please try again later!");
//                    return false;
//                }
//
//                new_config.set("owner", player.getName());
//                try {
//                    new_config.save(created_file);
//                } catch (IOException e) {
//                    player.sendMessage(ChatColor.RED+"Something wrong when saving configs. please again later!");
//                    Configurator.delete(strings[1]);
//                    return false;
//                }
////                loadTabCompleter();
//                return true;
//            case "edit": //animate 0edit 1"name" 2add_frame 3"name"
//                if(strings[2].equals("add_frame")){
//                    if(strings[3].isEmpty()) return false;
//                    Region region = Animator.getRegions().get(player.getUniqueId());
//                    File schem = new File(Configurator.getFile(strings[1]).getParentFile(),"\\"+strings[3]+".schem");
//                    if(schem.exists()){
//                        player.sendMessage(ChatColor.RED+"this frame is already exist!");
//                        return false;
//                    }
//                    FileConfiguration config = Configurator.getConfig(strings[1]);
//                    if(!config.contains("schem")) config.set("schem", new ArrayList<>());
//                    List<String> list = config.getStringList("schem");
//                    list.add(strings[3]);
//                    config.set("schem", list);
//
//                    Configurator.save(config, strings[1]);
//                    Animation.saveSchematic(player.getLocation(), region, schem);
//                    player.sendMessage(strings[3] +" saved as schem");
//                }
//                if(strings[2].equals("load")){
//                    if(strings[3].isEmpty()) return false;
//
//                    File schem = new File(Configurator.getFile(strings[1]).getParent()+"\\"+strings[3]+".schem");
//                    try {
//                        Animation.pasteSchematic(player, player.getLocation(), schem);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                if(strings[2].equals("setLocation")){
//                    FileConfiguration config = Configurator.getConfig(strings[1]);
//                    config.set("origin",player.getLocation());
//                    Configurator.save(config, strings[1]);
//                    player.sendMessage("saved!");
//                }
//                if(strings[2].equals("clear")){
//                    File[] files = Configurator.getFile(strings[1]).getParentFile().listFiles((dir, name) -> !name.endsWith(".yml"));
//                    if(files==null){
//                        player.sendMessage(ChatColor.RED+"This animation has no schema!");
//                        return false;
//                    }
//                    for (File file1 : files) {
//                        if(file1.delete()){
//                            player.sendMessage(file1.getName()+" deleted");
//                        }
//                    }
//                    FileConfiguration config = Configurator.getConfig(strings[1]);
//                    config.set("schem", null);
//                    Configurator.save(config, strings[1]);
//                }
//                if(strings[2].equals("FPS")) {
//                    long tick = 10;
//                    float fps = 0;
//                    try {
//                        fps = Float.parseFloat(strings[3]);
//                        if(fps > 20){
//                            player.sendMessage(ChatColor.RED + "FPS cant be more than 20!");
//                            return false;
//                        }
//                        tick = (long) (20/fps);
//                    } catch (NumberFormatException e) {
//                        player.sendMessage(ChatColor.RED + "Please input a number!");
//                        return false;
//                    } finally {
//                        FileConfiguration config = Configurator.getConfig(strings[1]);
//                        config.set("fps", tick);
//                        Configurator.save(config, strings[1]);
//                    }
//                }
//                break;
//            case "play" :
//                if(strings[1].isEmpty()) return false;
//                return Animation.play(player , strings[1]);
//            case "stop":
//                if(strings[1].isEmpty()) return false;
//                Animation.stop(player, strings[1]);
//                break;
//            case "update":
//                if(strings[1].isEmpty()) return false;
//                Animation.stop(player, strings[1]);
//                return Animation.play(player , strings[1]);
//            case "list":
//                player.sendMessage(ChatColor.DARK_GRAY+"List of currently running animations:");
//                for (String string : Animation.getRunningTask().keySet()) {
//                    Component component = getComponent(string);
//
//                }
//                break;
//        }
        return false;
    }

    private @NotNull Component getComponent(String string) {
        MiniMessage parser = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.clickEvent())
                        .resolver(StandardTags.hoverEvent())
                        .resolver(StandardTags.reset())

                        .build()
                )
                .build();
        Component parsed = parser.deserialize(
                "<click:suggest_command:'/animate edit +name '><hover:show_text:'<green>Click to edit!</green>'><dark_green> [EDIT]</dark_green></hover></click><click:run_command:'/animate stop +name'><hover:show_text:'<red>Click to stop this animation!</red>'><red> [STOP] </red></hover></click><click:run_command:'/animate delete +name'><hover:show_text:'<dark_red>Click to delete your animation</dark_red>'><b><i><dark_red>[DELETE]</dark_red></i> </b></hover></click><dark_gray><gold><b>  +name</b></gold></dark_gray> "
        );
        return parsed;
    }

    private static final Map<String, List<String>> TabCompleter = new HashMap<>();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return null;

        if(strings[0].equals("create")){
            return List.of(
                    "[your animation name]"
            );
        }

        if(strings[0].equals("edit") && !strings[1].isEmpty() && !strings[2].isEmpty()){
            return switch (strings[2]){
                case "add_frame" -> List.of("[Your frame name]");
                case "clear" -> List.of("clear your frames");
                case "FPS" -> List.of("[set your animation fps (based on ticks) must >=10]");
                case "setLocation" -> List.of("set your animation location");
                default -> List.of("Unknown argument..");
            };
        }

        if(strings[0].equals("edit") && !strings[1].isEmpty()){
            return List.of(
                    "add_frame",
                    "clear",
                    "FPS",
                    "load",
                    "setLocation"
            );
        }

        if(strings[0].equals("edit") || strings[0].equals("play") || strings[0].equals("update") || strings[0].equals("stop")){
            return TabCompleter.getOrDefault(player.getName(), Collections.emptyList());
        }


        return List.of(
                "wand",
                "create",
                "edit",
                "play",
                "stop",
                "update",
                "list"
        );
    }

    public static void loadListFileTabCompleter(){
        List<String> list = new ArrayList<>();
        File file = new File(JasperProject.getPlugin().getDataFolder()+"\\Animations");
        File[] files = file.listFiles();
        if(files==null) return;

//        Bukkit.getLogger().info(sender+" invoking GetAnimationConfig(TabCompleter in AnimationCommand.java)");
        for (File file1 : files) {
            File[] configs = file1.listFiles((dir, name) -> name.endsWith(".yml"));
            if(configs==null) continue;
            for (File config : configs) {
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(config);
                String owner = configuration.getString("owner");
                list.add(Configurator.getFileName(config));
                TabCompleter.computeIfAbsent(owner, k -> new ArrayList<>()).
                        add(Configurator.getFileName(config));
            }
        }
    }

}
