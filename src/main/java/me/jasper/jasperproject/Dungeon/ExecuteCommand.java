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

public class ExecuteCommand extends DungeonUtil implements CommandExecutor, TabCompleter {
    JasperProject plugin;
    public ExecuteCommand(JasperProject plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)){
            return false;
        }

        this.loadAndPasteSchematic("clear",BlockVector3.at(48,70,48),0, false);
        Generator room = new Generator();
        if(strings.length == 0){
            room = new Generator();
            room.generate();
        }if(strings.length == 2){
            room = new Generator(Integer.parseInt(strings[1]), Integer.parseInt(strings[0]));
            room.generate();
        }
        if(strings.length == 3){
            room = new Generator(Integer.parseInt(strings[1]), Integer.parseInt(strings[0]),Long.parseLong(strings[2]));
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
