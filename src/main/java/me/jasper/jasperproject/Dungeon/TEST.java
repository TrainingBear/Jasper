package me.jasper.jasperproject.Dungeon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class TEST {
    public static Point getButtonPosition(byte point, byte[][] roomused) {
        for (int i = 0; i < roomused.length; i++) {
            for (int j = 0; j < roomused[i].length; j++) {
                if (roomused[i][j] == point) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    // BFS function to find the shortest path
    public static byte[][] bfs(Point start, Point end, byte[][] roomused) {
        int rows = roomused.length;
        int cols = roomused[0].length;

        boolean[][] visited = new boolean[rows][cols];
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parentMap = new HashMap<>();

        queue.add(start);
        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            Bukkit.broadcastMessage("Finding path..");
            Point current = queue.poll();

            // Check if end is reached
            if (current.equals(end)) {
                roomused = reconstructPath(parentMap, start, end, roomused);
                return roomused;
            }

            // Explore neighbors
            for (int[] direction : directions) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];
                Point neighbor = new Point(newX, newY);

                if (isValid(neighbor, rows, cols, visited, roomused)) {
                    queue.add(neighbor);
                    visited[newX][newY] = true;
                    parentMap.put(neighbor, current);
                }
            }
        }
        Bukkit.broadcastMessage("Target not found");
        return roomused;
    }

    // Check if a point is within bounds and not a wall
    private static boolean isValid(Point p, int rows, int cols, boolean[][] visited, byte[][] roomused) {
        return p.x >= 0 && p.x < rows && p.y >= 0 && p.y < cols &&
                !visited[p.x][p.y] && roomused[p.x][p.y] == 0;
    }

    // Reconstruct path by backtracking from end to start
    private static byte[][] reconstructPath(Map<Point, Point> parentMap, Point start, Point end, byte[][] roomused) {
        Point step = end;
        while (!step.equals(start)) {
            step = parentMap.get(step);
            if (!step.equals(start)) {
                RS.loadAndPasteSchematic("path",RS.path.getPastepoint(step.x, step.y),0 );
                roomused[step.x][step.y] = 4;
            }
        }
        return roomused;
    }
    /*
     * [1]->->[2]
     *         v
     *         L->->[3]
     * */


    //        byte[][] size = getLayoutSize(p,l);
//        generatelayout(player, size);
//        byte[][] roomUsed = generateSpecialRoom(player,p,l);
//        HashMap<RoomType,Integer> room = new HashMap<>();
//        for (int i = 0; i < roomUsed.length; i++) {
//            for (int j = 0; j < roomUsed[i].length; j++) {
//                if (Pattern.is2x2(roomUsed,i,j) && (!room.containsKey(RoomType.a2X2) || room.get(RoomType.a2X2) == null || room.get(RoomType.a2X2) < 2)) {
//                    roomUsed = Pattern.fill2x2(roomUsed,i,j);
//                    player.sendMessage("2x2 room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.a2X2,room.getOrDefault(RoomType.a2X2,0)+1);
//                }
//                else if (Pattern.is1x4(roomUsed,i,j) && (!room.containsKey(RoomType.a4X1) || room.get(RoomType.a4X1) == null || room.get(RoomType.a4X1) < 1)) {
//                    roomUsed = Pattern.fill1x4(roomUsed,i,j);
//                    player.sendMessage("1x4 room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.a4X1,room.getOrDefault(RoomType.a4X1,0)+1);
//                }
//                else if (Pattern.isLShape_Ia(roomUsed,i,j) && (!room.containsKey(RoomType.aL) || room.get(RoomType.aL) == null || room.get(RoomType.aL) < 2)){
//                    roomUsed = Pattern.fillLShape_Ia(roomUsed,i,j);
//                    player.sendMessage("flipped_I shape room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.aL,room.getOrDefault(RoomType.aL,0)+1);
//                }
//                else if (Pattern.is4x1(roomUsed,i,j) && (!room.containsKey(RoomType.a4X1) || room.get(RoomType.a4X1) == null || room.get(RoomType.a4X1) < 1)) {
//                    roomUsed = Pattern.fill4x1(roomUsed,i,j);
//                    player.sendMessage("4x1 room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.a4X1,room.getOrDefault(RoomType.a4X1,0)+1);
//                }
//                else if (Pattern.isLShaped_I(roomUsed,i,j) && (!room.containsKey(RoomType.aL) || room.get(RoomType.aL) == null || room.get(RoomType.aL) < 2)){
//                    roomUsed = Pattern.fillLShaped_I(roomUsed,i,j);
//                    player.sendMessage("_I shape room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.aL,room.getOrDefault(RoomType.aL,0)+1);
//                }
//                else if(Pattern.is3x1(roomUsed,i,j) && (!room.containsKey(RoomType.a3X1) || room.get(RoomType.a3X1) == null|| room.get(RoomType.a3X1) < 2)){
//                    roomUsed = Pattern.fill3x1(roomUsed,i,j);
//                    player.sendMessage("3x1 room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.a3X1,room.getOrDefault(RoomType.a3X1,0)+1);
//                }
//                else if (Pattern.isLShapedLa(roomUsed,i,j) && (!room.containsKey(RoomType.aL) || room.get(RoomType.aL) == null || room.get(RoomType.aL) < 2)){
//                    roomUsed = Pattern.fillLShapedLa(roomUsed,i,j);
//                    player.sendMessage("flippedL shape room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.aL,room.getOrDefault(RoomType.aL,0)+1);
//                }
//                else if (Pattern.is1x3(roomUsed,i,j) && (!room.containsKey(RoomType.a3X1) || room.get(RoomType.a3X1) == null|| room.get(RoomType.a3X1) < 2)) {
//                    roomUsed = Pattern.fill1x3(roomUsed,i,j);
//                    player.sendMessage("1x3 room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.a3X1,room.getOrDefault(RoomType.a3X1,0)+1);
//                }
//                else if (Pattern.is2x1(roomUsed,i,j) && (!room.containsKey(RoomType.a2X1) || room.get(RoomType.a2X1) == null || room.get(RoomType.a2X1) < 2)) {
//                    roomUsed = Pattern.fill2x1(roomUsed,i,j);
//                    player.sendMessage("2x1 room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.a2X1,room.getOrDefault(RoomType.a2X1,0)+1);
//                }
//                else if (Pattern.isLShaped_L(roomUsed,i,j) && (!room.containsKey(RoomType.aL) || room.get(RoomType.aL) == null || room.get(RoomType.aL) < 2)){
//                    roomUsed = Pattern.fillLShaped_L(roomUsed,i,j);
//                    player.sendMessage("L shape room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.aL,room.getOrDefault(RoomType.aL,0)+1);
//                }
//                else if (Pattern.is1x2(roomUsed,i,j) && (!room.containsKey(RoomType.a2X1) || room.get(RoomType.a2X1) == null || room.get(RoomType.a2X1) < 2)) {
//                    roomUsed = Pattern.fill1x2(roomUsed, i, j);
//                    player.sendMessage("1x2 room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.a2X1, room.getOrDefault(RoomType.a2X1, 0) + 1);
//                }
//                else if (Pattern.is1x1(roomUsed,i,j) && (!room.containsKey(RoomType.a1X1) || room.get(RoomType.a1X1) == null || room.get(RoomType.a1X1) < 3)) {
//                    roomUsed = Pattern.fill1x1(roomUsed,i,j);
//                    player.sendMessage("1x1 room detected at: (" + i*32 + "," + j*32 + ")");
//                    room.put(RoomType.a1X1,room.getOrDefault(RoomType.a1X1,0)+1);
//                }
////                           else {
////                                roomUsed = Pattern.fill1x1(roomUsed,i,j);
////                                player.sendMessage("1x1 room detected at: (" + i + "," + j + ")");
////                                room.put(RoomType.a1X1,room.getOrDefault(RoomType.a1X1,0)+1);
////                            }
//            }
//        }


//        for (int i = 0; i < roomUsed.length; i++) {
////                    plugin.getLogger().info(""); // Print a new line
//                    StringBuilder row = new StringBuilder();
//                    for (int j = 0; j < roomUsed[i].length; j++) {
//                        row.append(String.format("%02d, ",roomUsed[i][j]));
//                    }
//                    plugin.getLogger().info(row.toString()); // Print the row
//                    player.sendMessage(row.toString());
//                }
//    }

//    public static char[][] generateSpecialRoom(Player player, int p, int l){
//
//        char[][] size = getLayoutSize((byte) p,(byte) l);
//
//        if ((!roomTypes.containsKey(RS.yellow) || roomTypes.get(RS.yellow) < 1) &&
//                (!roomTypes.containsKey(RS.fairy) || roomTypes.get(RS.fairy) < 2) &&
//                (!roomTypes.containsKey(RS.puzzle) || roomTypes.get(RS.puzzle) < 2) &&
//                (!roomTypes.containsKey(RS.entrance) || roomTypes.get(RS.entrance) < 1)
//        ){
//            /*
//             *   This is the entrance
//             * */
//            int pxe = 0;
//            int pze = random.nextInt(l);
//            size[pxe][pze] = RS.entrance.getID();
//            Location start = new Location(Bukkit.getWorld("test"),pxe*32 ,70, pze*32);
//            Block blockStart = start.getBlock();
//            player.sendMessage("pasted at: "+blockStart.getX()+", "+blockStart.getZ() +" With type: "+RoomType.START);
//            blockStart.setType(RoomType.START.getType());
//            roomTypes.put(RS.entrance,roomTypes.getOrDefault(RS.entrance,0)+1);
//            int rotation;
//            if(size[0][0]==1){
//                rotation = 90;
//                rotation = rotation * (random.nextInt(2)+1);
//                RS.loadAndPasteSchematic("entrance",
//                        RS.entrance.getPastepoint(pxe,pze),
//                        rotation);
//            } else if (size[0][l-1]==1) {
//                rotation = -90;
//                rotation = rotation * (random.nextInt(2)+1);
//                RS.loadAndPasteSchematic("entrance",
//                        RS.entrance.getPastepoint(pxe,pze),
//                        rotation);
//            }else {
//                rotation = 180;
//                do {
//                    rotation = rotation - (90 * (random.nextInt(3)+1));
//                } while (rotation == 0);
//                RS.loadAndPasteSchematic("entrance",
//                        RS.entrance.getPastepoint(pxe,pze),
//                        rotation);
//            }
//
//            /*
//             *   This is the fairy
//             * */
//
//            int pxf = (int) (p/2);
//            int pzf = random.nextInt(l);
//            size[pxf][pzf] = RS.fairy.getID();
//            Location fairy = new Location(Bukkit.getWorld("test"),pxf*32 ,70, pzf*32);
//            Block fairyBlock = fairy.getBlock();
//            player.sendMessage("pasted at: "+fairyBlock.getX()+", "+fairyBlock.getZ() +" With type: "+RoomType.FAIRY);
//            fairyBlock.setType(RoomType.FAIRY.getType());
//            roomTypes.put(RS.fairy,roomTypes.getOrDefault(RS.fairy,0)+1);
//            RS.loadAndPasteSchematic("fairy",
//                    RS.fairy.getPastepoint(pxf,pzf),
//                    RS.fairy.getAngle());
//
//            /*
//             *   This is the Yellow
//             * */
//
//            int pxy = pxf+1;
//            int pzy = random.nextInt(l);
//            size[pxy][pzy] = RS.yellow.getID();
//            Location yellow = new Location(Bukkit.getWorld("test"),pxy*32 ,70, pzy*32);
//            Block yellowBlock = yellow.getBlock();
//            player.sendMessage("pasted at: "+yellowBlock.getX()+", "+yellowBlock.getZ() +" With type: "+RoomType.MINI_BOSS);
//            yellowBlock.setType(RoomType.MINI_BOSS.getType());
//            roomTypes.put(RS.yellow,roomTypes.getOrDefault(RS.yellow,0)+1);
//            RS.loadAndPasteSchematic("yellow",
//                    RS.yellow.getPastepoint(pxy,pzy),
//                    RS.yellow.getAngle());
//
//
//            /*
//             *   This is the Blood
//             * */
//
//            int pzb = random.nextInt(l);
//
//            while (pzb==pzy){
//                pzb = random.nextInt(l);
//                player.sendMessage(""+pzb);
//            }
//            size[p-1][pzb] = RS.blood.getID();
//            Location blood = new Location(Bukkit.getWorld("test"),(p-1)*32 ,70, pzb*32);
//            Block bloodBlock = blood.getBlock();
//            player.sendMessage("pasted at: "+bloodBlock.getX()+", "+bloodBlock.getZ() +" With type: "+RoomType.BLOOD);
//            bloodBlock.setType(RoomType.BLOOD.getType());
//            roomTypes.put(RS.blood,roomTypes.getOrDefault(RS.blood,0)+1);
//            RS.loadAndPasteSchematic("blood",
//                    RS.blood.getPastepoint(p-1,pzb),
//                    RS.blood.getAngle());
//
//
//            /*
//             *   This is the puzzle
//             * */
//            int pxp = 1;
//            pxp += random.nextInt(p-2);
//            int pzp = random.nextInt(l);
//            if ((pxp==pxf)||(pxp==pxy)){
//                while ((pzp==pzf)||(pzp==pzy)){
//                    player.sendMessage(pzp+" = "+pzb);
//                    pzp = random.nextInt(l);
//                }
//            }
//            size[pxp][pzp] = RS.puzzle.getID();
//            Location puzzle = new Location(Bukkit.getWorld("test"),pxp*32 ,70, pzp*32);
//            Block puzzleBlock = puzzle.getBlock();
//            player.sendMessage("pasted at: "+puzzleBlock.getX()+", "+puzzleBlock.getZ() +" With type: "+RoomType.PUZLE);
//            puzzleBlock.setType(RoomType.PUZLE.getType());
//            roomTypes.put(RS.puzzle,roomTypes.getOrDefault(RS.puzzle,0)+1);
//            RS.loadAndPasteSchematic("puzzle",
//                    RS.puzzle.getPastepoint(pxp,pzp),
//                    RS.puzzle.getAngle());
//
//
//            int pxp2 = 1;
//            pxp2 += random.nextInt(p-2);
//            int pzp2 = random.nextInt(l);
//            if ((pxp2==pxf)||(pxp2==pxy||(pxp2==pxp))){
//                while ((pzp2==pzf)||(pzp2==pzy)||(pzp2==pzp)){
//                    pzp2 = random.nextInt(l);
//                }
//            }
//            size[pxp2][pzp2] = RS.puzzle.getID();
//            Location puzzle2 = new Location(Bukkit.getWorld("test"),pxp2*32 ,70, pzp2*32);
//            Block puzzleBlock2 = puzzle2.getBlock();
//            player.sendMessage("pasted at: "+puzzleBlock2.getX()+", "+puzzleBlock2.getZ() +" With type: "+RoomType.PUZLE);
//            puzzleBlock2.setType(RoomType.PUZLE.getType());
//            roomTypes.put(RS.puzzle,roomTypes.getOrDefault(RS.puzzle,0)+1);
//            RS.loadAndPasteSchematic("puzzle",
//                    RS.puzzle.getPastepoint(pxp2,pzp2),
//                    RS.puzzle.getAngle());
//
//        }else {
//            player.sendMessage("Generating special room has been Complete!");
//            player.sendMessage("Room generated: "+roomTypes);
//        }
//        return size;
//    }
}
