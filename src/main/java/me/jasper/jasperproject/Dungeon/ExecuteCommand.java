package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExecuteCommand implements CommandExecutor, TabCompleter {
    JasperProject plugin;
    public ExecuteCommand(JasperProject plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)){
            return false;
        }

        DungeonGenerator room = new DungeonGenerator() {
            @Override
            public void initialize(DungeonHandler handler) {
                handler.addRoom(RoomType.SPECIAL, CreatedRoom.TRAP.clone());
                handler.addRoom(RoomType.SPECIAL, CreatedRoom.PUZZLE1.clone());
                handler.addRoom(RoomType.SINGLE, CreatedRoom.SINGLE.clone());
                handler.addRoom(RoomType.TWO_X_ONE, CreatedRoom.TWO.clone());
                handler.addRoom(RoomType.THREE_X_ONE, CreatedRoom.THREE.clone());
                handler.addRoom(RoomType.FOUR_X_ONE, CreatedRoom.FOUR.clone());
                handler.addRoom(RoomType.L_SHAPE, CreatedRoom.L.clone());
                handler.addRoom(RoomType.BOX, CreatedRoom.BOX.clone());
            }
        };
        if(strings.length == 0){
            room.generate();
        }if(strings.length == 2){
            room.generate();
        }
        if(strings.length == 3){
            room.generate();
        }
        MapView mapView = Bukkit.getMap(1);
        mapView.setCenterX(((room.getP() * 32)/2)-16);
        mapView.setCenterZ(((room.getL() * 32)/2)-16);
        mapView.getRenderers().clear();
        mapView.setTrackingPosition(false);
        mapView.setUnlimitedTracking(false);
        mapView.setLocked(true);
        ItemStack dungeonmap = new ItemStack(Material.FILLED_MAP);
        dungeonmap.editMeta(e->{
            ((MapMeta) e).setMapView(mapView);
        });
        player.getInventory().setItem(8, dungeonmap);
        room.getMap().setViewer(player);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length==3) return List.of("seed");
        if(strings.length==2) return List.of("lebar");
        return List.of("panjang");
    }
}
