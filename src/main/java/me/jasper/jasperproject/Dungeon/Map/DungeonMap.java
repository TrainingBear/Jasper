package me.jasper.jasperproject.Dungeon.Map;

import lombok.Getter;
import me.jasper.jasperproject.Dungeon.DungeonHandler;
import me.jasper.jasperproject.Dungeon.Generator;
import me.jasper.jasperproject.Dungeon.Room;
import me.jasper.jasperproject.Dungeon.RoomType;
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R3.map.CraftMapCursor;
import org.bukkit.craftbukkit.v1_21_R3.map.RenderData;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DungeonMap {
    private static int GRID_PANJANG;
    private static int GRID_LEBAR;
    private final DungeonHandler handler;
    private final int GAP = 5;
    private final int MARGIN = 5;
    private final int MARGINX;
    private final int MARGINY;
    @Getter private final double CELL_SIZE;
    private final double PRE_SIZE;
    private final int[] DOOR_SIZE;
    @Getter private final double FINAL_CELL_SIZE;
    private final RenderData data = new RenderData();
    private Set<Room> uniqueRoom = new HashSet<>();
    @Getter private static Map<UUID, BukkitTask> maps = new HashMap<>();

    public DungeonMap(Generator generator){
        this.handler = generator.getHandler();
        this.GRID_PANJANG = generator.getP();
        this.GRID_LEBAR = generator.getL();
        this.PRE_SIZE = ((double) 128 /Math.max(GRID_PANJANG , GRID_LEBAR));
        this.CELL_SIZE = (PRE_SIZE-(double) MARGIN*PRE_SIZE / (48-MARGIN));
        this.DOOR_SIZE = new int[]{(int) (-PRE_SIZE/4.5), -6};
        this.FINAL_CELL_SIZE = CELL_SIZE-GAP;
        this.MARGINX = GRID_LEBAR > GRID_PANJANG? (int) (MARGIN + CELL_SIZE / 2) : MARGIN;
        this.MARGINY = GRID_PANJANG > GRID_LEBAR? (int) (MARGIN + CELL_SIZE / 2) : MARGIN;
    }

    public void setViewer(Player player){
        renderCanvas(player);
        BukkitTask task = maps.get(player.getUniqueId());
        if(task!=null)task.cancel();
        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                renderCursor(player);
            }
        }.runTaskTimerAsynchronously(JasperProject.getPlugin(), 10, 10);
        maps.put(player.getUniqueId(), runnable);
    }
    public void delete(Player player){
        BukkitTask task = maps.remove(player.getUniqueId());
        if(task!=null)task.cancel();
    }
    private void renderCanvas(Player... players) {
        buildDoor();
        for (Point point : handler.getEdge()) buildEmptyDoor(point);
        for (Room room : uniqueRoom) {
            if(room==null) continue;
//            List<Point> body = Shape.rotate(List.copyOf(room.getBody()), 180);
            List<Point> body = List.copyOf(room.getBody());
            refactor(body);
            if(room.getType().equals(RoomType.L_SHAPE)){
                List<Point> L = sort(body);
                byte color = room.getType().getColor();
                int min = Math.min(L.get(0).x, L.get(2).x);
                int min1 = Math.min(L.get(0).y, L.get(2).y);
                int max = Math.max(L.get(0).x, L.get(2).x);
                int max1 = Math.max(L.get(0).y, L.get(2).y);
                drawRoom(
                        min, min1,
                        color,
                        max1, max,false);
                int min2 = Math.min(L.get(1).x, L.get(2).x);
                int min3 = Math.min(L.get(1).y, L.get(2).y);
                int max2 = Math.max(L.get(1).x, L.get(2).x);
                int max3 = Math.max(L.get(1).y, L.get(2).y);
                drawRoom(
                        -min2, -min3,
                        color,
                        -max2, -max3,false);
                continue;
            }
            int minX = Integer.MAX_VALUE,minY = Integer.MAX_VALUE,
                    maxX = Integer.MIN_VALUE,maxY = Integer.MIN_VALUE;

            for (Point point : body){
                minX = Math.min(point.x, minX);
                minY = Math.min(point.y, minY);
                maxX = Math.max(point.x, maxX);
                maxY = Math.max(point.y, maxY);
            }
            byte color = room.getType().getColor();
            drawRoom(-minX, -minY, color,
                        -maxX, -maxY, false);
        }
        ClientboundMapItemDataPacket packet = new ClientboundMapItemDataPacket(
                new MapId(1),
                MapView.Scale.NORMAL.getValue(),
                false,
                null,
                new MapItemSavedData.MapPatch(0, 0, 128, 128, data.buffer)
        );
        for(Player player : players) ((CraftPlayer) player).getHandle().connection.send(packet);
    }
    void renderCursor(Player... players){
        Collection<MapDecoration> icons = new ArrayList<>();
        for(Player player : players){
            Location loc = player.getLocation();
            byte x = (byte) (((loc.getX() - (((GRID_PANJANG * 32)/2)-16)) * ((getFINAL_CELL_SIZE() * 1.75)) / getFINAL_CELL_SIZE()));
            byte y = (byte) (((loc.getZ() - (((GRID_LEBAR * 32)/2)-16)) * ((getFINAL_CELL_SIZE() * 1.75)) / getFINAL_CELL_SIZE()));
            MapCursor cursor = new MapCursor(x, y, (byte) (loc.getYaw() < 0 ? 16 + (loc.getYaw() / 22.5) : loc.getYaw() / 22),
                    MapCursor.Type.PLAYER, true);
            icons.add(new MapDecoration(
                    CraftMapCursor.CraftType.bukkitToMinecraftHolder(cursor.getType()),
                    cursor.getX(),
                    cursor.getY(),
                    cursor.getDirection(),
                    CraftChatMessage.fromStringOrOptional(cursor.getCaption()))
            );
        }
        ClientboundMapItemDataPacket packet = new ClientboundMapItemDataPacket(
                new MapId(1),
                MapView.Scale.NORMAL.getValue(),
                false,
                icons,
                new MapItemSavedData.MapPatch(0, 0, 128, 128, data.buffer)
        );
        for (Player player : players) ((CraftPlayer) player).getHandle().connection.send(packet);
    }
    private void drawRoom(int startx, int starty, byte color, int endx, int endy, boolean debug){
        int startX = (int) (startx*CELL_SIZE+MARGINX+GAP);
        int startY = (int) (starty*CELL_SIZE+MARGINY+GAP);

        int endX = (int) ((endx*(CELL_SIZE)) + CELL_SIZE+MARGINX);
        int endY = (int) ((endy*(CELL_SIZE)) + CELL_SIZE+MARGINY);
        if (debug) Bukkit.broadcast(Component.text("    Drew from "+startX+", "+startY+" to "+endX+", "+endY).color(NamedTextColor.GRAY));
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                data.buffer[x*128+y] = color;
            }
        }
    }
    void buildEmptyDoor(Point end) {
        Map<Point, Point> doors = handler.getDoorMap();
        Point nd = doors.remove(end); if(nd == null) return;
        Point transition = new Point(-(nd.x - end.x)*16, -(nd.y - end.y)*16);
        boolean rotation = transition.x!=0;
        drawDoor(
                (int) (end.x*(CELL_SIZE)+(CELL_SIZE/2)+transition.x), (int) (end.y*(CELL_SIZE)+(CELL_SIZE/2)+transition.y),
                handler.getGrid(end).getType().getColor(), rotation
        );
        Bukkit.broadcast(Component.text("Door has been drawn!"));
    }
    private void buildDoor() {
        Point step = handler.getBloodRoom();
        Room d1, d2;
        Point pre_step, transition;
        boolean rotation;
        Map<Point, Point> parentMap = handler.getRoomMap();
        while (!parentMap.isEmpty()) {
            pre_step = step;
            d1 = handler.getGrid(pre_step);
            step = parentMap.get(step);
            if(step == null) return;
            d2 = handler.getGrid(step);
            transition = new Point((int) (-(pre_step.x - step.x)*CELL_SIZE/2), (int) (-(pre_step.y - step.y)*CELL_SIZE/2));
            rotation = transition.x != 0;
            if(!Objects.equals(d1, d2)){
                drawDoor(
                        (int) (pre_step.x*(CELL_SIZE)+(CELL_SIZE/2)+transition.x),
                        (int) (pre_step.y*(CELL_SIZE)+(CELL_SIZE/2)+transition.y),
                        RoomType.END2.getColor(), rotation);
            }
        }
    }

    private void drawDoor(int startX, int startY, byte color, boolean rot){
        if(rot) rotate();

        int start = rot? startX+MARGINX+GAP : (int) (startX + MARGINX + GAP + Math.floor((double) (MARGIN * PRE_SIZE) / (128 - MARGIN)));
        int start2 = rot? (int) (startY + MARGINY + GAP + Math.floor((double) (MARGIN * PRE_SIZE) / (128 - MARGIN))) : startY+MARGINY+GAP;

        int end = start+DOOR_SIZE[0];
        int end2 = start2+DOOR_SIZE[1];

        for (   int x = Math.min(start,end);  x <= Math.max(start,end); x++) {
            for(int y = Math.min(start2,end2);  y <= Math.max(start2,end2); y++) {
//                if(data.buffer[x*127+y] null) continue;
                data.buffer[x*128+y] = color;
            }
        }
        if(rot) rotate();
    }

    public void loadRoom(){
        Set<Room> rooms = new HashSet<>();
        Room[][] grid = handler.getGrid();
        for (Room[] value : grid) rooms.addAll(Arrays.asList(value).subList(0, grid[0].length));
        this.uniqueRoom = rooms;
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
    private void refactor(List<Point> body){
        for (Point point : body) {
            point.move(-point.x, -point.y);
        }
    }
}
