package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import me.jasper.jasperproject.Dungeon.Floors.FloorONE;
import me.jasper.jasperproject.Dungeon.Loot.TIER_ONE_CHEST;
import me.jasper.jasperproject.Dungeon.Shapes.*;
import me.jasper.jasperproject.Dungeon.Shapes.Shape;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import me.jasper.jasperproject.Util.FileConfiguration.Configurator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalStructure;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R3.map.RenderData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Named;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DebugCommand implements CommandExecutor, TabCompleter {
    Location location;
    private Shape four = new FOUR_BY_FOUR();
    private int anchor = 0;
    private Room room = CreatedRoom.FOUR.clone();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        int rot = four.getRotation().getOrDefault(anchor, 0);
        if (strings.length==0){
            Location location = player.getLocation();
            byte[][][] fourByFour = four.getShape();
            for (byte[] point : fourByFour[anchor]) {
                Location add = location.clone().add(point[0], 0, point[1]);
                Block block = add.getBlock();
                block.setType(Material.GREEN_WOOL);
                player.sendMessage(Arrays.toString(point));
            }
            player.sendMessage(anchor+" Pasted with rotation of "+ rot);
            return true;
        }
        switch (strings[0]){
            case "rotate" -> {
                int anchor = Integer.parseInt(strings[1]);
                four.rotate(anchor);
                Integer i = four.getRotation().get(anchor);
                player.sendMessage("Rotated to "+ i);
            }
            case "anchor" -> this.anchor = Integer.parseInt(strings[1]);
            case "setLocation" -> this.location = player.getLocation();
            case "setRoom" -> player.sendMessage(room.getName());
            case "paste" -> {
                Room clone = room.clone();
                Point pastePoint = four.getPastePoint(anchor);
                clone.setLoc(new Point(1, 1));
                clone.setLocTranslate(pastePoint);
                clone.setRotation(rot);
                clone.loadScheme(true);
            }
            case "setType" -> {
                switch (strings[1]){
                    case "FOUR" -> {
                        this.four = new FOUR_BY_FOUR();
                        this.room = CreatedRoom.FOUR.clone();
                    }
                    case "THREE" -> {
                        this.four = new THREE_BY_THREE();
                        this.room = CreatedRoom.THREE.clone();
                    }
                    case "TWO" -> {
                        this.four = new TOW_BY_TWO();
                        this.room = CreatedRoom.TWO.clone();
                    }
                    case "ONE" -> {
                        this.four = new ONE_BY_ONE();
                        this.room = CreatedRoom.SINGLE.clone();
                    }
                    case "BOX" -> {
                        this.four = new BOX_BY_BOX();
                        this.room = CreatedRoom.BOX.clone();
                    }
                    case "L" -> {
                        this.four = new L_BY_L();
                        this.room = CreatedRoom.L.clone();
                    }
                }
                player.sendMessage(strings[1]+" -> "+this.room.getName());
            }
            case "map" -> {
                ItemStack item = new ItemStack(Material.FILLED_MAP);
                item.editMeta(e->{
                    ((MapMeta) e).setMapView(Bukkit.getMap(1));
                });
                player.getInventory().setItemInMainHand(item);
                RenderData data = new RenderData();
                for (byte i = 0; i < 127; i++) {
                    for (byte j = 0; j < 127; j++) {
                        data.buffer[i*127+j] = (byte) ((i+j)%127);
                    }
                }
                ClientboundMapItemDataPacket packet = new ClientboundMapItemDataPacket(
                        new MapId(1),
                        MapView.Scale.NORMAL.getValue(),
                        false,
                        null,
                        new MapItemSavedData.MapPatch(0, 0, 128, 128, data.buffer)
                );
                ((CraftPlayer) player).getHandle().connection.send(packet);
            }
            case "start" -> {
                if(strings[1].equals("FLOOR_ONE")){
                    FloorONE floorONE = new FloorONE(JPlayer.getJPlayer(player).createGroup());
                    floorONE.enter();

                    //if ready
                    floorONE.start();
                }
            }
            case "leave" -> {
                Dungeon dungeon = Dungeon.instance.get(JPlayer.getJPlayer(player).getLastInstance());
                dungeon.exit(player);
            }
            case "setup" -> {
                Configurator config = JasperProject.getDungeonConfig();
                if(strings[1].equals("save")){
                    if(strings.length<4) return false;
                    String type = strings[2];
                    String name = strings[3];
                    File roomsDirectory = new File(config.getParent(), "//rooms//" + type);
                    roomsDirectory.mkdirs();
                    Region vector = Animator.getRegions().get(player.getUniqueId());
                    BlockVector3 max = vector.getMaximumPoint();
                    BlockVector3 min = vector.getMinimumPoint();
                    Location location = new Location(player.getWorld(), max.x()-((max.x()-min.x())/2)+1, 70, max.z()-((max.z()-min.z())/2)+1);
                    File saveTo = new File(roomsDirectory, "//"+ name+".schem");
                    if (!saveTo.exists()) Structure.save(player, location, saveTo);
                    config.create("rooms-map");
                    config.edit("rooms-map", (e)->{
                        if(!e.contains(type)) e.set(type, new ArrayList<>());
                        List<String> list = e.getStringList(type);
                        if(!list.contains(name)) list.add(name);
                        e.set(type, list);
                    });
                    player.sendMessage(Component.text("Saved "+name+"!").color(NamedTextColor.GREEN));
                }
                if(strings[1].equals("load")){
                    String type = strings[2];
                    String name = strings[3]+".schem";
                    File roomsDirectory = new File(config.getParent(), "//rooms//" + type);
                    Structure.render(new File(roomsDirectory, "//"+name), player.getLocation(), bs -> {
                        if (bs instanceof Chest chest) {
                            chest.getPersistentDataContainer().set(TIER_ONE_CHEST.INSTANCE.key, PersistentDataType.BOOLEAN, true);
                            Bukkit.broadcastMessage("FOUND A CHEST");
                            chest.update();
                        }
                    });
                    player.sendMessage(Component.text("Loaded "+name).color(NamedTextColor.GREEN));
                }
                if(strings[1].equals("delete")){
                    String type = strings[2];
                    String name = strings[3];
                    File roomsDirectory = new File(config.getParent(), "//rooms//" + type);
                    File file = new File(roomsDirectory, "//" + name+".schem");
                    if(file.delete()){
                        config.edit("rooms-map", e->{
                            if(!e.contains(type)) e.set(type, new ArrayList<>());
                            List<String> list = e.getStringList(type);
                            list.remove(name);
                            e.set(type, list);
                        });
                        player.sendMessage(Component.text("You deleted "+name+"!").color(NamedTextColor.GREEN));
                        return true;
                    }
                }
                if(strings[1].equals("create_group")){
                    String name = strings[2];
                    Configurator created_compound = config.newCompound(name);
                    created_compound.newConfig(name);
                    player.sendMessage(Component.text("Created "+name+"!").color(NamedTextColor.GREEN));
                }
                if(strings[1].equals("edit_group")){
                    String name = strings[2];
                    if(strings[3].equals("add_room")) {
                        String schem = strings[4];
                        Configurator compound = config.getCompound(name);
                        if(compound==null){
                            player.sendMessage(Component.text("Can't find "+name+" group!").color(NamedTextColor.RED));
                            return false;
                        }
                        FileConfiguration config1 = compound.getConfig(name);
                        String type = null;
                        for (RoomType value : RoomType.values()) {
                            List<String> list = config.getConfig("rooms-map").getStringList(value.name());
                            if (list.contains(schem)) {
                                type = value.name();
                            }
                        }
                        if (type == null) {
                            player.sendMessage(Component.text("Can't find a schema named " + schem + ". /dungeon setup save <arg> to create a new rooms!").color(NamedTextColor.RED));
                            return false;
                        }
                        if (config1 == null) {
                            player.sendMessage(Component.text("Can't find " + name + " group").color(NamedTextColor.RED));
                            return false;
                        }
                        if (!config1.contains(type)) config1.set(type, new ArrayList<>());
                        String finalType = type;
                        compound.edit(name, e -> {
                            List<String> list = config1.getStringList(finalType);
                            list.add(schem);
                            config1.set(finalType, list);
                        });
                        player.sendMessage(Component.text("Added "+schem+" to "+name+"!").color(NamedTextColor.GREEN));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 2 && strings[0].equals("setType")){
            return List.of("FOUR", "THREE", "TWO", "ONE", "L", "BOX");
        }
        if(strings.length==2&&strings[0].equals("start")){
            List<String> complete = new ArrayList<>();
            for (File file : Arrays.stream(Objects.requireNonNull(JasperProject.getDungeonConfig().getParent().listFiles((f, n) -> f.isDirectory() && !n.endsWith(".yml")))).toList()) {
                complete.add(file.getName());
            }
            return complete.stream().filter(name -> name.startsWith(strings[1])).toList();
        }
        if(strings.length==5 && strings[3].equals("add_room")){
            List<String> complete = new ArrayList<>();
            Configurator config = JasperProject.getDungeonConfig();
            FileConfiguration config_ = config.getConfig("rooms-map");
            for (RoomType value : RoomType.values()) {
                if(!config_.contains(value.name())) config_.set(value.name(), new ArrayList<>());
                List<String> list = config_.getStringList(value.name());
                complete.addAll(list);
            }
            return complete.stream().filter(type -> type.startsWith(strings[4])).toList();
        }
        if(strings.length==4 && strings[1].equals("edit_group")){
            return List.of("add_room");
        }
        if(strings.length==3 && strings[1].equals("edit_group")){
            Configurator config = JasperProject.getDungeonConfig();
            List<String> complete = new ArrayList<>();
            for (File file : Objects.requireNonNull(config.getParent().listFiles((f, n) -> f.isDirectory() && !n.equals("rooms")))) {
                complete.add(file.getName());
            }
            return complete;
        }
        if(strings.length==4 && strings[1].equals("delete")){
            List<String> complete = new ArrayList<>();
            FileConfiguration config = JasperProject.getDungeonConfig().getConfig("rooms-map");
            if(config==null) return complete;
            List<String> list = config.getStringList(strings[2]);
            complete.addAll(list);
            return complete.stream().filter(name -> name.startsWith(strings[3])).toList();
        }
        if(strings.length==3 && strings[1].equals("create_group")){
            return List.of("<group-name ex: FLOOR_1>");
        }
        if(strings.length==2&&strings[0].equals("setup")){
            return List.of("save", "load", "delete", "list", "create_group", "edit_group").stream().filter(name -> name.startsWith(strings[1])).toList();
        }
        if(strings.length==4 && (strings[0].equals("setup"))){
            List<String> complete = new ArrayList<>();
            Configurator config = JasperProject.getDungeonConfig();
            FileConfiguration config_ = config.getConfig("rooms-map");
            for (RoomType value : RoomType.values()) {
                if(!config_.contains(value.name())) config_.set(value.name(), new ArrayList<>());
                List<String> list = config_.getStringList(value.name());
                complete.addAll(list);
            }
            return complete.stream().filter(type -> type.startsWith(strings[3])).toList();
        }
        if(strings.length==3&&strings[0].equals("setup")){
            return RoomType.autocomplete.stream().filter(type -> type.startsWith(strings[2])).toList();
        }
        return List.of("rotate", "anchor", "setLocation", "setRoom", "paste", "setType", "map", "start", "leave","setup").stream().filter(name -> name.startsWith(strings[0])).toList();
    }
}
