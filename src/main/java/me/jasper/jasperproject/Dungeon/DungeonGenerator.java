package me.jasper.jasperproject.Dungeon;

import lombok.Getter;
import me.jasper.jasperproject.Dungeon.Shapes.*;
import me.jasper.jasperproject.Dungeon.Shapes.Shape;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import me.jasper.jasperproject.Util.TookTimer;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Getter
public abstract class DungeonGenerator {
    private static final AtomicInteger atom = new AtomicInteger();
    protected final static Set<String> instances = new HashSet<>();
    protected final String instance_key;
    private long seed = new Random().nextLong();
    private int p = 4;
    private int l = 4;
    int MAX_RECUR_TRIES = 1000;
    int OCCUR = 0;
    private final DungeonHandler handler;
    private final DungeonMap map;

    public DungeonGenerator(int p, int l){
        this.p = p; this.l = l;
        this.handler = new DungeonHandler(p, l, this.seed);
        this.map = new DungeonMap(this);
        this.instance_key = "dungeon_instance-"+atom.getAndIncrement();
    }
    public DungeonGenerator(int p, int l, long seed){
        this.p = p; this.l = l; this.seed=seed;
        this.handler = new DungeonHandler(p, l, seed);
        this.map = new DungeonMap(this);
        this.instance_key = "dungeon_instance-"+atom.getAndIncrement();
    }
    public DungeonGenerator() {
        this.handler = new DungeonHandler(p, l, seed);
        this.map = new DungeonMap(this);
        this.instance_key = "dungeon_instance-"+atom.getAndIncrement();
    }

    /// THE MAIN METHOD
    public void generate(){
        initialize(handler);
        generateWorld();
        Bukkit.broadcast(Util.deserialize("Dungeon seed [<v>]", Placeholder.component("v", Component.text(seed).color(NamedTextColor.GREEN))).color(NamedTextColor.WHITE).hoverEvent(HoverEvent.showText(Component.text("Click to copy!"))).clickEvent(ClickEvent.copyToClipboard(String.valueOf(seed))));
        TookTimer.run("Initializing main", ()->{
            placeMainDungeon();
            reconstructPath(handler, (p, r) -> this.defineRoom(handler, p, true, r));
            handler.setMainInitialized(true);
        });
        buildDoor();
        TookTimer.run("Fill room",()->{
            fill(handler, true);/// fill the empty space
        });
        for (Point end : handler.getEdge()){
            buildEmptyDoor(end);
        }
        TookTimer.run("Rematch special room", this::placePTMR);
        TookTimer.run("Render Dungeon", () -> {
            handler.setDebug_mode(true);
            render();
            handler.setDebug_mode(false);
        });
        TookTimer.run("Render Dungeon Map", () -> {
            map.loadRoom();
        });
    }

    private void generateWorld(){
        NamespacedKey namespacedKey = new NamespacedKey(JasperProject.getPlugin(), instance_key);
        WorldCreator worldCreator = new WorldCreator(instance_key, namespacedKey);
        worldCreator.generator(new ChunkGenerator() {
        });
        Bukkit.createWorld(worldCreator);
        DungeonGenerator.instances.add(instance_key);
    }

    public boolean closeWorld(){
        World world = Bukkit.getWorld(instance_key);
        if(world==null) return false;
        boolean unloaded = Bukkit.unloadWorld(world, false);
        if(unloaded){
            instances.remove(instance_key);
            Bukkit.broadcast(Component.text(instance_key+" has been deleted!").color(NamedTextColor.GREEN));
            return world.getWorldFolder().delete();
        }
        return false;
    }

    public abstract void initialize(DungeonHandler handler);

    /// Place START, MID, END & Generate its path
    private void placeMainDungeon(){
        double distance1,distance2,distance3;
        Random random = handler.getRandom();
        Room[][] grid = handler.getGrid();
        Map<Point, Point> roomMap = handler.getRoomMap();
        Point start;
        Point end;
        Point mid;
        do{
            int x = random.nextInt(p);
            int y = random.nextInt(l);
            start = new Point(x, y);
            int x3 = random.nextInt(p);
            int y3 = random.nextInt(l);
            end = new Point(x3, y3);
            distance3 = start.distance(end);
            if(OCCUR++ > MAX_RECUR_TRIES){
                Bukkit.broadcast(Util.deserialize("Something when loading the main instance.").color(NamedTextColor.RED));
                return;
            }
        }while (distance3 < p-1);
        handler.setEntrance(start);
        handler.setBloodRoom(end);

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
                int x2 = random.nextInt(p);
                int y2 = random.nextInt(l);
                mid = new Point(x2, y2);
                distance1 = start.distance(mid);
                distance2 = mid.distance(end);
                if(OCCUR++ > MAX_RECUR_TRIES){
                    Bukkit.broadcast(Util.deserialize("Failed to load Dungeon.").color(NamedTextColor.RED));
                    return;
                }
            } while (handler.getGrid(mid) !=null && distance1 < ((double) p /2)-((double) p /4)+1 || distance2 < ((double) p /2)-((double) p /4)+1);
            prevx = mid.x;
            prevy = mid.y;
            handler.setFairy(mid);
            found = this.findPath(handler);
            if(OCCUR++ > MAX_RECUR_TRIES){
                Bukkit.broadcast(Util.deserialize("Failed to load Dungeon.").color(NamedTextColor.RED));
                return;
            }
        }while(!found);
        handler.setFairy(mid);
    }

    /// this store the edge and the index of the edge (Point, Index)
    private final List<Point> possiblePoint = new ArrayList<>();
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
                         buildEmptyDoor(point);
                     }
                     continue;
                 }

                 if(grid[i][j].getType() .equals(RoomType.SINGLE ) && grid[i][j].getConnected_room().values().stream().mapToInt(HashSet::size).sum() == 1) {
                     grid[i][j].replace(avaibleRooms.pop(), false);
                 }
             }

        /// Broke the room into pieces. | L -> 2x1 | BOX -> L | 4x1... -> 3x1...
        if(!avaibleRooms.isEmpty()){
            loadPossiblePoint();
            for (Point body : possiblePoint){
                if(avaibleRooms.isEmpty()) return;
                Shape shape = getShape(body, grid);
                endpoint.add(body);
                Point point = doors.get(body);
                grid[body.x][body.y] = avaibleRooms.pop();
                grid[body.x][body.y].addBody(body);
                grid[body.x][body.y].setLoc(body);

                isFit(point, shape, handler.getGrid(point));

                grid[body.x][body.y].addConnection(body,point);
                grid[point.x][point.y].addConnection(point,body);
                Bukkit.broadcast(Component.text(grid[body.x][body.y].getName()+" -> "+grid[point.x][point.y].getName()).color(NamedTextColor.GOLD));
                buildEmptyDoor(body);
            }
        }
    }

    private static Shape getShape(@NotNull Point body, @NotNull Room[][] grid) {
        Shape shape = null;
        if (grid[body.x][body.y].getType().equals(RoomType.L_SHAPE)) shape = new TOW_BY_TWO();
        if (grid[body.x][body.y].getType().equals(RoomType.BOX)) shape = new L_BY_L();
        if (grid[body.x][body.y].getType().equals(RoomType.FOUR_X_ONE)) shape = new THREE_BY_THREE();
        if (grid[body.x][body.y].getType().equals(RoomType.THREE_X_ONE)) shape = new TOW_BY_TWO();
        if (grid[body.x][body.y].getType().equals(RoomType.TWO_X_ONE)) shape = new ONE_BY_ONE();
        return shape;
    }

    int[][] dir = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    int[][] diagonal_dir = {{1,1}, {-1,-1}, {1,-1}, {-1,1}};

    HashSet<Point> neighborlist = new HashSet<>();
    private @Nullable Point getNeighbor(Point point, @Nullable Room room, boolean isBox){
        List<Point> connected_point_counter = new ArrayList<>();
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
                if(right && left && grid[dx][dy] != null) {
                    Room room_ = grid[dx][dy];
                    RoomType type = room_.getType();

                    if (type.equals(RoomType.L_SHAPE) ||
                    type.equals(RoomType.SINGLE) ||
                    type.equals(RoomType.TWO_X_ONE) ||
                    type.equals(RoomType.THREE_X_ONE) ||
                    type.equals(RoomType.FOUR_X_ONE) ||
                    type.equals(RoomType.BOX)) return new Point(dx, dy);
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
                    room.addConnection(new Point(dx, dy), new Point(x, y));
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
                 connected_point_counter.add(new Point(dx, dy));
             }
         }
        return connected_point_counter.size() == 1 ? connected_point_counter.getLast() : null;

    }

    private void loadPossiblePoint() {
        Room[][] grid = handler.getGrid();
        Map<Point, Point> doors = handler.getDoorMap();
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++){
                if(grid[i][j] == null) continue;
                if(grid[i][j].getType() == RoomType.START || grid[i][j].getType() == RoomType.MID || grid[i][j].getType() == RoomType.END || grid[i][j].getName().equals("PATH" ) || grid[i][j].getType() == RoomType.SINGLE) continue;
                Point current = new Point(i, j);
                Point neighbor;

                if(grid[i][j].getConnected_room().containsKey(current)) continue;
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

    void buildDoor() {
        Map<Point, Point> parentMap = handler.getRoomMap();
        Point start = handler.getEntrance();
        Room[][] grid = handler.getGrid();

        Point step = handler.getBloodRoom();
        grid[step.x][step.y].setRotation(whichDirection(step, parentMap.get(step)));
        Room d1, d2;
        Point pre_step, transition;
        int rotation;
        while (!step.equals(start)) {
            pre_step = step;
            d1 = grid[pre_step.x][pre_step.y];

            step = parentMap.get(step);
            if(step==null) return;
            d2 = grid[step.x][step.y];

            transition = new Point(-(pre_step.x - step.x)*16, -(pre_step.y - step.y)*16);
            rotation = transition.x==0? 0 : 90;

            if(!d1.equals(d2)){
                grid[pre_step.x][pre_step.y].addConnection(pre_step,step);
                grid[step.x][step.y].addConnection(step,pre_step);
                Location location = new Location(Bukkit.getWorld(instance_key), (pre_step.x * 32) + transition.x,
                        65, (pre_step.y * 32) + transition.y);
                Room room = handler.getGrid(pre_step);
                File door = new File(room.getSchema_path()+"//"+room.getName()+"//"+room.getName()+"_door_locked.schem");
                File sub_door = new File(JasperProject.getDungeonConfig().getParent(), "//locked_door.schem");
                Structure.renderWFawe(door.exists()? door : sub_door, location, null, rotation);
            }
        }
    }

    /// This going to render the dungeon to the actual shape
    private void render(){
        Bukkit.getScheduler().runTask(JasperProject.getPlugin(), () -> {
            for (Room[] rooms : handler.getGrid()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Room room : rooms) {
                    try{
                        room.loadScheme(handler.isDebug_mode(), instance_key);
                        stringBuilder.append(room.getLogo()).append(", ");
                    }catch (NullPointerException e){
                        stringBuilder.append(0).append(", ");
                    }
                }
                if(handler.isDebug_mode()) Bukkit.broadcast(Util.deserialize(stringBuilder.toString()).color(NamedTextColor.YELLOW));
            }
        });
    }
    /**
     * BFS function to find the shortest path
     * @return true if found
     * */
    boolean findPath(DungeonHandler handler) {
        Room[][] grid = handler.getGrid();
        Point start = handler.getEntrance();
        Point fairy = handler.getFairy();
        Point end = handler.getBloodRoom();
        Map<Point, Point> roomMap = handler.getRoomMap();

        boolean[][] visited = new boolean[grid.length][grid[0].length];
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Queue<Point> queue = new LinkedList<>();

        queue.add(start);
        visited[start.x][start.y] = true;
        boolean foundFairy = false;
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if(handler.isDebug_mode()) Bukkit.broadcast(Util.deserialize("current ("+current.x+", "+current.y+") -> "+(handler.getGrid(current) == null? "null" : handler.getGrid(current).getName())).color(NamedTextColor.YELLOW));
            /// Check if fairy is reached
            if (current.equals(fairy) && !foundFairy) {
                for (boolean[] visit : visited) Arrays.fill(visit,false);
                visited = recoverVisited(roomMap, current, start, visited);
                queue.clear();
                current = fairy;
                foundFairy = true;
                if(handler.isDebug_mode()) Bukkit.broadcast(Util.deserialize("Fairy has been found!").color(NamedTextColor.GREEN));
            }
            if (current.equals(end) && foundFairy) {
                reconstructPath(handler);
                if(handler.isDebug_mode()) Bukkit.broadcast(Util.deserialize("Blood finished!").color(NamedTextColor.GREEN));
                return true;
            }
            // Explore neighbors
            for (int[] direction : directions) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];
                Point neighbor = new Point(newX, newY);
                if (isValid(neighbor, grid.length, grid[0].length, visited, grid)) {
                    queue.add(neighbor);
                    visited[newX][newY] = true;
                    roomMap.put(neighbor, current);
                }
            }
        }
        if(handler.isDebug_mode()) {
            Bukkit.broadcast(Util.deserialize("Target not found").color(NamedTextColor.RED));
            Bukkit.broadcast(Util.deserialize(start.toString()).color(NamedTextColor.RED));
            Bukkit.broadcast(Util.deserialize(fairy.toString()).color(NamedTextColor.RED));
            Bukkit.broadcast(Util.deserialize(end.toString()).color(NamedTextColor.RED));
        }
        return false;
    }
    boolean[][] recoverVisited(Map<Point, Point> parrentMap, Point end, Point start, boolean[][] visited){
        Point step = end;
        visited[step.x][step.y] = true;
        do{
            step = parrentMap.get(step);
            if(step==null) return visited;
            visited[step.x][step.y] = true;
        }while (!step.equals(start));
        return visited;
    }
    private boolean isValid(Point p, int rows, int cols, boolean[][] visited, Room[][] grid) {
        return (p.x >= 0 && p.x < rows && p.y >= 0 && p.y < cols) &&
                !visited[p.x][p.y] && (grid[p.x][p.y] == null || grid[p.x][p.y].getType().equals(RoomType.MID) || grid[p.x][p.y].getType().equals(RoomType.END));
    }
    private void reconstructPath(DungeonHandler handler) {
        reconstructPath(handler,null);
    }
    public void reconstructPath(DungeonHandler handler, @org.jetbrains.annotations.Nullable BiConsumer<Point, Room> consumer) {
        Point end = handler.getBloodRoom();
        Point start = handler.getEntrance();
        Point fairy = handler.getFairy();
        Room[][] grid = handler.getGrid();
        Stack<Point> history = handler.getHistory();
        Map<Point, Point> parentMap = handler.getRoomMap();

        Point step = end;
        grid[end.x][end.y].setRotation(whichDirection(end, parentMap.get(end)));
        Point key = end;
        Room room = CreatedRoom.path1;
        while (!step.equals(start)) {
            step = parentMap.get(key);

            if(!step.equals(start) && !step.equals(fairy)){
                if(consumer!=null){
                    consumer.accept(step, room);
                }else {
                    grid[step.x][step.y] = room;
                    grid[step.x][step.y].setLoc((Point) step.clone());
                    history.push((Point) step.clone());
                }
            }
            if(step.equals(start)){
                grid[step.x][step.y].setRotation(whichDirection(step, key));
            }
            key = step;
            if(key.equals(fairy)){
                room = CreatedRoom.path2;
            }
        }
    }


    void buildEmptyDoor(Point end) {
        Room[][] grid = handler.getGrid();
        Map<Point, Point> doors = handler.getDoorMap();
        if(grid[end.x][end.y].isSingleDoor()){
            grid[end.x][end.y].setRotation(whichDirection(end, doors.get(end)));
        }
        Room room1 = grid[end.x][end.y];
        Point nd = doors.get(end); if(nd == null) return;
        Room room2 = grid[nd.x][nd.y];
        Point transition = new Point(-(nd.x - end.x)*16, -(nd.y - end.y)*16);
        int rotation = transition.x==0? 0 : 90;
        if(!room1.equals(room2)){
            Location location = new Location(Bukkit.getWorld(instance_key), (nd.x * 32) + transition.x,
                    65, (nd.y * 32) + transition.y);
            Room room = handler.getGrid(end);
            File door = new File(room.getSchema_path()+"//"+room.getName()+"//"+room.getName()+"_door.schem");
            File sub_door = new File(JasperProject.getDungeonConfig().getParent(), "//door.schem");
            Structure.renderWFawe(door.exists()? door : sub_door, location, null, rotation);
        }
    }
    /**
     * @param currentRoom Current Object.
     * @param neighbor Object yang akan di hadap.
     * */
    protected int whichDirection(Point currentRoom, Point neighbor){
        int dx,dy;
        dx = neighbor.x - currentRoom.x;
        dy = neighbor.y - currentRoom.y;
        if(dy==-1) return 90;
        if(dx==1) return 180;
        if(dy==1) return -90;
        return 0;
    }

    /// Fill the empty room randomly
    void fill(DungeonHandler handler, boolean ignoreLimit) {
        Stack<Point> history = handler.getHistory();
        Room[][] grid = handler.getGrid();
        Queue<Point> edge = handler.getEdge();
        Map<Point, Point> roomMap = handler.getDoorMap();
        history.remove(handler.getFairy());
        history.push(handler.getFairy());

        int[][] directions;
        int dir;
        while(!history.isEmpty()){
            directions = getValidStartPos(grid, history);
            if(directions==null) { return; }
            Point current = history.pop();
            if(handler.isDebug_mode()) Bukkit.broadcast(Component.text("defining "+handler.getGrid(current).getName()));
            do dir = handler.getRandom().nextInt(directions.length);
            while (directions[dir]==null && (directions[0]!=null || directions[1]!=null || directions[2]!=null || directions[3]!=null));
            if(directions[dir]==null) continue;

            int dx = current.x + directions[dir][0];
            int dy = current.y + directions[dir][1];
            Point neighbor = new Point(dx,dy);
            if(isValid(neighbor, grid)){
                if(!defineRoom(handler, neighbor, true, null)){
                    if(handler.isDebug_mode()) Bukkit.broadcast(Component.text("ran out of shape!").color(NamedTextColor.RED));
                    return;
                }
                if(handler.isDebug_mode()) {
                    String name = grid[dx][dy]!=null? grid[dx][dy].getName() : "null";
                    Bukkit.broadcast(Util.deserialize("Defined "+ name +" at "+dx+", "+dy));
                }
                edge.add(neighbor);
                grid[current.x][current.y].addConnection(current, neighbor);
                grid[dx][dy].addConnection(neighbor, current);
                roomMap.put(neighbor, current);
                history.push(current);
            }
        }
    }

    private int[][] getValidStartPos(Room[][] grid, Stack<Point> history) {
        Point point;
        while (!history.isEmpty()){
            point = history.pop();
            int[][] direction = getDirection(point, grid);
            if(direction!=null){
                history.push(point);
                return direction;
            }
        }
        return null;
    }

    private int[][] getDirection(Point point, Room[][] grid){
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        int[][] l = new int[4][];
        int i=0;
        for (int[] ints : directions) {
            int dx = point.x + ints[0];
            int dy = point.y + ints[1];
            if((((dx < grid.length) && dx >= 0) && ((dy < grid[0].length) && dy >= 0))
            && grid[dx][dy]==null)
                l[++i] = ints;
        }
        return l;
    }

    private boolean isValid(Point n, Room[][] grid){
        return ( n.x >= 0 && n.x < grid.length) &&
                (n.y >= 0 && n.y < grid[0].length) && grid[n.x][n.y] == null;
    }

    boolean defineRoom(DungeonHandler handler, Point point, boolean ignoreLimit, @org.jetbrains.annotations.Nullable Room exception) {
        LinkedList<Shape> pick = new LinkedList<>(List.of(
                new BOX_BY_BOX(), new L_BY_L(), new FOUR_BY_FOUR(),
                new THREE_BY_THREE(), new TOW_BY_TWO()
        ));
        Collections.shuffle(pick, handler.getRandom());
        pick.addLast(new ONE_BY_ONE());
        while (!pick.isEmpty()){
            Shape shape = pick.pop();
            if(isFit(point, shape, exception)) return true;
        }
        return false;
    }

    boolean isFit(Point point, Shape shapes, @org.jetbrains.annotations.Nullable Room exception) {
        int x = point.x, y = point.y;
        Room[][] grid = handler.getGrid();
        Stack<Point> history = handler.getHistory();

        byte[][][] shape = shapes.getShape();
        int anchor = 0;
        boolean valid = false;
        for (int i = 0; i < shape.length; i++) { ///  i = the anchor
            valid = true;
            for (int rot = 1; rot < 5; rot++) { /// rotate every anchor
                shapes.rotate(i);
                valid = true;
                for (byte[] bytes : shape[i]) { /// re-check if valid
                    if (!isValid(x + bytes[0], y + bytes[1], grid, exception)) {
                        valid = false;
                        break;
                    }
                }
                if (valid) break;
            }
            anchor = i;
            if (valid) break;
        }
        if (!valid) return false;
        char logo = '`';
        if (grid[x][y] != null) {
            logo = grid[x][y].getLogo();
        }
        Room valid_room = handler.getRooms().get(shapes.getType()).getFirst().clone();;
        if(handler.isDebug_mode()) Bukkit.broadcast(Util.deserialize("Found "+valid_room.getName()+" at "+x+", "+y));
        valid_room.setRotation(shapes.getRotation().get(anchor));
        valid_room.setLoc(new Point(x, y));
        valid_room.setLocTranslate(shapes.getPastePoint(anchor));
        if(logo!='`') valid_room.setLogo(logo);
        for (byte[] bytes : shape[anchor]) {
            grid[x + bytes[0]][y + bytes[1]] = valid_room;
            Point p = new Point(x + bytes[0], y + bytes[1]);
            history.push(p);
            valid_room.addBody(p);
        }
        return true;
    }
    boolean isValid(int x, int y, Room[][] grid, @org.jetbrains.annotations.Nullable Room room){
        if(room == null) return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == null;
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && (grid[x][y] == room);
    }
}