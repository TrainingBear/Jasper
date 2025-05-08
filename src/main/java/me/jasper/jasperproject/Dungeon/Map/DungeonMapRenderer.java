package me.jasper.jasperproject.Dungeon.Map;

import lombok.Getter;
import me.jasper.jasperproject.Dungeon.DungeonHandler;
import me.jasper.jasperproject.Dungeon.Generator;
import me.jasper.jasperproject.Dungeon.Room;
import me.jasper.jasperproject.Dungeon.RoomType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DungeonMapRenderer extends MapRenderer {
    private final DungeonHandler handler;
    private final int GAP = 5;
    private final int MARGIN = 5;
    private int MARGINX = MARGIN;
    private int MARGINY = MARGIN;
    @Getter private final double CELL_SIZE;
    private final double PRE_SIZE;
    private final ArrayList<Room> rooms;
    private final int[] DOOR_SIZE;
    @Getter private final double FINAL_CELL_SIZE;

    public DungeonMapRenderer(Generator generator){
        this.handler = generator.getHandler();
        int GRID_PANJANG = generator.getP();
        int GRID_LEBAR = generator.getL();
        PRE_SIZE = ((double) 128 /Math.max(GRID_PANJANG , GRID_LEBAR));
        CELL_SIZE = (PRE_SIZE-(double) MARGIN*PRE_SIZE / (48-MARGIN));
        rooms = getRooms(generator);
        DOOR_SIZE = new int[]{(int) (-PRE_SIZE/4.5), -6};
        FINAL_CELL_SIZE = CELL_SIZE-GAP;
        MARGINX = GRID_LEBAR > GRID_PANJANG? (int) (MARGIN + CELL_SIZE / 2) : MARGIN;
        MARGINY = GRID_PANJANG > GRID_LEBAR? (int) (MARGIN + CELL_SIZE / 2) : MARGIN;

    }


    boolean rendered = false;

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {

        if(rendered) return;

//        for (int i = 0; i < 128; i++) {
//            for (int j = 0; j < 128; j++) {
//                mapCanvas.setPixelColor(i, j, new Color(255, 255, 255));
//            }
//        }

        buildDoor(mapCanvas, handler.getRoomMap(),
                handler.getBloodRoom(), true);

        for (Point endpoint : handler.getEdge()){
            buildDoor(mapCanvas, handler.getDoorMap(),
                    endpoint, false);
        }


        for (Room room : rooms) {
            if(room==null) continue;
            if(room.getType() == RoomType.L_SHAPE){
                List<Point> L = sort(room.getBody());

                Color color = room.getType().color;

                drawRoom(mapCanvas,
                        Math.min(L.get(0).x, L.get(2).x), Math.min(L.get(0).y, L.get(2).y),
                         color,
                        Math.max(L.get(0).x, L.get(2).x), Math.max(L.get(0).y, L.get(2).y),false);

                drawRoom(mapCanvas,
                        Math.min(L.get(1).x, L.get(2).x), Math.min(L.get(1).y, L.get(2).y),
                         color,
                        Math.max(L.get(1).x, L.get(2).x), Math.max(L.get(1).y, L.get(2).y),false);
                continue;
            }

            int minX = Integer.MAX_VALUE,minY = Integer.MAX_VALUE,
                    maxX = Integer.MIN_VALUE,maxY = Integer.MIN_VALUE;
            for (Point point : room.getBody()){
                minX = Math.min(point.x, minX);
                minY = Math.min(point.y, minY);
                maxX = Math.max(point.x, maxX);
                maxY = Math.max(point.y, maxY);
            }


            Color color = room.getType().color;
            drawRoom(mapCanvas, minX, minY, color,
                        maxX, maxY, false);
        }


        rendered = true;
    }

    private void drawRoom(MapCanvas canvas, int startx, int starty, Color color, int endx, int endy, boolean debug){
        int startX = (int) (startx*CELL_SIZE+MARGINX+GAP);
        int startY = (int) (starty*CELL_SIZE+MARGINY+GAP);

        int endX = (int) ((endx*(CELL_SIZE)) + CELL_SIZE+MARGINX);
        int endY = (int) ((endy*(CELL_SIZE)) + CELL_SIZE+MARGINY);

        if (debug) Bukkit.broadcastMessage(ChatColor.GRAY+"    Drew from "+startX+", "+startY+" to "+endX+", "+endY);
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                canvas.setPixelColor(x,y,color);
            }
        }
    }

    private void buildDoor(MapCanvas canvas, Map<Point, Point> parentMapOri, Point end, boolean locked) {
        Point step = end;
        Room d1, d2;
        Point pre_step, transition;
        boolean rotation;
        Map<Point, Point> parentMap = new HashMap<>(Map.copyOf(parentMapOri));
        Room[][] grid = handler.getGrid();

        while (!parentMap.isEmpty()) {
            pre_step = step;
            d1 = handler.getGrid(pre_step);

            step = parentMap.remove(step);
            if(step == null){
                return;
            }
            d2 = handler.getGrid(step);

            transition = new Point((int) (-(pre_step.x - step.x)*CELL_SIZE/2), (int) (-(pre_step.y - step.y)*CELL_SIZE/2));
            rotation = transition.x != 0;

            if(!Objects.equals(d1, d2)){
                drawDoor(canvas,
                        (int) (pre_step.x*(CELL_SIZE)+(CELL_SIZE/2)+transition.x),
                        (int) (pre_step.y*(CELL_SIZE)+(CELL_SIZE/2)+transition.y),
                        locked? new Color(54, 11, 11) : handler.getGrid(pre_step).getType().color, rotation);
            }
        }
    }

    private void drawDoor(MapCanvas canvas, int startX, int startY, Color color, boolean rot){
        if(rot) rotate();

        int start = rot? startX+MARGINX+GAP : (int) (startX + MARGINX + GAP + Math.floor((double) (MARGIN * PRE_SIZE) / (128 - MARGIN)));
        int start2 = rot? (int) (startY + MARGINY + GAP + Math.floor((double) (MARGIN * PRE_SIZE) / (128 - MARGIN))) : startY+MARGINY+GAP;

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

    private ArrayList<Room> getRooms(Generator generator){
        ArrayList<Room> rooms = new ArrayList<>();
        Room[][] grid = handler.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if(!rooms.contains(grid[i][j])){
                    rooms.add(grid[i][j]);
                }
            }
        }
        return rooms;
    }
    private List<Point> sort(List<Point> body){
        int[][] dirs = {{1,0},{0,1},{-1,0},{0,-1},};
        LinkedList<Point> sorted = new LinkedList<>();
        for(Point point : body){
            int counter = 0;
            for(int[] dir : dirs){
                if(body.contains(new Point(point.x-dir[0], point.y-dir[1]))){
                    counter++;
                }
            }
            if(counter>=2){
                sorted.addLast(point);
                continue;
            }
            sorted.addFirst(point);
        }
        return sorted;
    }
    private void rotate(){
        DOOR_SIZE[0] = DOOR_SIZE[0]+DOOR_SIZE[1];
        DOOR_SIZE[1] = DOOR_SIZE[0]-DOOR_SIZE[1];
        DOOR_SIZE[0] = DOOR_SIZE[0]-DOOR_SIZE[1];
    }
}
