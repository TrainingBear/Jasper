package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import me.jasper.jasperproject.Dungeon.Floors.FloorONE;
import me.jasper.jasperproject.Dungeon.Shapes.*;
import me.jasper.jasperproject.Dungeon.Shapes.Shape;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import me.jasper.jasperproject.Util.FileConfiguration.Configurator;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalStructure;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                    Region vector = Animator.getRegions().get(player.getUniqueId());
                    BlockVector3 block = vector.getMaximumPoint();
                    Location location = new Location(player.getWorld(), block.x(), 70, block.z());
                    Structure.save(player, location, new File(config.getParent(), "//rooms//"+name+"//.schem"));
                    config.create("rooms-map");
                    config.edit("rooms-map", (e)->{
                        if(!e.contains(type)) e.set(type, new ArrayList<>());
                        List<String> list = e.getStringList(type);
                        list.add(name+".schem");
                    });
                }
                if(strings[1].equals("load")){
                    String name = strings[2];
                    Structure.render(new File(config.getParent(), "//rooms//"+name+".schem"), player.getLocation());
                }
                if(strings[1].equals("delete")){
                    String name = strings[2];
                    File file = new File(config.getParent(), "//rooms//" + name + ".schem");
                    return file.delete();
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
            return List.of("FLOOR_ONE", "FLOOR_TWO__SOON");
        }
        if(strings.length==2&&strings[0].equals("setup")){
            return List.of("save", "load", "delete", "list");
        }
        if(strings.length==3 && (strings[1].equals("load") || strings[1].equals("delete"))){
            List<String> complete = new ArrayList<>();
            Configurator config = JasperProject.getDungeonConfig();
            FileConfiguration config_ = config.getConfig("rooms-map");
            for (RoomType value : RoomType.values()) {
                if(!config_.contains(value.name())) config_.set(value.name(), new ArrayList<>());
                List<String> list = config_.getStringList(value.name());
                complete.addAll(list);
            }
        }
        if(strings.length==3&&strings[0].equals("setup")){
            return RoomType.autocomplete;
        }
        return List.of("rotate", "anchor", "setLocation", "setRoom", "paste", "setType", "map", "start", "leave","setup");
    }
}
