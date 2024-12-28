package me.jasper.jasperproject.Dungeon;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Generate {
    static Random random = new Random();
    static HashMap<RS,Integer> roomTypes = new HashMap<>();
    static JasperProject plugin;
    public Generate(JasperProject plugin) {
        Generate.plugin = plugin;
    }

    public static char[][] getLayoutSize(byte p, byte l){
        char[][] size = new char[p][l];
        for (byte i = 0; i < p; i++) {
            for (byte j = 0; j < l; j++) {
                size[i][j] = 0;

            }
        }
        return size;
    }
    public static void generatelayout(Player player,char[][] size){

        player.sendMessage("Layout: ");
        for (byte j = 0; j < 4;j++){
            StringBuilder generatedSize = new StringBuilder();
            for (byte k = 0; k < 4; k++) {
                Location currentblockloc = new Location(Bukkit.getWorld("test"),j*32 ,70, k*32);
                Block block = currentblockloc.getBlock();
                block.setType(Material.GLASS);
                generatedSize.append(j*33+", "+70+", "+k*33+" || ");
            }
//                player.sendMessage(generatedSize.toString());
        }

    }
    public static void setgeneratelayouttoAir(char[][] size){


        for (byte j = 0; j < size.length;j++){
            for (byte k = 0; k < size[0].length; k++) {
                Location currentblockloc = new Location(Bukkit.getWorld("test"),j*32 ,70, k*32);
                Block block = currentblockloc.getBlock();
                block.setType(Material.AIR);

            }
        }

    }
    private static void generatePuzzle(char[][] roomused,byte x,byte y,Player player){
        Pattern.generate1Door(roomused, RS.path.getID(), RS.path2.getID(),x,y,"puzzle");
    }
    private static void generateYellow(char[][] roomused,byte x,byte y,Player player){
        Pattern.generate1Door(roomused, RS.path.getID(), RS.path2.getID(),x,y,"yellow");
    }

    private static void generateFairy(char[][] roomused,int x,int y,Player player){
        Location fairy = new Location(Bukkit.getWorld("test"),x*32 ,70, y*32);
        Block fairyBlock = fairy.getBlock();
        player.sendMessage("pasted at: "+fairyBlock.getX()+", "+fairyBlock.getZ() +" With type: "+RT.FAIRY);
        fairyBlock.setType(RT.FAIRY.getType());
        roomTypes.put(RS.fairy,roomTypes.getOrDefault(RS.fairy,0)+1);
        RS.loadAndPasteSchematic("fairy",
                RS.getPastepoint(x,y),
                RS.fairy.getAngle());
    }
    private static void generateStart(char[][] roomused,byte x,byte y,Player player){
        Location start = new Location(Bukkit.getWorld("test"), x * 32, 70, y * 32);
        Block blockStart = start.getBlock();
        blockStart.setType(RT.START.getType());
        roomTypes.put(RS.entrance, roomTypes.getOrDefault(RS.entrance, 0) + 1);
        Pattern.generate1Door(roomused, RS.path.getID(),x,y,"entrance");
    }
    private static void generateEnd(char[][] roomused,byte x,byte y,Player player){
            Location blood = new Location(Bukkit.getWorld("test"), x * 32, 70, y * 32);
            Block bloodBlock = blood.getBlock();
            bloodBlock.setType(RT.BLOOD.getType());
            roomTypes.put(RS.blood, roomTypes.getOrDefault(RS.blood, 0) + 1);
            Pattern.generate1Door(roomused, RS.path2.getID(), x,y,"blood");
    }

    private static void GenerateDoor(char[][] roomused,byte x,byte y,Player player){

    }

    private static char[][] roomused;
    public static void generateRoom(Player player, byte p, byte l) {
//                roomused = new char[][]{
//                        {3, 0, 1, 0},
//                        {0, 0, 0, 0},
//                        {0, 0, 0, 0},
//                        {0, 2, 0, 0}
//                };
        roomused = new char[p][l];
        generatelayout(player, roomused);
        byte distanceFairyWstart,distanceFairyWend,distance,startx,starty,endx,endy,fairyx,fairyy,puzzlex,puzzley,yellowx,yellowy;
        do {
            startx = (byte) random.nextInt(p);
            starty = (byte) random.nextInt(l);
            endx = (byte) random.nextInt(p);
            endy = (byte) random.nextInt(l);
            distance = (byte) Point.distance(startx, starty, endx, endy);

            fairyx = (byte) random.nextInt(p);
            fairyy = (byte) random.nextInt(l);
            distanceFairyWstart = (byte) Point.distance(fairyx, fairyy, startx, starty);
            distanceFairyWend = (byte) Point.distance(fairyx, fairyy, endx, endy);
        } while ((distance < p - 1)||(distanceFairyWstart < p / 2) || (distanceFairyWend < p / 2));

        roomused[startx][starty] = RS.entrance.getID();
        roomused[fairyx][fairyy] = RS.fairy.getID();
        roomused[endx][endy] = RS.blood.getID();
        Point start = Path.getButtonPosition(RS.entrance.getID(), roomused);
        Point mid = Path.getButtonPosition(RS.fairy.getID(), roomused);
        Point end = Path.getButtonPosition(RS.blood.getID(), roomused);
        boolean isPathAFound,isPathBFound;
        {
            isPathAFound = Path.bfs(RS.fairy.getID(), // ini end id
                    start, // Point Start
                    mid,    // Point End
                    RS.blood.getID(), // Ignored Room
                    roomused, // char[][] room
                    RS.path.getID(), // path1 ID
                    RS.path.getID(), // path2 ID
                    RS.path.getID()); // Ignored Path
            player.sendMessage("is Path type A found: " + isPathAFound);
            isPathBFound = Path.bfs(RS.blood.getID(), // ini end id
                    mid, // Point Start
                    end,    // Point End
                    RS.entrance.getID(), // Ignored Room
                    roomused, // char[][] room
                    RS.path2.getID(), // path1 ID
                    RS.path2.getID(), // path2 ID
                    RS.path.getID()); // Ignored Path
            player.sendMessage("is Path type 5 found: " + isPathBFound);
            if (!isPathBFound) {
                isPathBFound = Path.bfs(RS.blood.getID(), // ini end id
                        mid, // Point Start
                        end,    // Point End
                        RS.entrance.getID(), // Ignored Room
                        roomused, // char[][] room
                        RS.path2.getID(), // path1 ID
                        RS.path.getID(), // path2 ID
                        RS.path2.getID()); // Ignored Path
                player.sendMessage("is Path type 5 found: " + isPathBFound);
            }
            if (isPathBFound) {
                isPathAFound = Path.bfs(RS.fairy.getID(), // ini end id
                        start, // Point Start
                        mid,    // Point End
                        RS.blood.getID(), // Ignored Room
                        roomused, // char[][] room
                        RS.path.getID(), // path1 ID
                        RS.path.getID(), // path2 ID
                        RS.path2.getID()); // Ignored Path
                player.sendMessage("is Path type A found: " + isPathAFound);
            }
        } // Generate Path dari [E] -> [F] -> [B] di sini

        do {
            puzzlex = (byte) random.nextInt(p);
            puzzley = (byte) random.nextInt(l);
        } while (roomused[puzzlex][puzzley] != '\u0000');


        do {
            yellowx = (byte) random.nextInt(p);
            yellowy = (byte) random.nextInt(l);
        } while (roomused[yellowx][yellowy] != '\u0000');


        roomused[puzzlex][puzzley] = RS.puzzle.getID();
        roomused[yellowx][yellowy] = RS.yellow.getID();


        Point puzzle = Path.getButtonPosition(RS.puzzle.getID(), roomused);
        Point yellow = Path.getButtonPosition(RS.yellow.getID(), roomused);



        generateStart(roomused,startx,starty,player);
        generateEnd(roomused,endx,endy,player);
        generateFairy(roomused,fairyx,fairyy,player);
        generatePuzzle(roomused,puzzlex,puzzley,player);
        generateYellow(roomused,yellowx,yellowy,player);
        
        HashMap<Character,Integer> roomLimit = new HashMap<>();
        roomLimit.put('#',100);
        roomLimit.put('L',100);
        roomLimit.put('1',100);
        roomLimit.put('2',100);
        roomLimit.put('3',100);
        roomLimit.put('4',100);
        
        
        
        roomused = Pattern.Load.path(roomLimit,roomused,RS.path.getID(),player);
        roomused = Pattern.Load.path(roomLimit,roomused,RS.path2.getID(),player);
//        roomused = Pattern.Load.room(roomLimit,roomused,player);

        player.sendMessage("Placed Start and end with distance of "+ distance);
        player.sendMessage("With start location of "+ start.x+", "+start.y);
        player.sendMessage("With end location of "+ end.x+", "+endy);

        for (int i = 0; i <  roomused.length; i++) {
//                   plugin.getLogger().info(""); // Print a new line
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < roomused[i].length; j++) {
                row.append(String.format("%c, ",roomused[i][j]));
            }
            plugin.getLogger().info(row.toString()); // Print the row
            player.sendMessage(row.toString());
        }

    }

    public static class Path {
        public static Point getButtonPosition(char target, char[][] roomused) {
            for (int i = 0; i < roomused.length; i++) {
                for (int j = 0; j < roomused[i].length; j++) {
                    if (roomused[i][j] == target) {
                        return new Point(i, j);
                    }
                }
            }
            return null;
        }

        // BFS function to find the shortest path
        public static boolean bfs(char endid,Point start, Point end,char ignoredRoom, char[][] roomused,char pathid,char pathid2,char ignoredPath) {
            int rows = roomused.length;
            int cols = roomused[0].length;

            boolean[][] visited = new boolean[rows][cols];
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            Queue<Point> queue = new LinkedList<>();
            Map<Point, Point> parentMap = new HashMap<>();

            queue.add(start);
            visited[start.x][start.y] = true;

            while (!queue.isEmpty()) {
                Point current = queue.poll();

                // Check if end is reached
                if (current.equals(end)) {
                    reconstructPath(parentMap, start, end, roomused,pathid);
                    return true;
                }

                // Explore neighbors
                for (int[] direction : directions) {
                    int newX = current.x + direction[0];
                    int newY = current.y + direction[1];
                    Point neighbor = new Point(newX, newY);

                    if (isValid(neighbor,ignoredRoom, rows, cols, visited, roomused,endid,pathid,pathid2,ignoredPath)) {
                        queue.add(neighbor);
                        visited[newX][newY] = true;
                        parentMap.put(neighbor, current);
                    }
                }
            }
            Bukkit.broadcastMessage("Target not found");
            return false;
        }

        // Check if a point is within bounds and not a wall
        private static boolean isValid(Point p,char mid, int rows, int cols, boolean[][] visited, char[][] roomused,char end,char pathid,char pathid2,char ignoredPath) {
            return p.x >= 0 && p.x < rows && p.y >= 0 && p.y < cols &&
                    !visited[p.x][p.y] && ((roomused[p.x][p.y] != mid)) && ((roomused[p.x][p.y]=='\u0000')||(roomused[p.x][p.y]==pathid)||(roomused[p.x][p.y]==pathid2)||(roomused[p.x][p.y]==end)
                    && ((roomused[p.x][p.y]!=ignoredPath)&&(roomused[p.x][p.y]!=RS.puzzle.getID())&&(roomused[p.x][p.y]!=RS.yellow.getID()))) ;
        }


        // Reconstruct path by backtracking from end to start
        private static void reconstructPath(Map<Point, Point> parentMap, Point start, Point end, char[][] roomused,char pathid) {
            Point step = end;
            while (!step.equals(start)) {
                step = parentMap.get(step);
                if (!step.equals(start)) {
                    roomused[step.x][step.y] = pathid;
                }
            }
        }
    }

}


