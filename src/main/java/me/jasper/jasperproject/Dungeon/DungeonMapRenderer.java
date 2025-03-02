package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class DungeonMapRenderer extends MapRenderer {
    Generator g;
    final int GAP = 5;
    final int MARGIN = 5;
    final int MAX_RECUR_TRIES = 1000;
    int GRID_PANJANG ;
    int GRID_LEBAR;
    int CELL_SIZE;
    int PRE_SIZE;
    ArrayList<Room> rooms;
    int[] DOOR_SIZE;


    DungeonMapRenderer(Generator generator){
        this.g = generator;
        GRID_PANJANG = generator.p;
        GRID_LEBAR = generator.l;
        PRE_SIZE = (int) (128/Math.sqrt(GRID_PANJANG*GRID_LEBAR));
        CELL_SIZE = (int) (PRE_SIZE-(double) MARGIN*PRE_SIZE / (48-MARGIN));
        rooms = getRooms(generator);
        DOOR_SIZE = new int[]{-PRE_SIZE/4, -6};
    }

    boolean rendered = false;

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        if(rendered) return;
        Bukkit.broadcastMessage("Image has been drawn");

        buildDoor(mapCanvas, g.parrentMap,
                new Point(g.x3, g.y3), true);
        for (Point endpoint : g.endpoint){
            buildDoor(mapCanvas, g.parentMap2,
                    endpoint, false);
        }


        for (Room room : rooms) {
            if(room==null) continue;
            if(room.type == RoomType.L_SHAPE){
                List<Point> L = sort(room.body);

                Color color = room.type.color;

                drawRoom(mapCanvas,
                        Math.min(L.get(0).x, L.get(2).x), Math.min(L.get(0).y, L.get(2).y),
                        CELL_SIZE, color,
                        Math.max(L.get(0).x, L.get(2).x), Math.max(L.get(0).y, L.get(2).y),false);

                drawRoom(mapCanvas,
                        Math.min(L.get(1).x, L.get(2).x), Math.min(L.get(1).y, L.get(2).y),
                        CELL_SIZE, color,
                        Math.max(L.get(1).x, L.get(2).x), Math.max(L.get(1).y, L.get(2).y),false);
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
        int startX = startx*CELL_SIZE+MARGIN+GAP;
        int startY = starty*CELL_SIZE+MARGIN+GAP;

        int endX = (endx*(cellSize)) + CELL_SIZE+MARGIN;
        int endY = (endy*(cellSize)) + CELL_SIZE+MARGIN;

        if (debug) Bukkit.broadcastMessage(ChatColor.GRAY+"    Drew from "+startX+", "+startY+" to "+endX+", "+endY);
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                canvas.setPixelColor(x,y,color);
            }
        }
    }

    void buildDoor(MapCanvas canvas, Map<Point, Point> parentMapOri, Point end, boolean locked) {
        Point step = end;
        Room d1, d2;
        Point pre_step, transition;
        boolean rotation;
        Map<Point, Point> parentMap = new HashMap<>(Map.copyOf(parentMapOri));

        while (!parentMap.isEmpty()) {
            pre_step = step;
            d1 = g.grid[pre_step.x][pre_step.y];

            step = parentMap.remove(step);
            if(step == null){
                return;
            }
            d2 = g.grid[step.x][step.y];

            transition = new Point(-(pre_step.x - step.x)*CELL_SIZE/2, -(pre_step.y - step.y)*CELL_SIZE/2);
            rotation = transition.x != 0;

            if(!Objects.equals(d1, d2)){
                drawDoor(canvas,
                        pre_step.x*(CELL_SIZE)+(CELL_SIZE/2)+transition.x,
                        pre_step.y*(CELL_SIZE)+(CELL_SIZE/2)+transition.y,
                        locked? Color.GRAY : g.grid[pre_step.x][pre_step.y].type.color, rotation);
            }
        }
    }
//    void buildDoor(Map<Point, Point> parentMap, Point start, Point end, MapCanvas canvas, boolean locked) {
//        Point step = end;
//        Room d1, d2;
//        Point pre_step, transition;
//        boolean rotation;
//        Bukkit.broadcastMessage(ChatColor.RED+"Building Locked Door..");
//
//        while (!step.equals(start)) {
//            pre_step = step;
//            d1 = g.grid[pre_step.x][pre_step.y];
//
//            step = parentMap.get(step);
//            d2 = g.grid[step.x][step.y];
//
//            transition = new Point(-(pre_step.x - step.x)*CELL_SIZE/2, -(pre_step.y - step.y)*CELL_SIZE/2);
//            rotation = transition.x != 0;
//
//            if(!Objects.equals(d1, d2)){
//                drawDoor(canvas,
//                        pre_step.x*(CELL_SIZE)+(CELL_SIZE/2)+transition.x,
//                        pre_step.y*(CELL_SIZE)+(CELL_SIZE/2)+transition.y,
//                        locked? Color.GRAY : g.grid[pre_step.x][pre_step.y].type.color, rotation);
//
//            }
//        }
//    }

    void drawDoor(MapCanvas canvas, int startX, int startY, Color color, boolean rot){
        if(rot) rotate();

        int start = rot? startX+MARGIN+GAP : (int) (startX + MARGIN + GAP + Math.floor((double) (MARGIN * PRE_SIZE) / (128 - MARGIN)));
        int start2 = rot? (int) (startY + MARGIN + GAP + Math.floor((double) (MARGIN * PRE_SIZE) / (128 - MARGIN))) : startY+MARGIN+GAP;

        int end = start+DOOR_SIZE[0];
        int end2 = start2+DOOR_SIZE[1];

        for (   int x = Math.min(start,end);  x <= Math.max(start,end); x++) {
            for(int y = Math.min(start2,end2);  y <= Math.max(start2,end2); y++) {
                if(canvas.getPixelColor(x, y) != null) continue;
                canvas.setPixelColor(x, y, color);
            }
        }
        if(rot) rotate();
//        canvas.setPixelColor(
//                start,
//                start2,
//                Color.WHITE);
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
    void rotate(){
        DOOR_SIZE[0] = DOOR_SIZE[0]+DOOR_SIZE[1];
        DOOR_SIZE[1] = DOOR_SIZE[0]-DOOR_SIZE[1];
        DOOR_SIZE[0] = DOOR_SIZE[0]-DOOR_SIZE[1];
    }
}
