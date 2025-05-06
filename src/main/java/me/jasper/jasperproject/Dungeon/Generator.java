package me.jasper.jasperproject.Dungeon;

import lombok.Setter;
import me.jasper.jasperproject.Dungeon.Rooms.Rooms;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Generator extends DungeonUtil{
    @Setter long seed = new Random().nextLong();
    @Setter int p = 4;
    @Setter int l = 4;
    int MAX_RECUR_TRIES = 1000;
    int OCCUR = 0;

    //room limit
    Map<RoomType,Integer> CURRENT_LIMIT = new HashMap<>(Map.of(
            RoomType.TWO_X_ONE, 0,
            RoomType.THREE_X_ONE, 0,
            RoomType.FOUR_X_ONE, 0,
            RoomType.BOX, 0,
            RoomType.L_SHAPE, 0,
            RoomType.SINGLE, 0,
            RoomType.PUZZLE, 0,
            RoomType.TRAP, 0,
            RoomType.MINI_BOSS, 0

    ));

    HashMap<RoomType,Integer> MAX_LIMIT = new HashMap<>(Map.of(
            RoomType.TWO_X_ONE, 2,
            RoomType.THREE_X_ONE, 1,
            RoomType.FOUR_X_ONE, 1,
            RoomType.BOX,  1,
            RoomType.L_SHAPE, 1,
            RoomType.SINGLE, 10,
            RoomType.SPECIAL, 3
    ));
    DungeonHandler handler = new DungeonHandler(p, l, seed);
    /*
    * THIS IS THE MAIN METHOD
    * */
    public void generate(){
        Room[][] grid = handler.getGrid();
        handler.setDebug_mode(true);
        TextComponent message = new TextComponent("Dungeon Seed [" +ChatColor.GREEN+ seed +ChatColor.WHITE+"]");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(seed)));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy the seeds!")));
        Bukkit.spigot().broadcast(message);

        long startTime = System.nanoTime();
        placeMainDungeon();

        //Parsing current path into the actual shaped rooms
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < l; j++) {
                if(grid[i][j]==null || grid[i][j].type != RoomType.TEST) continue;
                Point point = new Point(i, j);
                this.defineRoom(handler, point, true, CreatedRoom.path1);
                this.defineRoom(handler, point, true, CreatedRoom.path2);
            }
        }

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
                Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. OCCUR outbeat MAX_RECUR_TRIES");
                return;
            }
        }while (distance3 < p-1);

        grid[x][y] = CreatedRoom.entrance.clone();
        grid[x][y].setLoc(new Point(x,y));
        grid[x3][y3] = CreatedRoom.blood.clone();
        grid[x3][y3].setLoc(new Point(x3,y3));

        boolean found;
        int prevx,prevy;
        do{
            prevx = random.nextInt(p);
            prevy = random.nextInt(l);
            if(OCCUR++ > MAX_RECUR_TRIES){
                Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. OCCUR outbeat MAX_RECUR_TRIES");
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
                    Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. OCCUR outbeat MAX_RECUR_TRIES");
                    return;
                }
            }while (grid[x2][y2]!=null && distance1 < ((double) p /2)-((double) p /4)+1 || distance2 < ((double) p /2)-((double) p /4)+1);
            prevx = x2;
            prevy = y2;
            grid[x2][y2] = CreatedRoom.fairy;
            found = this.findPath(handler);
            if(OCCUR++ > MAX_RECUR_TRIES){
                Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. OCCUR outbeat MAX_RECUR_TRIES");
                return;
            }
        }while(!found);
        grid[x2][y2].setLoc(new Point(x2*32,y2*32));
        history.remove(new Point(x2,y2));
        history.addFirst(new Point(x2,y2));
    }


    //this store the edge and the index of the edge (Point, Index)
    List<Point> possiblePoint = new ArrayList<>();
    private void placePTMR(){
        Room[][] grid = handler.getGrid();
        LinkedList<Room> avaibleRooms = handler.get(RoomType.SPECIAL);
        Map<Point, Point> doors = handler.getDoorMap();
        Queue<Point> endpoint = handler.getEdge();

        for (int i = 0; i < grid.length; i++) {
             for (int j = 0; j < grid[0].length; j++){
                 if(avaibleRooms.isEmpty()) {
                     return;
                 }
                 Point neighbor = getNeighbor(i, j, grid[i][j],false);
                    if(grid[i][j] == null){
                        if (neighbor != null) {
                            grid[i][j] = avaibleRooms.pop();
                            grid[i][j].setLoc(new Point(i*32, j*32));
                            grid[i][j].addBody(new Point(i, j));

                            doors.put(new Point(i, j), neighbor);
                            endpoint.add(new Point(i, j));
//                            buildEmtyDoor(handler);
                        }
                        continue;
                    }

                    if(grid[i][j].type == RoomType.SINGLE && grid[i][j].conected_room.values().stream().mapToInt(HashSet::size).sum() == 1) {
                        if(avaibleRooms.isEmpty()) return;
                        grid[i][j].replace(avaibleRooms.pop(), false);
                    }
             }
         }

        /**
         * Broke the room into pieces. | L -> 2x1 | BOX -> L | 4x1... -> 3x1...
         * */
        if(!avaibleRooms.isEmpty()){
//            List<Room> list_room_to_refresh = new ArrayList<>();
            getPointIntegerHashMap();
            for (Point body : possiblePoint){
                if(avaibleRooms.isEmpty()) return;

                RoomShape shape = switch (grid[body.x][body.y].type){
                    case L_SHAPE -> RoomShape.TWO_X_ONE;
                    case BOX -> RoomShape.L_SHAPE;
                    case FOUR_X_ONE -> RoomShape.THREE_X_ONE;
                    case THREE_X_ONE -> RoomShape.TWO_X_ONE;
                    case TWO_X_ONE -> RoomShape.ONE;
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
                grid[body.x][body.y].addBody(new Point(body.x, body.y));
                grid[body.x][body.y].setLoc(new Point(body.x,body.y));

                isFit(handler, point, shape, grid[point.x][point.y]);

                grid[body.x][body.y].addConection(body,point);
                grid[point.x][point.y].addConection(point,body);

//                buildEmtyDoor(handler);
            }
        }
    }

    int[][] dir = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    int[][] diagonal_dir = {{1,1}, {-1,-1}, {1,-1}, {-1,1}};

    HashSet<Point> neighborlist = new HashSet<>();
    private Point getNeighbor(int x, int y, Room room, boolean isBox){
        List<Point> conected_point_counter = new ArrayList<>();
        Room[][] grid = handler.getGrid();
        int dx = 0;
        int dy = 0;
        if(neighborlist.contains(new Point(x, y))) return null;
        if(room==null){
            for (int[] dirs : dir){
                dx = x + dirs[0];
                dy = y + dirs[1];

                boolean right = dx >= 0 && dx < grid.length;
                boolean left = dy >= 0 && dy < grid[0].length;
                if(right && left &&
                        grid[dx][dy] != null && (grid[dx][dy].type == RoomType.L_SHAPE ||
                        grid[dx][dy].type == RoomType.TWO_X_ONE ||
                        grid[dx][dy].type == RoomType.SINGLE ||
                        grid[dx][dy].type == RoomType.THREE_X_ONE ||
                        grid[dx][dy].type == RoomType.FOUR_X_ONE || grid[dx][dy].type == RoomType.BOX)){
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
                    return  new Point(dx, dy);
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

    private void getPointIntegerHashMap() {
        Room[][] grid = handler.getGrid();
        Map<Point, Point> doors = handler.getDoorMap();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++){
                if(grid[i][j] == null) continue;
                if(grid[i][j].type == RoomType.START || grid[i][j].type == RoomType.MID || grid[i][j].type == RoomType.END || grid[i][j].name == "PATH" || grid[i][j].type == RoomType.SINGLE) continue;
                Point current = new Point(i, j);
                Point neighbor;


                if(grid[i][j].conected_room.containsKey(current)) {
                    continue;
                }
                neighbor = getNeighbor(i, j, grid[i][j], true);
                if(grid[i][j].type ==RoomType.BOX && neighbor!=null){
                    doors.put(current, neighbor);
                    possiblePoint.add(current);
                    continue;
                }

                neighbor = getNeighbor(i, j, grid[i][j], false);
                if(neighbor!=null){
                    possiblePoint.add(current);
                    doors.put(current,neighbor);
                }
            }
        }
    }

    //This gona render the dungeon to the actual shape
    private void render(){
        for (Room[] rooms : handler.getGrid()) {
//            StringBuilder stringBuilder = new StringBuilder();
            for (Room room : rooms) {
                try{
                    room.loadScheme();
                    //conected_room.values().stream().mapToInt(HashSet::size).sum()
//                    stringBuilder.append(room.logo).append(", ");
                }catch (NullPointerException e){
//                    stringBuilder.append(0).append(", ");
                }
            }
//            Bukkit.broadcastMessage(stringBuilder.toString());
        }
    }
}
