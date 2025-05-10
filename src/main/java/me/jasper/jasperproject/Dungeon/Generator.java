package me.jasper.jasperproject.Dungeon;

import lombok.Getter;
import me.jasper.jasperproject.Dungeon.Shapes.*;
import me.jasper.jasperproject.Dungeon.Shapes.Shape;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;

@Getter
public class Generator extends DungeonUtil{
    private long seed = new Random().nextLong();
    private int p = 4;
    private int l = 4;
    int MAX_RECUR_TRIES = 1000;
    int OCCUR = 0;
    private final DungeonHandler handler;
    public Generator(int p, int l){
        this.p = p; this.l = l;
        this.handler = new DungeonHandler(p, l, this.seed);
    }
    public Generator(int p, int l, long seed){
        this.p = p; this.l = l; this.seed=seed;
        this.handler = new DungeonHandler(p, l, seed);
    }

    public Generator() {
        this.handler = new DungeonHandler(p, l, seed);
    }

    //    //room limit
//    Map<RoomType,Integer> CURRENT_LIMIT = new HashMap<>(Map.of(
//            RoomType.TWO_X_ONE, 0,
//            RoomType.THREE_X_ONE, 0,
//            RoomType.FOUR_X_ONE, 0,
//            RoomType.BOX, 0,
//            RoomType.L_SHAPE, 0,
//            RoomType.SINGLE, 0,
//            RoomType.PUZZLE, 0,
//            RoomType.TRAP, 0,
//            RoomType.MINI_BOSS, 0
//
//    ));
//
//    HashMap<RoomType,Integer> MAX_LIMIT = new HashMap<>(Map.of(
//            RoomType.TWO_X_ONE, 2,
//            RoomType.THREE_X_ONE, 1,
//            RoomType.FOUR_X_ONE, 1,
//            RoomType.BOX,  1,
//            RoomType.L_SHAPE, 1,
//            RoomType.SINGLE, 10,
//            RoomType.SPECIAL, 3
//    ));
    /*
    * THIS IS THE MAIN METHOD
    * */
    public void generate(){
        Map<RoomType, LinkedList<Room>> rooms = handler.getRooms();
        handler.addRoom(RoomType.SPECIAL, CreatedRoom.TRAP.clone());
        handler.addRoom(RoomType.SPECIAL, CreatedRoom.PUZZLE1.clone());

        handler.addRoom(RoomType.SINGLE, CreatedRoom.SINGLE.clone());
        handler.addRoom(RoomType.TWO_X_ONE, CreatedRoom.TWO.clone());
        handler.addRoom(RoomType.THREE_X_ONE, CreatedRoom.THREE.clone());
        handler.addRoom(RoomType.FOUR_X_ONE, CreatedRoom.FOUR.clone());
        handler.addRoom(RoomType.L_SHAPE, CreatedRoom.L.clone());
        handler.addRoom(RoomType.BOX, CreatedRoom.BOX.clone());

        Map<Point, Point> roomMap = handler.getRoomMap();
        handler.setDebug_mode(true);
        TextComponent message = new TextComponent("Dungeon Seed [" +ChatColor.GREEN+ seed +ChatColor.WHITE+"]");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(seed)));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy the seeds!")));
        Bukkit.spigot().broadcast(message);

        long startTime = System.nanoTime();
        handler.setDebug_mode(false);
        placeMainDungeon();

        //Parsing current path into the actual shaped rooms

        for (Point point : roomMap.keySet()) {
            Room room = handler.getGrid(point);
            if(room == null || room.getType() != RoomType.TEST) continue;
            if (room.getName().equals(CreatedRoom.path2.getName())) {
                this.defineRoom(handler, point, true, CreatedRoom.path2);
                continue;
            }
            this.defineRoom(handler, point, true, CreatedRoom.path1);
        }

        handler.setDebug_mode(true);
        //Fill Dungeon room
        fill(handler, true);
        long endTime = System.nanoTime();
        String time = String.format("%.2f", (endTime - startTime) / 1_000_000.0);
        Bukkit.broadcastMessage(ChatColor.GREEN+"Initializing main took "+time +" ms");

        buildDoor(handler);
        for (Point end : handler.getEdge()){
            buildEmtyDoor(end, handler);
        }

        startTime = System.nanoTime();
        placePTMR();
        render();
        endTime = System.nanoTime();
        String time2 = String.format("%.2f", (endTime - startTime) / 1_000_000.0);

        Bukkit.broadcastMessage(ChatColor.GREEN+"Rendering dungeon took "+time2+" ms");
    }

    //Place START, MID, END & Generate its path
    private void placeMainDungeon(){
        double distance1,distance2,distance3;
        int x, y, x2, y2, x3, y3;
        Random random = handler.getRandom();
        Room[][] grid = handler.getGrid();
        Map<Point, Point> roomMap = handler.getRoomMap();
        Stack<Point> history = handler.getHistory();
        do{
            x = random.nextInt(p);
            y = random.nextInt(l);
            x3 = random.nextInt(p);
            y3 = random.nextInt(l);
            distance3 = Point.distance(x,y,x3,y3);
            if(OCCUR++ > MAX_RECUR_TRIES){
                Bukkit.broadcast(Util.deserialize("Something when loading the main instance.").color(NamedTextColor.RED));
                return;
            }
        }while (distance3 < p-1);

        Point start = new Point(x, y);
        Point end = new Point(x3, y3);
        handler.setEntrance(start);
        handler.setBloodRoom(end);
        grid[x][y] = CreatedRoom.entrance.clone();
        grid[x][y].setLoc(start);
        grid[x3][y3] = CreatedRoom.blood.clone();
        grid[x3][y3].setLoc(end);

        boolean found;
        int prevx,prevy;
        do{
            prevx = random.nextInt(p);
            prevy = random.nextInt(l);
            if(OCCUR++ > MAX_RECUR_TRIES){
                Bukkit.broadcast(Util.deserialize("Can't find any path!").color(NamedTextColor.RED));
                return;
            }
        }while (grid[prevx][prevy] != null);

        do{
            roomMap.clear();
            do{
                grid[prevx][prevy] = null;
                x2 = random.nextInt(p);
                y2 = random.nextInt(l);
                distance1 = Point.distance(x,y,x2,y2);
                distance2 = Point.distance(x2,y2,x3,y3);
                if(OCCUR++ > MAX_RECUR_TRIES){
                    Bukkit.broadcast(Util.deserialize("Failed to load Dungeon.").color(NamedTextColor.RED));
                    return;
                }
            } while (grid[x2][y2]!=null && distance1 < ((double) p /2)-((double) p /4)+1 || distance2 < ((double) p /2)-((double) p /4)+1);
            prevx = x2;
            prevy = y2;
            Point fairy = new Point(x2, y2);
            handler.setFairy(fairy);
            grid[x2][y2] = CreatedRoom.fairy.clone();
            found = this.findPath(handler);
            if(OCCUR++ > MAX_RECUR_TRIES){
                Bukkit.broadcast(Util.deserialize("Failed to load Dungeon.").color(NamedTextColor.RED));
                return;
            }
        }while(!found);
        Point fairy = new Point(x2, y2);
        handler.setFairy(fairy);
        grid[x2][y2].setLoc(fairy);
        history.remove(fairy);
        history.addFirst(fairy);
    }


    //this store the edge and the index of the edge (Point, Index)
    List<Point> possiblePoint = new ArrayList<>();
    private void placePTMR(){
        Room[][] grid = handler.getGrid();
        LinkedList<Room> avaibleRooms = handler.get(RoomType.SPECIAL);
        Map<Point, Point> doors = handler.getDoorMap();
        Queue<Point> endpoint = handler.getEdge();

        for (int i = 0; i < grid.length; i++)
             for (int j = 0; j < grid[0].length; j++){
                 if(avaibleRooms == null || avaibleRooms.isEmpty()) return;
                 Point point = new Point(i, j);
                    if(grid[i][j] == null){
                        Point neighbor = getNeighbor(point, null,false);
                        if (neighbor != null) {
                            grid[i][j] = avaibleRooms.pop();
                            grid[i][j].setLoc(point);
                            grid[i][j].addBody(point);

                            doors.put(point, neighbor);
                            endpoint.add(point);
                            buildEmtyDoor(point, handler);
                        }
                        continue;
                    }

                    if(grid[i][j].getType() == RoomType.SINGLE && grid[i][j].getConected_room().values().stream().mapToInt(HashSet::size).sum() == 1) {
                        if(avaibleRooms.isEmpty()) return;
                        grid[i][j].replace(avaibleRooms.pop(), false);
                    }
         }

        /**
         * Broke the room into pieces. | L -> 2x1 | BOX -> L | 4x1... -> 3x1...
         * */
        if(!avaibleRooms.isEmpty()){
            loadPossiblePoint();
            for (Point body : possiblePoint){
                if(avaibleRooms.isEmpty()) return;
                Shape shape = switch (grid[body.x][body.y].getType()){
                    case L_SHAPE -> new L_BY_L();
                    case BOX -> new BOX_BY_BOX();
                    case FOUR_X_ONE -> new THREE_BY_THREE();
                    case THREE_X_ONE -> new TOW_BY_TWO();
                    case TWO_X_ONE -> new ONE_BY_ONE();
                    case PUZZLE -> null;
                    case START -> null;
                    case MID -> null;
                    case END -> null;
                    case TRAP -> null;
                    case MINI_BOSS -> null;
                    case END2 -> null;
                    case SPECIAL -> null;
                    case SINGLE -> null;
                    case TEST -> null;
                };
                endpoint.add(body);
                Point point = doors.get(body);
                grid[body.x][body.y].replace(new Room("TES",RoomType.L_SHAPE,6669,"null",null, null), true);
                grid[body.x][body.y] = avaibleRooms.pop();
                grid[body.x][body.y].addBody(body);
                grid[body.x][body.y].setLoc(body);

                isFit(handler, point, shape, handler.getGrid(point));

                grid[body.x][body.y].addConection(body,point);
                grid[point.x][point.y].addConection(point,body);

                buildEmtyDoor(body, handler);
            }
        }
    }

    int[][] dir = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    int[][] diagonal_dir = {{1,1}, {-1,-1}, {1,-1}, {-1,1}};

    HashSet<Point> neighborlist = new HashSet<>();
    private @Nullable Point getNeighbor(Point point, @Nullable Room room, boolean isBox){
        List<Point> conected_point_counter = new ArrayList<>();
        Room[][] grid = handler.getGrid();
        int dx, dy ;
        if(neighborlist.contains(point)) return null;
        int x= point.x, y = point.y;
        if(room==null){
            for (int[] dirs : dir){
                dx = x + dirs[0];
                dy = y + dirs[1];

                boolean right = dx >= 0 && dx < grid.length;
                boolean left = dy >= 0 && dy < grid[0].length;
                if(right && left &&
                        grid[dx][dy] != null && (grid[dx][dy].getType() == RoomType.L_SHAPE ||
                        grid[dx][dy].getType() == RoomType.TWO_X_ONE ||
                        grid[dx][dy].getType() == RoomType.SINGLE ||
                        grid[dx][dy].getType() == RoomType.THREE_X_ONE ||
                        grid[dx][dy].getType() == RoomType.FOUR_X_ONE || grid[dx][dy].getType() == RoomType.BOX)){
                    return new Point(dx,dy);
                }
            }
            return null;
        }
        if(isBox){
            for(int[] dirs : diagonal_dir){
                dx = x + dirs[0];
                dy = y + dirs[1];

                if( dx >= 0 && dx < grid.length &&
                dy >= 0 && dy < grid[0].length && Objects.equals(grid[dx][dy], room) &&
                possiblePoint.contains(new Point(dx, dy))){
                    return null;
                }
            }
            for (int[] dirs : dir){
                dx = x + dirs[0];
                dy = y + dirs[1];

                boolean right = dx >= 0 && dx < grid.length;
                boolean left = dy >= 0 && dy < grid[0].length;
                if(right && left && Objects.equals(grid[dx][dy], room) && !possiblePoint.contains(new Point(dx, dy))){
                    room.addConection(new Point(dx, dy), new Point(x, y));
                    return new Point(dx, dy);
                }
            }
            return null;
        }
         for (int[] dirs : dir){
             dx = x + dirs[0];
             dy = y + dirs[1];

             boolean right = dx >= 0 && dx < grid.length;
             boolean left = dy >= 0 && dy < grid[0].length;
             if(right && left && Objects.equals(grid[dx][dy], room)){
                 conected_point_counter.add(new Point(dx, dy));
             }
         }
        return conected_point_counter.size() == 1 ? conected_point_counter.getLast() : null;

    }

    private void loadPossiblePoint() {
        Room[][] grid = handler.getGrid();
        Map<Point, Point> doors = handler.getDoorMap();
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++){
                if(grid[i][j] == null) continue;
                if(grid[i][j].getType() == RoomType.START || grid[i][j].getType() == RoomType.MID || grid[i][j].getType() == RoomType.END || grid[i][j].getName() == "PATH" || grid[i][j].getType() == RoomType.SINGLE) continue;
                Point current = new Point(i, j);
                Point neighbor;

                if(grid[i][j].getConected_room().containsKey(current)) continue;
                boolean isBox = grid[i][j].getType().equals(RoomType.BOX);
                if(isBox){
                    neighbor = getNeighbor(current, grid[i][j], true);
                    if(neighbor!=null){
                        doors.put(current, neighbor);
                        possiblePoint.addFirst(current);
                        continue;
                    }
                }

                neighbor = getNeighbor(current, grid[i][j], false);
                if(neighbor!=null){
                    possiblePoint.add(current);
                    doors.put(current,neighbor);
                }
            }

    }

    //This gona render the dungeon to the actual shape
    private void render(){
        Bukkit.getScheduler().runTaskAsynchronously(JasperProject.getPlugin(), () -> {
            for (Room[] rooms : handler.getGrid()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Room room : rooms) {
                    try{
                        room.loadScheme();
                        stringBuilder.append(room.getLogo()).append(", ");
                    }catch (NullPointerException e){
                        stringBuilder.append(0).append(", ");
                    }
                }
                if(handler.isDebug_mode()) Bukkit.broadcast(Util.deserialize(stringBuilder.toString()).color(NamedTextColor.YELLOW));
            }
        });
    }
}