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
import me.jasper.jasperproject.Dungeon.Shapes.*;
import me.jasper.jasperproject.Dungeon.Shapes.Shape;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public abstract class DungeonUtil {

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
                !visited[p.x][p.y] && (grid[p.x][p.y] == null || grid[p.x][p.y].getID() == 3 || grid[p.x][p.y].getID() == 2);
    }
    private void reconstructPath(DungeonHandler handler) {
        Point end = handler.getBloodRoom();
        Point start = handler.getEntrance();
        Point fairy = handler.getFairy();
        Room[][] grid = handler.getGrid();
        Map<Point, Point> parentMap = handler.getRoomMap();

        Point step = end;
        grid[end.x][end.y].setRotation(wichDirection(end, parentMap.get(end)));
        Point key = end;
        Room room = CreatedRoom.path1;
        while (!step.equals(start)) {
            step = parentMap.get(key);
            if(step.equals(start)){
                grid[step.x][step.y].setRotation(wichDirection(step, key));
            }

            if(!step.equals(start) && !step.equals(fairy)){
                grid[step.x][step.y] = room;
                grid[step.x][step.y].setLoc((Point) step.clone());
                handler.getHistory().add((Point) step.clone());
            }
            key = step;
            if(key.equals(fairy)){
                room = CreatedRoom.path2;
            }
        }
    }

    void buildDoor(DungeonHandler handler) {
        Map<Point, Point> parentMap = handler.getDoorMap();
        Point start = handler.getEntrance();
        Room[][] grid = handler.getGrid();

        Point step = handler.getBloodRoom();
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

            if(!Objects.equals(d1, d2)){
                grid[pre_step.x][pre_step.y].addConection(pre_step,step);
                grid[step.x][step.y].addConection(step,pre_step);
                this.loadAndPasteSchematic("lockeddoor",
                        BlockVector3.at((pre_step.x*32)+transition.x,
                                70,(pre_step.y*32)+ transition.y),rotation, false);
            }
        }
    }
    void buildEmtyDoor(Point end, DungeonHandler handler) {
        Room[][] grid = handler.getGrid();
        Map<Point, Point> parentMapOri = handler.getRoomMap();
        Point step = end;
        Room room1, room2;
        Point pre_step, transition;
        int rotation;
        Map<Point, Point> parentMap = new HashMap<>(Map.copyOf(parentMapOri));

        if(grid[end.x][end.y].getID()==0){
            grid[end.x][end.y].setRotation(wichDirection(end, parentMap.get(end)));
        }
        while (!parentMap.isEmpty()) {
            pre_step = step;
            room1 = grid[pre_step.x][pre_step.y];
            step = parentMap.remove(step);
            if(step == null) return;
            room2 = grid[step.x][step.y];
            transition = new Point(-(pre_step.x - step.x)*16, -(pre_step.y - step.y)*16);
            rotation = transition.x==0? 0 : 90;
            if(!Objects.equals(room1, room2)){
                this.loadAndPasteSchematic("door",
                    BlockVector3.at((pre_step.x*32)+ transition.x,
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
    void fill(DungeonHandler handler, boolean ignoreLimit) {
        Stack<Point> history = handler.getHistory();
        Room[][] grid = handler.getGrid();
        Queue<Point> edge = handler.getEdge();
        Map<Point, Point> roomMap = handler.getRoomMap();

        int[][] directions;
        int dir;
        while(!history.isEmpty()){
            Point current = getValidStartPos(grid, history);
            if(current==null) return;
            directions = getDirection(current, grid);
            do dir = handler.getRandom().nextInt(directions.length);
            while (directions[dir]==null && (directions[0]!=null || directions[1]!=null || directions[2]!=null || directions[3]!=null));

            if(directions[dir]==null) return;

            int dx = current.x + directions[dir][0];
            int dy = current.y + directions[dir][1];
            Point neighbor = new Point(dx,dy);
            if(isValid(grid, neighbor)){
                boolean defined = defineRoom(handler, neighbor, true, null);
                if(!defined){
                    Bukkit.broadcastMessage(ChatColor.RED+"ran out of shape!");
                    break;
                }
                edge.add(new Point(dx,dy));
                grid[current.x][current.y].addConection(current, neighbor);
                grid[dx][dy].addConection(neighbor, current);
                roomMap.put(neighbor, current);
            }
        }
    }
    private Point getValidStartPos(Room[][] grid, Stack<Point> history) {
        Point point;
        while (!history.isEmpty()){
            point = history.pop();
            if(hasDir(point, grid)){
                history.add(point);
                return point;
            }
        }
        return null;
    }
    private boolean hasDir(Point point, Room[][] grid){
        int i = point.x, j = point.x;
        return  ((i+1 < grid.length) &&  grid[i+1][j] == null) ||
                ((i-1 >= 0) &&  grid[i-1][j] == null) ||
                ((j+1 < grid[i].length) &&  grid[i][j+1] == null) ||
                ((j-1 >= 0) &&  grid[i][j-1] == null);
    }
    private int[][] getDirection(Point point, Room[][] grid){
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        int i = point.x;
        int j = point.y;
        int[][] l = new int[4][];
        if((i+1 < grid.length) &&  grid[i+1][j]==null) { l[0] =directions[0]; }
        if((i-1 >= 0) &&  grid[i-1][j]==null) { l[1] = directions[1];}
        if((j+1 < grid[i].length) &&  grid[i][j+1]==null) { l[2] = directions[2];}
        if((j-1 >= 0) &&  grid[i][j-1]==null) { l[3] = directions[3];}
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

        try (
                FileInputStream fis = new FileInputStream(file);
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
     * */
    boolean defineRoom(DungeonHandler handler, Point point, boolean ignoreLimit, @Nullable Room exception) {
        LinkedList<Shape> pick = new LinkedList<>(List.of(
                new BOX_BY_BOX(), new L_BY_L(), new FOUR_BY_FOUR(),
                new THREE_BY_THREE(), new TOW_BY_TWO()
        ));
        Collections.shuffle(pick, handler.getRandom());
        pick.addLast(new ONE_BY_ONE());
        while (!pick.isEmpty()){
            Shape shape = pick.pop();
            if(isFit(handler, point, shape, exception)){
                return true;
            }
        }
        return false;
    }

    boolean isFit(DungeonHandler handler, Point point, me.jasper.jasperproject.Dungeon.Shapes.Shape shapes, @Nullable Room exception) {
        int x = point.x, y = point.y;
        Room[][] grid = handler.getGrid();
        Stack<Point> history = handler.getHistory();

        byte[][][] shape = shapes.getShape();
        for (int i = 0; i < shape.length; i++) {
            int rotation = 4;
            boolean valid = true;
            for (int rot = 0; rot < rotation; rot++) {
                shapes.rotate(i);
                for (byte[] bytes : shape[i]) {
                    if(!isValid(x+bytes[0], y+bytes[1], grid, exception)){
                        valid = false;
                        break;
                    }
                }
            }
            if(!valid) return false;
            Room valid_room = handler.getRooms().get(shapes.getType()).getFirst().clone();
            for (byte[] bytes : shape[i]) {
                grid[x+bytes[0]][y+bytes[1]] = valid_room;
            }
        }
        return true;
    }
    boolean isValid(int x, int y, Room[][] grid, @Nullable Room room){
        if(room == null){
                return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == null;
            }
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length &&
                (grid[x][y] == room);
    }
}