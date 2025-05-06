package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import me.jasper.jasperproject.Dungeon.Map.CursorRenderer;
import me.jasper.jasperproject.Dungeon.Map.DungeonMapRenderer;
import me.jasper.jasperproject.JasperProject;
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



        DungeonMapRenderer renderer = new DungeonMapRenderer(room);
        MapView mapView = Bukkit.createMap(player.getWorld());
        mapView.getRenderers().clear();
        mapView.setScale(MapView.Scale.NORMAL);
        mapView.setCenterX(((room.p * 32)/2)-16);
        mapView.setCenterZ(((room.l * 32)/2)-16);

        mapView.addRenderer(renderer);
        mapView.addRenderer(new CursorRenderer(renderer));
        mapView.setTrackingPosition(false);
        mapView.setUnlimitedTracking(false);
        mapView.setLocked(true);

        player.sendMap(mapView);


        ItemStack dungeonmap = new ItemStack(Material.FILLED_MAP);
        MapMeta mapmeta = (MapMeta) dungeonmap.getItemMeta();
        mapmeta.setMapId(0);
        mapmeta.setMapView(mapView);
        dungeonmap.setItemMeta(mapmeta);

        player.getInventory().addItem(dungeonmap);

        long endTime = System.nanoTime();
        String time2 = String.format("%.2f", (endTime - startTime) / 1_000_000.0);

        Bukkit.broadcastMessage(ChatColor.GOLD+"Dungeon map generated in "+time2);

        return false;
    }

//    public void sendDungeonMap(Player player){
//        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
//        PacketContainer packet = manager.createPacket(PacketType.Play.Server.MAP);
//
//        Wrapper
//
//        packet.getIntegers().write(0,0);
//
//        byte[] canvas = packet.getByteArrays().read(0);
//    }
}
