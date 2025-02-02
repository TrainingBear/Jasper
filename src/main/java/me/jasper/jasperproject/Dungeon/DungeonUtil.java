package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.fastutil.Hash;
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
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DungeonUtil {

        // BFS function to find the shortest path
    boolean[][] recoverVisited(Map<Point, Point> parrentMap, Point end, Point start, boolean[][] visited){
        Point step = end;
        visited[step.x][step.y] = true;
        do{
            step = parrentMap.get(step);
            visited[step.x][step.y] = true;
        }while (!step.equals(start));
        return visited;
    }
    public boolean findPath(Point start, Point fairy, Point end, Room[][] grid, Room pathSchem,Room pathSchem2, Map<Point, Point> parentMap, Stack<Point> history) {
            int rows = grid.length;
            int cols = grid[0].length;

            int target = grid[end.x][end.y].ID;
            boolean[][] visited = new boolean[rows][cols];
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            Queue<Point> queue = new LinkedList<>();

            queue.add(start);
            visited[start.x][start.y] = true;
            boolean foundFairy = false;

            while (!queue.isEmpty()) {
                Point current = queue.poll();

                // Check if end is reached
                if (current.equals(fairy) && !foundFairy) {
                    for (boolean[] visit : visited) Arrays.fill(visit,false);
                    visited = recoverVisited(parentMap, fairy, start, visited);
                    queue.clear();
                    current = fairy;
                    foundFairy = true;
                    Bukkit.broadcastMessage("Fairy found");
                }
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
            Bukkit.broadcastMessage("Target not found");
            return false;
        }

    public void buildDoor(Map<Point, Point> parentMap, Point start, Point end, Room[][] grid) {
        DungeonUtil util = new DungeonUtil();
        Point step = end;
        String d1, d2;
        int x,y,x2,y2,dx,dy,rotation;
        while (!step.equals(start)) {
            x2 = step.x;
            y2 = step.y;
            d1 = grid[x2][y2].name;

            step = parentMap.get(step);
            x = step.x;
            y = step.y;
            d2 = grid[x][y].name;

            dx = -(x2-x)*16;
            dy = -(y2-y)*16;
            rotation = dx==0? 0 : 90;
            if(!Objects.equals(d1, d2)){
                util.loadAndPasteSchematic("door",
                        new BlockVector3((x2*32)+dx,70,(y2*32)+dy),rotation, false);
            }
        }
    }
    public void buildEmtyDoor(Map<Point, Point> parentMap, Point end, Room[][] grid) {
        DungeonUtil util = new DungeonUtil();
        Point step = end;
        String d1, d2;
        int x,y,x2,y2,dx,dy,rotation;
        if(grid[end.x][end.y].ID==0){
            grid[end.x][end.y].setRotation(wichDirection(end, parentMap.get(end)));
        }
        while (!parentMap.isEmpty()) {
            x2 = step.x;
            y2 = step.y;
            d1 = grid[x2][y2].name;

            step = parentMap.remove(step);
            if(step == null){
//                Bukkit.broadcastMessage("Stopped at "+x2+", "+y2);
                return;
            }
            x = step.x;
            y = step.y;
            d2 = grid[x][y].name;

            dx = -(x2-x)*16;
            dy = -(y2-y)*16;
            rotation = dx==0? 0 : 90;
            if(!Objects.equals(d1, d2)){
                util.loadAndPasteSchematic("door",
                    new BlockVector3((x2*32)+dx,70,(y2*32)+dy),rotation, false);
            }
        }
    }

        // Implements BFS function, but fill the grid after path found
        public void random_dir(Map<Character,Integer> CURRENT_LIMIT, Map<Character,Integer> MAX_LIMIT, Random random, Room[][] grid, Stack<Point> history, Queue<Point> endpoint, Map<Point,Point> parrentMap) {
        int[][] directions;
        int dir;
        while(!history.isEmpty()){
            Point current = getValidStartPos(grid,history);

            directions = getDir(grid,current);
            do{
                dir = random.nextInt(directions.length);
            }while (directions[dir]==null && (directions[0]!=null || directions[1]!=null || directions[2]!=null || directions[3]!=null));

            if(directions[dir]==null){
                break;
            }

            int dx = current.x + directions[dir][0];
            int dy = current.y + directions[dir][1];
            Point neighbor = new Point(dx,dy);
            if(isValid(grid,neighbor)){
                Bukkit.broadcastMessage("Defining current room of "+dx+", "+dy);
                defineRoom(CURRENT_LIMIT, MAX_LIMIT,false, random, grid, dx, dy,null, history, true);
 //               history.add(neighbor);
 //               queue.add(neighbor);

                endpoint.add(new Point(dx,dy));
                parrentMap.put(neighbor, current);
            }if(!hasDir(neighbor.x, neighbor.y, grid)){
   //             grid[dx][dy] = new Room("pojokan","p",7,"puzzle", new Point(dx*32, dy*32));
                neighbor = getnewPath(grid ,history, parrentMap, neighbor);
                if(neighbor!=null){
 //                   queue.add(neighbor);
                    history.add(neighbor);
                }
            }


        }
    }

        public void loadAndPasteSchematic(String fileName, BlockVector3 location, int rotationDegrees, boolean ignoreAir) {
        File file = new File("C:\\Users\\user\\AppData\\Roaming\\.feather\\player-server\\servers\\7a1e3607-139e-4341-a6b9-6340739908da\\plugins\\WorldEdit\\schematics\\" + fileName + ".schem");

        if (!file.exists()) {
            Bukkit.broadcastMessage("Schematic file not found.");
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

        //whichDirection() util
        private boolean isdirValid(Room[][] grid, int x, int y, int o){
        return (x < grid.length && x >= 0) && (y < grid[0].length && y >=0) &&
                grid[x][y]!=null && grid[x][y].ID == o;
    }

        //findPath() util
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
                grid[step.x][step.y] = room.clone();
                grid[step.x][step.y].setLoc(new Point(step.x * 32, step.y * 32));
                history.add(new Point(step.x, step.y));
            }
            key = step;
            if(key.equals(fairy)){
                room = pathid2;
            }
        }
    }
    public int wichDirection(Point currentRoom, Point neightboor){
        int dx,dy;
        dx = neightboor.x - currentRoom.x;
        dy = neightboor.y - currentRoom.y;
        if(dy==-1) return -90;
        if(dx==1) return 180;
        if(dy==1) return 90;
        return 0;
    }
    private Point getnewPath(Room[][] grid, Stack<Point> history, Map<Point,Point> parrentMap, Point current) {
        Point point;
        int x,y;
        while (true){
            if(history.isEmpty()){
                return null;
            }
            point = history.pop();
//            parrentMap.put(point,current);
//            current = point;
            x = point.x;
            y = point.y;
            if(hasDir(x,y,grid)){
                return point;
            }
        }
    }
    private Point getValidStartPos(Room[][] grid, Stack<Point> history) {
        Point point;
        int x,y;
        while (true){
            if(history.isEmpty()){
                return null;
            }
            point = history.pop();
            x = point.x;
            y = point.y;
            if(hasDir(x,y,grid)){
                return point;
            }
        }
    }
    private boolean hasDir(int i, int j, Room[][] grid){
        return  ((i+1 < grid.length) &&  grid[i+1][j]==null) ||
                ((i-1 >= 0) &&  grid[i-1][j]==null) ||
                ((j+1 < grid[i].length) &&  grid[i][j+1]==null) ||
                ((j-1 >= 0) &&  grid[i][j-1]==null);
    }
    private int[][] getDir(Room[][] grid,Point point){
        System.out.println("Getting current("+point.x+", "+ point.y+") dirrection..");
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        int i = point.x;
        int j = point.y;
        boolean bawah,atas,kanan,kiri;
        bawah = (i+1 < grid.length) &&  grid[i+1][j]==null;
        atas = (i-1 >= 0) &&  grid[i-1][j]==null;
        kanan = (j+1 < grid[i].length) &&  grid[i][j+1]==null;
        kiri = (j-1 >= 0) &&  grid[i][j-1]==null;
        int[][] l = new int[4][];
        if(bawah) { l[0] =directions[0];}
        if(atas) { l[1] = directions[1];}
        if(kanan) { l[2] = directions[2];}
        if(kiri) { l[3] = directions[3];}
        return l;
    }
    private boolean isValid(Room[][] grid,Point n){
        return ( n.x >= 0 && n.x < grid.length) &&
                (n.y >= 0 && n.y < grid[0].length) && grid[n.x][n.y] == null;
    }

    public boolean defineRoom(Map<Character,Integer> CURRENT_LIMIT, Map<Character,Integer> MAX_LIMIT,boolean placeSpecial,Random random, Room[][] grid, int i, int j, Room room, Stack<Point> history, boolean debug) {
//            if (!(grid[i][j] != null && (grid[i][j].ID == 4 || grid[i][j].ID == 5))){
//                Bukkit.broadcastMessage("Cant define room");
//                return;
//            }
        int[][][] lshapes = {
                // L
                {{0, 0}, {1, 0}, {1, 1}},//0

                {{0, 0}, {0, 1}, {1, 0}},//90
                {{0, 0}, {0, 1}, {1, 1}},//180
                {{0, 0}, {1, 0}, {1, -1}},//270
                {{0, 0}, {-1, 0}, {0, 1}},//0
                {{0, 0}, {0, -1}, {1, -1}},//90
                {{0, 0}, {0, -1}, {1, 0}},//180
                {{0, 0}, {-1, 0}, {0, -1}},//270
                {{0, 0}, {0, -1}, {-1, -1}},//0
                // 90 degree of L
                {{0, 0}, {-1, 0}, {-1, 1}},//90
                // 180 degree of L
                {{0, 0}, {-1, 0}, {-1, -1}},//180
                // 270 degree of L
                {{0, 0}, {0, 1}, {-1, 1}},//270
        };

        int[][][] box = {
                // box
                {{0, 0}, {0, 1}, {1, 1}, {1, 0}},
                {{0, 0}, {0, -1}, {1, -1}, {1, 0}},
                {{0, 0}, {-1, 0}, {-1, -1}, {0, -1}},
                {{0, 0}, {-1, 0}, {0, 1}, {1, 1}},
        };

        int[][][] four = {
                // 4x1
                {{0, 0}, {0, 1}, {0, 2}, {0, 3}},
                {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
                {{0, 0}, {0, -1}, {0, 1}, {0, 2}},
                {{0, 0}, {-1, 0}, {1, 0}, {2, 0}},
                {{0, 0}, {0, -1}, {0, -2}, {0, 1}},
                {{0, 0}, {-1, 0}, {-2, 0}, {1, 0}},
                {{0, 0}, {0, -1}, {0, -2}, {0, -3}},
                {{0, 0}, {-1, 0}, {-2, 0}, {-3, 0}},
                // 1x4
        };

        int[][][] three = {
                // 3x1
                {{0, 0}, {0, 1}, {0, 2}},
                {{0, 0}, {1, 0}, {2, 0}},
                {{0, 0}, {0, -1}, {0, 1}},
                {{0, 0}, {-1, 0}, {1, 0}},
                {{0, 0}, {0, -1}, {0, -2}},
                {{0, 0}, {-1, 0}, {-2, 0}},
                // 1x3

        };
        int[][][] two = {
                // 2x1

                {{0, 0}, {0, 1}},
                {{0, 0}, {1, 0}},
                // 1x2
                {{0, 0}, {0, -1}},
                {{0, 0}, {-1, 0}},
        };
        int[][][] one = {
                // 1x1
                {{0, 0}}
        };
        int[][][] shape;
        Point pastepoint = new Point(i*32,j*32);

        Map<int[][][], LinkedList<Room>> shapes = new HashMap(
                Map.of(
                        two, ListRoom.TWO,
                        three, ListRoom.THREE,
                        four, ListRoom.FOUR,
                        box, ListRoom.BOX,
                        lshapes, ListRoom.L,
                        one, ListRoom.SINGLE
                )
        );

        Stack<int[][][]> pick = new Stack<>();

        pick.add(two);
        pick.add(three);
        pick.add(four);
        pick.add(box);
        pick.add(lshapes);
        Collections.shuffle(pick, random);
        pick.addFirst(one);

        while (!pick.isEmpty()){
            shape = pick.pop();
            if(isFit(CURRENT_LIMIT, MAX_LIMIT, placeSpecial, grid, i, j, shape, shapes, pastepoint, room, history, debug)){
//                Bukkit.broadcastMessage("Found shape! at "+ grid[i][j].loc.x +", "+grid[i][j].loc.y);
                return true;
            }
        }
        return false;
    }

    int[][][] getCopyOfShape(Room validShape){
        int[][][] four2 = {
                // 4x1
                {{0, 0}, {0, 1}, {0, 1}, {0, 1}},
                {{0, 0}, {1, 0}, {1, 0}, {1, 0}},
                {{0, 0}, {0, -1}, {0, 1}, {0, 1}},
                {{0, 0}, {-1, 0}, {1, 0}, {1, 0}},
                {{0, 0}, {0, -1}, {0, -1}, {0, 1}},
                {{0, 0}, {-1, 0}, {-1, 0}, {1, 0}},
                {{0, 0}, {0, -1}, {0, -1}, {0, -1}},
                {{0, 0}, {-1, 0}, {-1, 0}, {-1, 0}},
                // 1x4
        };
        int[][][] three = {
                // 3x1
                {{0, 0}, {0, 1}, {0, 1}},
                {{0, 0}, {1, 0}, {1, 0}},
                {{0, 0}, {0, -1}, {0, 1}},
                {{0, 0}, {-1, 0}, {1, 0}},
                {{0, 0}, {0, -1}, {0, -1}},
                {{0, 0}, {-1, 0}, {-1, 0}},
                // 1x3

        };
        int[][][] box2 = {
                // box
                {{0, 0}, {0, 1}, {1, 0}, },
                {{0, 0}, {0, -1}, {1, 0},},
                {{0, 0}, {-1, 0}, {0, -1}},
                {{0, 0}, {0, 1}, {1, 0}, },
        };
        int[][][] lshapes2 = {
                // L
                {{0, 0}, {1, 0}, {0, 1}},//0
                {{0, 0}, {0, 1}, {1, 0}},//90
                {{0, 0}, {0, 1}, {1, 0}},//180
                {{0, 0}, {1, 0}, {0, -1}},//270
                {{0, 0}, {-1, 0}, {0, 1}},//0
                {{0, 0}, {1, 0}, {0, -1}},//90
                {{0, 0}, {0, -1}, {1, 0}},//180
                {{0, 0}, {-1, 0}, {0, -1}},//270
                {{0, 0}, {0, -1}, {-1, 0}},//0
                // 90 degree of L
                {{0, 0}, {-1, 0}, {0, 1}},//90
                // 180 degree of L
                {{0, 0}, {-1, 0}, {0, -1}},//180
                // 270 degree of L
                {{0, 0}, {0, 1}, {-1, 0}},//270
        };
        int[][][] two = {
                // 2x1

                {{0, 0}, {0, 1}},
                {{0, 0}, {1, 0}},
                // 1x2
                {{0, 0}, {0, -1}},
                {{0, 0}, {-1, 0}},
        };
        int[][][] one = {
                // 1x1
                {{0, 0}}
        };
        return switch (validShape.logo){
            case '4' -> four2;
            case '3' -> three;
            case '#' -> box2;
            case 'L' -> lshapes2;
            case '2' -> two;
            case '1' -> one;
            default -> throw new IllegalStateException("Unexpected value: " + validShape.logo);
        };
    }
    private boolean isFit(Map<Character,Integer> CURRENT_LIMIT, Map<Character,Integer> MAX_LIMIT, boolean special, Room[][] grid, int i, int j, int[][] @NotNull [] shape, Map<int[][][], LinkedList<Room>> shapes, Point pastepoint, Room room, Stack<Point> history, boolean debug) {
        int x = 0, y = 0, dx, dy;
        int lx, ly;
        boolean valid = true;
        int rotation = 1;

        if(CURRENT_LIMIT.get(shapes.get(shape).peek().logo) >= MAX_LIMIT.get(shapes.get(shape).peek().logo)){
            return false;
        }
        for (int k = 0; k < shape.length; k++) {
            valid = true;
            for (int l = 0; l < shape[k].length; l++) {
                    x = i + shape[k][l][0];
                    y = j + shape[k][l][1];
                    if (!isValid(grid, x, y, room)) {
                        valid = false;
                        break;
                    }
            }
            if(valid) {
                CURRENT_LIMIT.put(shapes.get(shape).peek().logo,CURRENT_LIMIT.get(shapes.get(shape).peek().logo)+1);
                Bukkit.broadcastMessage(shapes.get(shape).peek().name+" has limit of "+ CURRENT_LIMIT.get((shapes.get(shape).peek().logo)));

                Room validRoom = shapes.get(shape).peek().clone();
                if(special && Objects.equals(shapes.get(shape).peek().name, "SINGLE")){
//                    shapes.get(shape).remove();
                    validRoom = shapes.get(shape).peekLast().clone();
//                    Bukkit.broadcastMessage("ADDED SPECIAL ROOM");
                }

                validRoom.setName(validRoom.name+k);
                if(debug){
                    Bukkit.broadcastMessage(validRoom.name+" Shape Found at "+i+", "+j+" with rot of "+k);
                }
                int[][][] copyOfShape = getCopyOfShape(validRoom);
                if(room != null){
                    validRoom.setLogo(room.logo);
                }
                for (int l = 0; l < shape[k].length; l++) {
                    if(validRoom.logo == '#'){
                        int m = l == 0? l: l-1;
                        x = i + copyOfShape[k][m][0];
                        y = j + copyOfShape[k][m][1];
                    }else {
                        x = i + copyOfShape[k][l][0];
                        y = j + copyOfShape[k][l][1];
                    }

                    lx = i + shape[k][l][0];
                    ly = j + shape[k][l][1];

                    dx = -(i-x)*16;
                    dy = -(j-y)*16;

                    pastepoint.translate(dx,dy);
                    if(debug){
                        Bukkit.broadcastMessage(" -translated to "+dx+", "+dy);
                    }

                    grid[lx][ly] = validRoom;
                    history.add(new Point(lx,ly));
                }
                validRoom.setLoc(pastepoint);
                validRoom.setRotation((rotation+2)*90);
                return valid;
            }
            rotation++;
        }

        return valid;
    }
    static boolean isValid(Room[][] grid, int x, int y, Room room){
            if(room == null){
                return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == null;
            }
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length &&
                (grid[x][y] != null  && grid[x][y].ID == room.ID);
    }

}