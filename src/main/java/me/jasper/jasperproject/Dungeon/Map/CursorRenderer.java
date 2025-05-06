package me.jasper.jasperproject.Dungeon.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CursorRenderer extends MapRenderer {
    MapCursorCollection mapCursor = new MapCursorCollection();
    List<Player> players = new ArrayList<>();
    DungeonMapRenderer mainMap;
    public CursorRenderer(DungeonMapRenderer mainMap){
        this.mainMap = mainMap;
    }



    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        update(mapView,player);
        mapCanvas.setCursors(mapCursor);
    }
    void update(MapView mapView,Player p){
        mapCursor = new MapCursorCollection();
        players = p.getWorld().getPlayers();

        for(Player player : players){
            Location loc = player.getLocation();

            byte x = (byte) (((loc.getX() - mapView.getCenterX()) * ((mainMap.getFINAL_CELL_SIZE() * 1.75)) / mainMap.getFINAL_CELL_SIZE()));
            byte y = (byte) (((loc.getZ() - mapView.getCenterZ()) * ((mainMap.getFINAL_CELL_SIZE() * 1.75)) / mainMap.getFINAL_CELL_SIZE()));

//        player.sendMessage(ChatColor.GRAY+"your location is : "+loc.getBlockX()+", "+loc.getBlockZ());
//        player.sendMessage(ChatColor.GRAY+" you in map : "+x+", "+y);

            mapCursor.addCursor(new MapCursor(x, y,
                    (byte) (loc.getYaw() < 0 ? 16 + (loc.getYaw() / 22.5) : loc.getYaw() / 22),
                    MapCursor.Type.PLAYER, true));
        }
    }




}
