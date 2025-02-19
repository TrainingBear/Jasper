package me.jasper.jasperproject.Dungeon;

import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Generator extends DungeonUtil{
    Random rand = new Random();
    @Setter
    long seed = rand.nextLong();
    @Setter
    int p = 4;
    @Setter
    int l = 4;
    int MAX_RECUR_TRIES = 1000;
    int OCCUR = 0;
    boolean debug = false;
    Random random = new Random(seed);
    int x,y,x2,y2,x3,y3;

    Room entrance = new Room("Entrance",RoomType.START,1,"entrance", 'E');
    Room fairy = new Room("Fairy",RoomType.MID,2,"fairy", 'F');
    Room blood = new Room("Blood Room",RoomType.END,3,"blood", 'B');
    Room path1 = new Room("PATH",RoomType.TEST,4,"path1",'1');
    Room path2 = new Room("PATH2",RoomType.TEST,5,"path2", '1');

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

    Rooms avaibleRooms = new Rooms();
    HashMap<RoomType,Integer> MAX_LIMIT = new HashMap<>(Map.of(
            RoomType.TWO_X_ONE, 10,
            RoomType.THREE_X_ONE, 10,
            RoomType.FOUR_X_ONE, 10,
            RoomType.BOX,  10,
            RoomType.L_SHAPE, 10,
            RoomType.SINGLE, 10,
            RoomType.SPECIAL, 3
    ));
    Room[][] grid;
    Stack<Point> history = new Stack<>();
    Map<Point, Point> parentMap2 = new HashMap<>();

    /*
    * THIS IS THE MAIN METHOD
    * */
    public void generate(){
        grid = new Room[p][l];
        random.setSeed(seed);
        Map<Point, Point> parrentMap = new HashMap<>();
        Queue<Point> endpoint = new LinkedList<>();

        TextComponent message = new TextComponent("Dungeon Seed [" +ChatColor.GREEN+ seed +ChatColor.WHITE+"]");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(seed)));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy the seeds!")));
        Bukkit.spigot().broadcast(message);

        long startTime = System.nanoTime();

        placeMainDungeon(grid, parrentMap, history);

        //Parsing current path into the actual shaped rooms
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < l; j++) {
                this.defineRoom(avaibleRooms, this, i, j, path1, false);
                this.defineRoom(avaibleRooms, this, i, j, path2, false);
            }
        }

//        MAX_LIMIT = new HashMap<>(Map.of(
//                RoomType.TWO_X_ONE, 0,
//                RoomType.THREE_X_ONE, 0,
//                RoomType.FOUR_X_ONE, 10,
//                RoomType.BOX,  0,
//                RoomType.L_SHAPE, 0,
//                RoomType.SINGLE, 0,
//                RoomType.SPECIAL, 3
//        ));

        //Fill Dungeon room
        this.random_dir(avaibleRooms, this, endpoint, parentMap2);

        long endTime = System.nanoTime();
        String time = String.format("%.2f", (endTime - startTime) / 1_000_000.0);

        Bukkit.broadcastMessage(ChatColor.GREEN+"Initializing dungeon took "+time +" ms");


        this.buildDoor(parrentMap, new Point(x,y), new Point(x3,y3), grid);
        Bukkit.broadcastMessage(ChatColor.RED+"Building Empty Door..");
        for (Point end : endpoint){
            this.buildEmtyDoor(parentMap2, end, grid);
        }
        placePTMR();
        render();
        endTime = System.nanoTime();
        time = String.format("%.2f", (endTime - startTime) / 1_000_000.0);

        Bukkit.broadcastMessage(ChatColor.GREEN+"Rendering dungeon took "+time+" ms");
    }

    //Place START, MID, END & Generate its path
    private void placeMainDungeon(Room[][] grid, Map<Point, Point> parrentMap, Stack<Point> history){
        double distance1,distance2,distance3;
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

        grid[x][y] = entrance;
        grid[x][y].setLoc(new Point(x*32,y*32));
        grid[x3][y3] = blood;
        grid[x3][y3].setLoc(new Point(x3*32,y3*32));


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
            parrentMap.clear();
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
            grid[x2][y2] = fairy;
            found = this.findPath(new Point(x,y),new Point(x2,y2), new Point(x3,y3), grid, path1, path2, parrentMap, history, true);

            if(OCCUR++ > MAX_RECUR_TRIES){
                Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. OCCUR outbeat MAX_RECUR_TRIES");
                return;
            }
        }while(!found);
        grid[x2][y2].setLoc(new Point(x2*32,y2*32));
        history.remove(new Point(x2,y2));
        history.addFirst(new Point(x2,y2));

    }

    //Place Puzle Trap MiniBoss Room

    //this store the edge and the index of the edge (Point, Index)
    List<Point> possiblePoint = new ArrayList<>();
    HashMap<Point, Point> puzzle_map = new HashMap<>();
     void placePTMR(){
        int special = 0;
        for (Room[] rooms : grid){
            for(Room room : rooms){
                if(room==null) continue;
                if(room.type == RoomType.SINGLE && room.conected_room.values().stream().mapToInt(HashSet::size).sum() == 1) {
                    if(special >= MAX_LIMIT.get(RoomType.SPECIAL)) return;
                    room.replace(avaibleRooms.SPECIAL.pop());
                    Bukkit.broadcastMessage("FOUND EDGE!!");
                    special++;
                }
            }
        }
        /**
         * Broke the room into pieces.
         * L -> 2x1
         * BOX -> L
         * 4x1... -> 3x1...
         * */
        if(special < MAX_LIMIT.get(RoomType.SPECIAL)){
            getPointIntegerHashMap();
            for (Point body : possiblePoint){
                if(special >= MAX_LIMIT.get(RoomType.SPECIAL)) return;
                if(grid[body.x][body.y].type == RoomType.FOUR_X_ONE){
                    Bukkit.broadcastMessage("replaced from"+grid[body.x][body.y].type+" to");
                    grid[body.x][body.y].replace(avaibleRooms.THREE.peek().clone());
                    Bukkit.broadcastMessage(String.valueOf(grid[body.x][body.y].type));
                    grid[body.x][body.y].body.remove(body);
                    Point index = grid[body.x][body.y].foundIndexation;
                    Bukkit.broadcastMessage("the indexation of this shape is "+index.toString());
                    Point pastepoint = new Point(index.x*32,index.y*32);

                    for (Point shape : grid[body.x][body.y].body){
                        if (shape == null) break;
                        int dx = -(index.x-shape.x)*16;
                        int dy = -(index.y-shape.y)*16;
                        Bukkit.broadcastMessage("   translating to.. "+dx+" "+dy);
                        pastepoint.translate(dx,dy);
                    }
                    grid[body.x][body.y].setLoc(pastepoint);
                    Bukkit.broadcastMessage(puzzle_map.get(body).toString());

                    grid[body.x][body.y] = avaibleRooms.SPECIAL.pop();
                    grid[body.x][body.y].setLoc(new Point(body.x*32,body.y*32));
                    buildEmtyDoor(puzzle_map,body,grid);
                    Bukkit.broadcastMessage("Same Room? "+grid[body.x][body.y].name);
                }
                if(grid[body.x][body.y].type == RoomType.THREE_X_ONE){
                    Bukkit.broadcastMessage("replaced from"+grid[body.x][body.y].type+" to");
                    grid[body.x][body.y].replace(avaibleRooms.TWO.peek().clone());
                    Bukkit.broadcastMessage(String.valueOf(grid[body.x][body.y].type));
                    grid[body.x][body.y].body.remove(body);
                    Point index = grid[body.x][body.y].foundIndexation;
                    Bukkit.broadcastMessage("the indexation of this shape is "+index.toString());
                    Point pastepoint = new Point(index.x*32,index.y*32);

                    for (Point shape : grid[body.x][body.y].body){
                        if (shape == null) break;
                        int dx = -(index.x-shape.x)*16;
                        int dy = -(index.y-shape.y)*16;
                        Bukkit.broadcastMessage("   translating to.. "+dx+" "+dy);
                        pastepoint.translate(dx,dy);
                    }
                    grid[body.x][body.y].setLoc(pastepoint);
                    Bukkit.broadcastMessage(puzzle_map.get(body).toString());

                    grid[body.x][body.y] = avaibleRooms.SPECIAL.pop();
                    grid[body.x][body.y].setLoc(new Point(body.x*32,body.y*32));
                    buildEmtyDoor(puzzle_map,body,grid);
                    Bukkit.broadcastMessage("Same Room? "+grid[body.x][body.y].name);
                }
                if(grid[body.x][body.y].type == RoomType.TWO_X_ONE){
                    Bukkit.broadcastMessage("replaced from"+grid[body.x][body.y].type+" to");
                    grid[body.x][body.y].replace(avaibleRooms.SINGLE.peek().clone());
                    Bukkit.broadcastMessage(String.valueOf(grid[body.x][body.y].type));
                    grid[body.x][body.y].body.remove(body);
                    Point index = grid[body.x][body.y].foundIndexation;
                    Bukkit.broadcastMessage("the indexation of this shape is "+index.toString());

                    grid[body.x][body.y].setLoc(new Point(index.x*32,index.y*32));
                    Bukkit.broadcastMessage(puzzle_map.get(body).toString());

                    grid[body.x][body.y] = avaibleRooms.SPECIAL.pop();
                    grid[body.x][body.y].setLoc(new Point(body.x*32,body.y*32));
                    buildEmtyDoor(puzzle_map,body,grid);
                    Bukkit.broadcastMessage("Same Room? "+grid[body.x][body.y].name);
                }
                special++;
            }
        }
    }
    int[][] dir = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    Point getNeighbor(int x, int y, Room room){
        List<Point> sementara = new ArrayList<>();
         for (int[] dirs : dir){
             int dx = x + dirs[0];
             int dy = y + dirs[1];

             boolean right = dx >= 0 && dx < grid.length;
             boolean left = dy >= 0 && dy < grid[0].length;
             if(right && left && Objects.equals(grid[dx][dy], room)){
                 sementara.add(new Point(dx, dy));
             }
         }
        if(sementara.size() == 1) return sementara.getLast();
        return null;
    }

    private void getPointIntegerHashMap() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++){
                if(grid[i][j]==null) continue;
                if(grid[i][j].type == RoomType.START || grid[i][j].type == RoomType.MID || grid[i][j].type == RoomType.END || grid[i][j].name == "PATH" || grid[i][j].type == RoomType.SINGLE) continue;

                Point current = new Point(i, j);
                if(grid[i][j].conected_room.containsKey(current)) {
                    Bukkit.broadcastMessage("Room at "+grid[i][j].loc+" has conected grid[i![j");
                    continue;
                }

                Bukkit.broadcastMessage("Getting "+grid[i][j].name);

                Point neighbor = getNeighbor(i, j, grid[i][j]);
                if(neighbor!=null){
                    possiblePoint.add(current);
                    puzzle_map.put(current,neighbor);
                }
                Bukkit.broadcastMessage(current+" to "+getNeighbor(i, j, grid[i][j]));


            }
        }
    }

    //This gona render the dungeon to the actual shape
    private void render(){
        for (Room[] rooms : grid) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Room room : rooms) {
                try{
                    if(room.type == RoomType.SPECIAL || room.type == RoomType.TRAP || room.type == RoomType.PUZZLE || room.type == RoomType.MINI_BOSS){
                        Bukkit.broadcastMessage("special with paste point of "+room.loc.toString());
                    }
                    room.loadScheme();
                    //conected_room.values().stream().mapToInt(HashSet::size).sum()
                    stringBuilder.append(room.logo).append(", ");
                }catch (NullPointerException e){
                    stringBuilder.append(0).append(", ");
                }
            }
            Bukkit.broadcastMessage(stringBuilder.toString());
        }
    }
}
