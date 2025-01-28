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

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DungeonUtil {

        // BFS function to find the shortest path
        public boolean findPath(Point start, Point end, Room[][] grid, Room pathSchem, Map<Point, Point> parentMap, Stack<Point> history) {
            int rows = grid.length;
            int cols = grid[0].length;

            int target = grid[end.x][end.y].ID;
            boolean[][] visited = new boolean[rows][cols];
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            Queue<Point> queue = new LinkedList<>();

            queue.add(start);
            visited[start.x][start.y] = true;
            boolean p1 = false;
            while (!queue.isEmpty()) {
                Point current = queue.poll();

                // Check if end is reached
                if (current.equals(end)) {
                    reconstructPath(parentMap, start, end, grid, pathSchem, history);
                    return true;
                }

                // Explore neighbors
                for (int[] direction : directions) {
                    int newX = current.x + direction[0];
                    int newY = current.y + direction[1];
                    Point neighbor = new Point(newX, newY);
                    if (isValid(neighbor, rows, cols, visited, grid,target)) {
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
                        new BlockVector3((x2*32)+dx,70,(y2*32)+dy),rotation);
            }
        }
    }
    public void buildEmtyDoor(Map<Point, Point> parentMap, Point end, Room[][] grid) {
        DungeonUtil util = new DungeonUtil();
        Point step = end;
        String d1, d2;
        int x,y,x2,y2,dx,dy,rotation;
        Bukkit.broadcastMessage("Building emty door with end of "+end.x +", "+end.y);
        while (!parentMap.isEmpty()) {
            x2 = step.x;
            y2 = step.y;
            d1 = grid[x2][y2].name;

            step = parentMap.remove(step);
            if(step == null){
                Bukkit.broadcastMessage("Stopped at "+x2+", "+y2);
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
                    new BlockVector3((x2*32)+dx,70,(y2*32)+dy),rotation);
            }
        }
    }

        // Implements BFS function, but fill the grid after path found
        public void random_dir(Room[][] grid, Stack<Point> history, Queue<Point> endpoint, Map<Point,Point> parrentMap) {
        int[][] directions;
   //     Stack<Point> queue = new Stack<>();
        Random random = new Random();
 //       queue.add(getValidStartPos(grid,history));

        int dir;
        while(!history.isEmpty()){
            Point current = getValidStartPos(grid,history);

            Bukkit.broadcastMessage("getting random current room.. "+current.x+", "+current.y);
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
                defineRoom(grid, dx, dy,null, history, true);
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


        public int wichDirection(Room[][] grid,Point menghadap, int dihadap){
            int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
            int dx,dy;
            for (int[] direction : directions) {
                dx = menghadap.x + direction[0];
                dy = menghadap.y + direction[1];
                if(isdirValid(grid,dx,dy,dihadap)){
                    if(Arrays.equals(direction,directions[0])){
                        return 180;
                    }if(Arrays.equals(direction,directions[1])){
                        return 0;
                    }if(Arrays.equals(direction,directions[2])){
                        return 90;
                    }if(Arrays.equals(direction,directions[3])){
                        return -90;
                    }
                }
            }
            return 0;
        }
        public void loadAndPasteSchematic(String fileName, BlockVector3 location, int rotationDegrees) {
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
                        .ignoreAirBlocks(false)
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
        private boolean isValid(Point p, int rows, int cols, boolean[][] visited, Room[][] grid, int target) {
        return (p.x >= 0 && p.x < rows && p.y >= 0 && p.y < cols) &&
                !visited[p.x][p.y] && (grid[p.x][p.y] == null || grid[p.x][p.y].ID == target);
    }
        private void reconstructPath(Map<Point, Point> parentMap, Point start, Point end, Room[][] grid, Room pathid, Stack<Point> history) {
        Point step = end;

        while (!step.equals(start)) {
            step = parentMap.get(step);
            if(!step.equals(start)){
                grid[step.x][step.y] = pathid.clone();
                grid[step.x][step.y].setLoc(new Point(step.x * 32, step.y * 32));
                history.add(new Point(step.x, step.y));
            }
        }
    }

    // random dir util
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
//        int k = 0;
//        k = bawah?++k:k;
//        k = atas?++k:k;
//        k = kanan?++k:k;
//        k = kiri?++k:k;
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

    public void defineRoom(Room[][] grid, int i, int j, Room room, Stack<Point> history, boolean debug) {
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
                {{0, 0}, {0, -1}, {1, 0}, {1, -1}},
                {{0, 0}, {-1, 0}, {-1, -1}, {0, -1}},
                {{0, 0}, {-1, 0}, {0, 1}, {-1, 1}},
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
        int[][][] shape = null;
        boolean fit;
        int translate = 1;
        Point pastepoint = new Point(i*32,j*32);

        Map<int[][][],Room> shapes = new HashMap();
        shapes.put(two, new Room("2x1 room","room",8,"1x2", '2'));
        shapes.put(three, new Room("3x1 room","room",8,"1x3", '3'));
        shapes.put(four, new Room("4x1 room","room",8,"1x4", '4'));
        shapes.put(box, new Room("2x2 room","room",8,"2x2", '#'));
        shapes.put(lshapes, new Room("L room","room",8,"Lshape", 'L'));
        shapes.put(one, new Room("name"+1,"room",0,"null", '1'));

        Stack<int[][][]> pick = new Stack<>();

        pick.add(two);
        pick.add(one);
        pick.add(three);
        pick.add(four);
        pick.add(box);
        pick.add(lshapes);
        Collections.shuffle(pick);

        int notmatch = 0;

        boolean isL = false;
        while (!pick.isEmpty()){
            shape = pick.pop();
            isL = Arrays.deepEquals(shape, lshapes);
            if(isFit(grid, i, j, shape, shapes, pastepoint, room, isL, history, debug)){
//                Bukkit.broadcastMessage("Found shape! at "+ grid[i][j].loc.x +", "+grid[i][j].loc.y);
                break;
            }
            notmatch++;
        }
        if(notmatch==5){
        }
    }

    private boolean isFit(Room[][] grid, int i, int j, int[][][] shape, Map<int[][][], Room> shapes, Point pastepoint, Room room, boolean isL, Stack<Point> history, boolean debug) {
        int x, y, x2, y2, dx, dy;
        int lx, ly;
        boolean valid = true;
        int rotation = 1;

        int[][][] lshapes2 = {
                // L
                {{0, 0}, {1, 0}, {1, 1}},//0

                {{0, 0}, {0, 1}, {1, 1}},//90
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
                if(isL){
                    x2 = i;
                    y2 = j;
                    if(debug){

                    }
                    Bukkit.broadcastMessage("LShape Found at "+x2+", "+y2);
                    for (int l = 0; l < shape[k].length; l++) {
                            x = i + lshapes2[k][l][0];
                            y = j + lshapes2[k][l][1];

                            lx = i + shape[k][l][0];
                            ly = j + shape[k][l][1];

                            dx = -(x2-x)*16;
                            dy = -(y2-y)*16;

                            x2 = x;
                            y2 = y;

                            pastepoint.translate(dx,dy);
                            grid[lx][ly] = shapes.get(shape);
                            history.add(new Point(lx,ly));
                            Bukkit.broadcastMessage(" -at "+x2+", "+y2);
                    }
                    shapes.get(shape).setLoc(pastepoint);
                    shapes.get(shape).setRotation((rotation+2)*90);
                    return valid;
                }
                x2 = i;
                y2 = j;
                Bukkit.broadcastMessage("Shape Found at "+x2+", "+y2);
                for (int l = 0; l < shape[k].length; l++) {
                        x = i + shape[k][l][0];
                        y = j + shape[k][l][1];

                        dx = -(x2-x)*16;
                        dy = -(y2-y)*16;

                        x2 = x;
                        y2 = y;

                        pastepoint.translate(dx,dy);
                        grid[x][y] = shapes.get(shape);
                        history.add(new Point(x,y));
                        Bukkit.broadcastMessage(" -at "+x2+", "+y2);
                }
                shapes.get(shape).setLoc(pastepoint);
                shapes.get(shape).setRotation(rotation*90);
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