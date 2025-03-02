package me.jasper.jasperproject.Dungeon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class DungeonMapRenderer extends MapRenderer {
    Generator g;

    final int GAP = 1;
    final int MARGIN = 5;
    final int MAX_RECUR_TRIES = 1000;


    int GRID_PANJANG ;
    int GRID_LEBAR;
    int CELL_SIZE;
    int PRE_SIZE;
    ArrayList<Room> rooms;
    DungeonMapRenderer(Generator generator){
        this.g = generator;
        GRID_PANJANG = generator.p;
        GRID_LEBAR = generator.l;
        PRE_SIZE = (int) (128/Math.sqrt(GRID_PANJANG*GRID_LEBAR)-(double) GAP);
        CELL_SIZE = (int) (PRE_SIZE-(double) MARGIN*PRE_SIZE / (128-MARGIN));
        rooms = getRooms(generator);
    }

    boolean rendered = false;

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        if(rendered) return;
        Bukkit.broadcastMessage("Image has been drawn");

        for (Room room : rooms) {
            if(room==null) continue;
            if(room.type == RoomType.L_SHAPE){
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"FOUND MINMAX of "+room.name+": has weight body of "+room.body.size());
                List<Point> L = sort(room.body);

                Color color = room.type.color;

                drawRoom(mapCanvas,
                        Math.min(L.get(0).x, L.get(2).x), Math.min(L.get(0).y, L.get(2).y),
                        CELL_SIZE, color,
                        Math.max(L.get(0).x, L.get(2).x), Math.max(L.get(0).y, L.get(2).y),true);

                drawRoom(mapCanvas,
                        Math.min(L.get(1).x, L.get(2).x), Math.min(L.get(1).y, L.get(2).y),
                        CELL_SIZE, color,
                        Math.max(L.get(1).x, L.get(2).x), Math.max(L.get(1).y, L.get(2).y),true);
                continue;
            }

            int minX = Integer.MAX_VALUE,minY = Integer.MAX_VALUE,
                    maxX = Integer.MIN_VALUE,maxY = Integer.MIN_VALUE;
            for (Point point : room.body){
                minX = Math.min(point.x, minX);
                minY = Math.min(point.y, minY);
                maxX = Math.max(point.x, maxX);
                maxY = Math.max(point.y, maxY);
            }

            Color color = room.type.color;
            drawRoom(mapCanvas, minX, minY, CELL_SIZE, color,
                        maxX, maxY, false);
        }
        rendered = true;
    }
    
    void drawRoom(MapCanvas canvas, int startx, int starty, int cellSize, Color color, int endx, int endy, boolean debug){
        int startX = (startx*CELL_SIZE)+GAP+MARGIN;
        int startY = (starty*CELL_SIZE)+GAP+MARGIN;

        int endX = (endx*(cellSize+GAP)) + cellSize;
        int endY = (endy*(cellSize+GAP)) + cellSize;

       if (debug)Bukkit.broadcastMessage(ChatColor.GRAY+"    Drew from "+startX+", "+startY+" to "+endX+", "+endY);
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                canvas.setPixelColor(x,y,color);
            }
        }
    }
    
    ArrayList<Room> getRooms(Generator generator){
        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < generator.grid.length; i++) {
            for (int j = 0; j < generator.grid[0].length; j++) {
                if(!rooms.contains(generator.grid[i][j])){
                    rooms.add(generator.grid[i][j]);
                }
            }
        }
        return rooms;
    }

    List<Point> sort(List<Point> body){
        int[][] dirs = {{1,0},{0,1},{-1,0},{0,-1},};
        List<Point> sorted = new ArrayList<>();
        for(Point point : body){
            int counter = 0;
            for(int[] dir : dirs){
                if(body.contains(new Point(point.x-dir[0], point.y-dir[1]))){
                    counter++;
                }
            }
            if(counter>=2){
                Bukkit.broadcastMessage("MID FOUND");
                sorted.addLast(point);
                continue;
            }
            sorted.addFirst(point);
        }
        return sorted;
    }
}
