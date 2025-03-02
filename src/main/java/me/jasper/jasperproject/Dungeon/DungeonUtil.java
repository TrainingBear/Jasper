package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public abstract class DungeonUtil {

    /**
     * BFS function to find the shortest path
     * */
    boolean findPath(Point start, Point fairy, Point end, Room[][] grid, Room pathSchem,Room pathSchem2, Map<Point, Point> parentMap, Stack<Point> history, boolean debug) {
            int rows = grid.length;
            int cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            Queue<Point> queue = new LinkedList<>();

            queue.add(start);
            visited[start.x][start.y] = true;
            boolean foundFairy = false;

            while (!queue.isEmpty()) {
                Point current = queue.poll();

                // Check if fairy is reached
                if (current.equals(fairy) && !foundFairy) {
                    for (boolean[] visit : visited) Arrays.fill(visit,false);
                    visited = recoverVisited(parentMap, fairy, start, visited);
                    queue.clear();
                    current = fairy;
                    foundFairy = true;
                }
                // if fairy found & end found
                if (current.equals(end) && foundFairy) {
                    reconstructPath(parentMap, start, fairy, end, grid, pathSchem, pathSchem2, history);
                    return true;
                }

                // Explore neighbors
                for (int[] direction : directions) {
                    int newX = current.x + direction[0];
                    int newY = current.y + direction[1];
                    Point neighbor = new Point(newX, newY);
                    if (isValid(neighbor, rows, cols, visited, grid)) {
                        queue.add(neighbor);
                        visited[newX][newY] = true;
                        parentMap.put(neighbor, current);
                    }
                }
            }
            if(debug){
                Bukkit.broadcastMessage("Target not found");
            }
            return false;
        }
    boolean[][] recoverVisited(Map<Point, Point> parrentMap, Point end, Point start, boolean[][] visited){
        Point step = end;
        visited[step.x][step.y] = true;
        do{
            step = parrentMap.get(step);
            visited[step.x][step.y] = true;
        }while (!step.equals(start));
        return visited;
    }
    private boolean isValid(Point p, int rows, int cols, boolean[][] visited, Room[][] grid) {
        return (p.x >= 0 && p.x < rows && p.y >= 0 && p.y < cols) &&
                !visited[p.x][p.y] && (grid[p.x][p.y] == null || grid[p.x][p.y].ID == 3 || grid[p.x][p.y].ID == 2);
    }
    private void reconstructPath(Map<Point, Point> parentMap, Point start,Point fairy, Point end, Room[][] grid, Room pathid, Room pathid2, Stack<Point> history) {
        Point step = end;
        grid[end.x][end.y].setRotation(wichDirection(end, parentMap.get(end)));
        Point key = end;
        Room room = pathid;
        while (!step.equals(start)) {
            step = parentMap.get(key);

            if(step.equals(start)){
                grid[step.x][step.y].setRotation(wichDirection(step, key));

            }

            if(!step.equals(start) && !step.equals(fairy)){
                grid[step.x][step.y] = room;
                grid[step.x][step.y].setLoc(new Point(step.x * 32, step.y * 32));
                history.add(new Point(step.x, step.y));
            }
            key = step;
            if(key.equals(fairy)){
                room = pathid2;
            }
        }
    }

    void buildDoor(Map<Point, Point> parentMap, Point start, Point end, Room[][] grid) {
        Point step = end;
        Room d1, d2;
        Point pre_step, transition;
        int rotation;
        Bukkit.broadcastMessage(ChatColor.RED+"Building Locked Door..");
        while (!step.equals(start)) {
            pre_step = step;
            d1 = grid[pre_step.x][pre_step.y];

            step = parentMap.get(step);
            d2 = grid[step.x][step.y];

            transition = new Point(-(pre_step.x - step.x)*16, -(pre_step.y - step.y)*16);
            rotation = transition.x==0? 0 : 90;

            if(!Objects.equals(d1, d2)){
                grid[pre_step.x][pre_step.y].addConection(pre_step,step);
//                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+ "    "+grid[pre_step.x][pre_step.y].name+" --> "+grid[step.x][step.y].name+" = "+grid[pre_step.x][pre_step.y].conected_room.values().stream().mapToInt(HashSet::size).sum());
                grid[step.x][step.y].addConection(step,pre_step);
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+ "    "+grid[step.x][step.y].name+" --> "+grid[pre_step.x][pre_step.y].name+" = "+grid[step.x][step.y].conected_room.values().stream().mapToInt(HashSet::size).sum());
                this.loadAndPasteSchematic("lockeddoor",
                        new BlockVector3((pre_step.x*32)+transition.x,
                                70,(pre_step.y*32)+ transition.y),rotation, false);
            }
        }
    }
    void buildEmtyDoor(Map<Point, Point> parentMapOri, Point end, Room[][] grid) {
        Point step = end;
        Room d1, d2;
        Point pre_step, transition;
        int rotation;
        Map<Point, Point> parentMap = new HashMap<>(Map.copyOf(parentMapOri));

        if(grid[end.x][end.y].ID==0){
            grid[end.x][end.y].setRotation(wichDirection(end, parentMap.get(end)));
        }
        while (!parentMap.isEmpty()) {
            pre_step = step;
            d1 = grid[pre_step.x][pre_step.y];

            step = parentMap.remove(step);
            if(step == null){
                return;
            }
            d2 = grid[step.x][step.y];

            transition = new Point(-(pre_step.x - step.x)*16, -(pre_step.y - step.y)*16);
            rotation = transition.x==0? 0 : 90;

            if(!Objects.equals(d1, d2)){
                this.loadAndPasteSchematic("door",
                    new BlockVector3((pre_step.x*32)+ transition.x,
                            70,(pre_step.y*32)+ transition.y),rotation, false);
            }
        }
    }
    /**
     * @param currentRoom Currnet Object.
     * @param neightboor Object yang akan di hadap.
     * */
    private int wichDirection(Point currentRoom, Point neightboor){
        int dx,dy;
        dx = neightboor.x - currentRoom.x;
        dy = neightboor.y - currentRoom.y;
        if(dy==-1) return -90;
        if(dx==1) return 180;
        if(dy==1) return 90;
        return 0;
    }

    /**
     * Finding Random direction and define the shape,
     *  also fill the grid after path found.
     */
    void random_dir(Rooms avaibleRooms, Generator G, Queue<Point> endpoint, Map<Point,Point> parrentMap, boolean ignoreLimit) {
        int[][] directions;
        int dir;
        while(!G.history.isEmpty()){
            Point current = getValidStartPos(G.grid,G.history);


            directions = getDir(G.grid,current);
            do{
                dir = G.random.nextInt(directions.length);
            }while (directions[dir]==null && (directions[0]!=null || directions[1]!=null || directions[2]!=null || directions[3]!=null));

            if(directions[dir]==null){
                break;
            }

            int dx = current.x + directions[dir][0];
            int dy = current.y + directions[dir][1];
            Point neighbor = new Point(dx,dy);
            if(isValid(G.grid,neighbor)){
                boolean defined = defineRoom(avaibleRooms, G, dx, dy,null, false, ignoreLimit);
                if(!defined){
                    Bukkit.broadcastMessage(ChatColor.RED+"ran out of shape!");
                    break;
                }

                endpoint.add(new Point(dx,dy));
                G.grid[current.x][current.y].addConection(current, neighbor);
                G.grid[neighbor.x][neighbor.y].addConection(neighbor, current);
                parrentMap.put(neighbor, current);
            }if(!hasDir(neighbor.x, neighbor.y, G.grid)){
                neighbor = getValidStartPos(G.grid, G.history);
                if(neighbor!=null){
                    G.history.add(neighbor);
                }
            }


        }
    }
    private Point getValidStartPos(Room[][] grid, Stack<Point> historyori) {
        Point point;
        int x,y;
        Stack<Point> history = new Stack<>();
        history = historyori;
        while (!history.isEmpty()){
            point = history.pop();
            x = point.x;
            y = point.y;
            if(hasDir(x,y,grid)){
                return point;
            }
        }
        return null;
    }
    private boolean hasDir(int i, int j, Room[][] grid){
        return  ((i+1 < grid.length) &&  grid[i+1][j] == null) ||
                ((i-1 >= 0) &&  grid[i-1][j] == null) ||
                ((j+1 < grid[i].length) &&  grid[i][j+1] == null) ||
                ((j-1 >= 0) &&  grid[i][j-1] == null);
    }
    private int[][] getDir(Room[][] grid,Point point){
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        int i = point.x;
        int j = point.y;
        boolean bawah,atas,kanan,kiri;
        bawah = (i+1 < grid.length) &&  grid[i+1][j]==null;
        atas = (i-1 >= 0) &&  grid[i-1][j]==null;
        kanan = (j+1 < grid[i].length) &&  grid[i][j+1]==null;
        kiri = (j-1 >= 0) &&  grid[i][j-1]==null;
        int[][] l = new int[4][];
        if(bawah) { l[0] =directions[0]; }
        if(atas) { l[1] = directions[1];}
        if(kanan) { l[2] = directions[2];}
        if(kiri) { l[3] = directions[3];}
        return l;
    }
    private boolean isValid(Room[][] grid,Point n){
        return ( n.x >= 0 && n.x < grid.length) &&
                (n.y >= 0 && n.y < grid[0].length) && grid[n.x][n.y] == null;
    }

    protected void loadAndPasteSchematic(String fileName, BlockVector3 location, int rotationDegrees, boolean ignoreAir) {
        File file = new File("C:\\Users\\user\\AppData\\Roaming\\.feather\\player-server\\servers\\7a1e3607-139e-4341-a6b9-6340739908da\\plugins\\WorldEdit\\schematics\\" + fileName + ".schem");

        if (!file.exists()) {
            Bukkit.broadcastMessage(fileName+" file not found.");
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            Bukkit.broadcastMessage("Invalid schematic format.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             ClipboardReader reader = format.getReader(fis)) {
            Clipboard clipboard = reader.read();

            //rotating
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            AffineTransform transform = new AffineTransform();
            transform = transform.rotateY(rotationDegrees);
            holder.setTransform(holder.getTransform().combine(transform));

            try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                    .world(BukkitAdapter.adapt(Bukkit.getWorld("test")))
                    .build()) {
                Operation operation = holder.createPaste(editSession)
                        .to(location)
                        .ignoreAirBlocks(ignoreAir)
                        .build();

                Operations.complete(operation);
//                Bukkit.broadcastMessage("Schematic "+fileName+" pasted with a " + rotationDegrees + "° rotation! at: "+location.toString());
            }
        } catch (IOException | WorldEditException e) {
            Bukkit.broadcastMessage("Failed to load or paste schematic: " + e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     * Define point of x & y to random shape,
     * with the possible of direction of the point.
     * @param i is the x.
     * @param j is the y.
     * @param g is the Generator.java.
     * */
    boolean defineRoom(Rooms avaibleRooms, Generator g , int i, int j, Room room, boolean debug, boolean ignoreLimit) {

        LinkedList<RoomShape> pick = new LinkedList<>(List.of(RoomShape.values()));
        pick.removeLast();
        Collections.shuffle(pick, g.random);
        pick.addLast(RoomShape.ONE);


        while (!pick.isEmpty()){
            RoomShape shapes = pick.pop();
            if(isFit(avaibleRooms, g , i, j, shapes, room, debug)){
//                Bukkit.broadcastMessage("Found shape! at "+ grid[i][j].loc.x +", "+grid[i][j].loc.y);
                return true;
            }
        }
        return false;
    }
    private boolean isFit(Rooms avaibleRooms,Generator G, int i, int j, RoomShape shapes,  Room room, boolean debug) {
        HashMap<RoomType, LinkedList<Room>> avb = new HashMap(Map.of(
                RoomType.TWO_X_ONE, avaibleRooms.TWO,
                RoomType.THREE_X_ONE, avaibleRooms.THREE,
                RoomType.FOUR_X_ONE, avaibleRooms.FOUR,
                RoomType.BOX, avaibleRooms.BOX,
                RoomType.L_SHAPE, avaibleRooms.L,
                RoomType.SINGLE, avaibleRooms.SINGLE
        ));
        Point pastepoint = new Point(i*32, j*32);
        int x = 0, y = 0, dx, dy;
        int lx, ly;
        boolean valid = true;
        int rotation = 1;

        if(G.CURRENT_LIMIT.get(shapes.type) >= G.MAX_LIMIT.get(shapes.type)){
            if(debug){
                Bukkit.broadcastMessage("REACHED MAX_LIMIT");
            }
            return false;
        }
        for (int k = 0; k < shapes.shape.length; k++) {
            valid = true;
            for (int l = 0; l < shapes.shape[k].length; l++) {
                    x = i + shapes.shape[k][l][0];
                    y = j + shapes.shape[k][l][1];
                    if (!isValid(G.grid, x, y, room)) {
                        valid = false;
                        break;
                    }
            }
            if(valid) {
                G.CURRENT_LIMIT.put(shapes.type,G.CURRENT_LIMIT.get(shapes.type)+1);
                Room validRoom = avb.get(shapes.type).peek().clone();
                validRoom.setName(validRoom.name+"_"+k);
                validRoom.setFoundIndexation(new Point(i, j));
                    Bukkit.broadcastMessage(validRoom.name+" Shape Found at "+i+", "+j+" with rot of "+k);

                int[][][] copyOfShape = shapes.copyOfShape;
//                if(room != null){
//                    validRoom.setLogo(room.logo);
//                }
                for (int l = 0; l < shapes.shape[k].length; l++) {
                    if(validRoom.type == RoomType.BOX){
                        int m = l == 0? l: l-1;
                        x = i + copyOfShape[k][m][0];
                        y = j + copyOfShape[k][m][1];
                    }else {
                        x = i + copyOfShape[k][l][0];
                        y = j + copyOfShape[k][l][1];
                    }

                    lx = i + shapes.shape[k][l][0];
                    ly = j + shapes.shape[k][l][1];

                    dx = -(i-x)*16;
                    dy = -(j-y)*16;

                    pastepoint.translate(dx,dy);
                    if(debug){
                        Bukkit.broadcastMessage(" -translated to "+dx+", "+dy);
                    }

                    G.grid[lx][ly] = validRoom;
                    G.history.add(new Point(lx,ly));

                    validRoom.body.add(new Point(lx,ly));
                }
                validRoom.setLoc(pastepoint);
                validRoom.setRotation((rotation+2)*90);
                Bukkit.broadcastMessage("Successful load the shape!");
                return valid;
            }
            rotation++;
        }

        return valid;
    }

    protected boolean isFit(Rooms avaibleRooms,Generator G, int i, int j, RoomShape shapes,  Room room, boolean debug, boolean ignoreLimit, int rot) {
        Random random = new Random(354657233254L);
        HashMap<RoomType, LinkedList<Room>> avb = new HashMap(Map.of(
                RoomType.TWO_X_ONE, avaibleRooms.TWO,
                RoomType.THREE_X_ONE, avaibleRooms.THREE,
                RoomType.FOUR_X_ONE, avaibleRooms.FOUR,
                RoomType.BOX, avaibleRooms.BOX,
                RoomType.L_SHAPE, avaibleRooms.L,
                RoomType.SINGLE, avaibleRooms.SINGLE
        ));
        Point pastepoint = new Point(i*32, j*32);
        int x = 0, y = 0, dx, dy;
        int lx, ly;
        boolean valid = true;
        int rotation = rot;

        if(G.CURRENT_LIMIT.get(shapes.type) >= G.MAX_LIMIT.get(shapes.type) && !ignoreLimit){
            if(debug){
                Bukkit.broadcastMessage("REACHED MAX_LIMIT");
            }
            return false;
        }
        for (int k = 0; k < shapes.shape.length; k++) {
            valid = true;
            for (int l = 0; l < shapes.shape[k].length; l++) {
                    x = i + shapes.shape[k][l][0];
                    y = j + shapes.shape[k][l][1];
                    if (!isValid(G.grid, x, y, room)) {
                        valid = false;
                        break;
                    }
            }
            if(valid) {
                G.CURRENT_LIMIT.put(shapes.type,G.CURRENT_LIMIT.get(shapes.type)+1);
                Room validRoom = avb.get(shapes.type).peek().clone();
                validRoom.setName(validRoom.name+"_"+k+"_"+random.nextInt(100));
                validRoom.setFoundIndexation(new Point(i, j));

                if(debug){
                    Bukkit.broadcastMessage(validRoom.name+" Shape Found at "+i+", "+j+" with rot of "+k);
                }
                int[][][] copyOfShape = shapes.copyOfShape;
                if(room != null){
                    validRoom.setLogo(room.logo);
                }
                for (int l = 0; l < shapes.shape[k].length; l++) {
                    x = i + copyOfShape[k][l][0];
                    y = j + copyOfShape[k][l][1];

                    lx = i + shapes.shape[k][l][0];
                    ly = j + shapes.shape[k][l][1];

                    dx = -(i-x)*16;
                    dy = -(j-y)*16;

                    pastepoint.translate(dx,dy);
                    if(debug){
                        Bukkit.broadcastMessage(" -translated to "+dx+", "+dy);
                    }

                    G.grid[lx][ly] = validRoom;
                    validRoom.body.add(new Point(lx,ly));
                }
                validRoom.setLoc(pastepoint);
                validRoom.setRotation((rotation+2)*90);
                return valid;
            }
            rotation++;
        }

        return valid;
    }
    boolean isValid(Room[][] grid, int x, int y, Room room){
            if(room == null){
                return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == null;
            }
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length &&
                (grid[x][y] == null  || grid[x][y] == room);
    }
}