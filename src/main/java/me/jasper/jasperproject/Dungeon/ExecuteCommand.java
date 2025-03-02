package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public class ExecuteCommand extends DungeonUtil implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)){
            return false;
        }


        this.loadAndPasteSchematic("clear",new BlockVector3(48,70,48),0, false);
        Generator room = new Generator();
        if(strings.length == 0){
            room.generate();
        }if(strings.length == 1){
            room.setSeed(Long.parseLong(strings[0]));
            room.generate();
        }
        if(strings.length == 2){
            room.setL(Integer.parseInt(strings[0]));
            room.setP(Integer.parseInt(strings[1]));
            room.generate();
        }
        if(strings.length == 3){
            room.setL(Integer.parseInt(strings[0]));
            room.setP(Integer.parseInt(strings[1]));
            room.setSeed(Long.parseLong(strings[2]));
            room.generate();
        }
        long startTime = System.nanoTime();

        MapView mapView = Bukkit.createMap(player.getWorld());
        mapView.getRenderers().clear();
        mapView.addRenderer(new DungeonMapRenderer(room));
        mapView.setScale(MapView.Scale.NORMAL);
        mapView.setTrackingPosition(false);
        mapView.setUnlimitedTracking(false);

        mapView.setLocked(true);
        mapView.setCenterX(0);
        mapView.setCenterZ(0);

        ItemStack dungeonmap = new ItemStack(Material.FILLED_MAP);
        MapMeta mapmeta = (MapMeta) dungeonmap.getItemMeta();
        mapmeta.setMapView(mapView);
        dungeonmap.setItemMeta(mapmeta);

        player.getInventory().addItem(dungeonmap);

        long endTime = System.nanoTime();
        String time2 = String.format("%.2f", (endTime - startTime) / 1_000_000.0);

        Bukkit.broadcastMessage(ChatColor.GOLD+"Dungeon map generated in "+time2);

        return false;
    }
}
