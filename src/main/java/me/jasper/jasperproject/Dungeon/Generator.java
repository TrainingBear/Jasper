package me.jasper.jasperproject.Dungeon;

import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.awt.*;
import java.util.*;

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
            RoomType.TWO_X_ONE, 2,
            RoomType.THREE_X_ONE, 1,
            RoomType.FOUR_X_ONE, 1,
            RoomType.BOX,  1,
            RoomType.L_SHAPE, 1,
            RoomType.SINGLE, 10,
            RoomType.PUZZLE, 1,
            RoomType.TRAP, 1,
            RoomType.MINI_BOSS, 1
    ));
    Room[][] grid = new Room[p][l];
    Stack<Point> history = new Stack<>();

    // main method
    public void generate(){
        random.setSeed(seed);
        Queue<Point> endpoint = new LinkedList<>();
        Map<Point, Point> parrentMap = new HashMap<>();

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


        //Fill Dungeon room
        Map<Point, Point> parrentMap2 = new HashMap<>();
        this.random_dir(avaibleRooms, this, endpoint, parrentMap2);

        long endTime = System.nanoTime();
        String time = String.format("%.2f", (endTime - startTime) / 1_000_000.0);

        Bukkit.broadcastMessage(ChatColor.GREEN+"Initializing dungeon took "+time +" ms");


        this.buildDoor(parrentMap, new Point(x,y), new Point(x3,y3), grid);
        for (Point end : endpoint){
            this.buildEmtyDoor(parrentMap2, end, grid);
        }
        placePTMR(random, grid, endpoint);
        renderDungeon(grid);
        endTime = System.nanoTime();
        time = String.format("%.2f", (endTime - startTime) / 1_000_000.0);

        Bukkit.broadcastMessage(ChatColor.GREEN+"Rendering dungeon took "+time+" ms");
    }

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
                break;
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
            distance1 = Point.distance(x,y,x2,y2);
            distance2 = Point.distance(x2,y2,x3,y3);
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
                    break;
                }
            }while (grid[x2][y2]!=null && distance1 < ((double) p /2)-((double) p /4)+1 || distance2 < ((double) p /2)-((double) p /4)+1);
            prevx = x2;
            prevy = y2;
            grid[x2][y2] = fairy;
            found = this.findPath(new Point(x,y),new Point(x2,y2), new Point(x3,y3), grid, path1, path2, parrentMap, history, true);

            if(OCCUR++ > MAX_RECUR_TRIES){
                Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. OCCUR outbeat MAX_RECUR_TRIES");
                break;
            }
        }while(!found);
        grid[x2][y2].setLoc(new Point(x2*32,y2*32));
        history.remove(new Point(x2,y2));
        history.addFirst(new Point(x2,y2));

    }

    //Create a random location for puzzle, trap, mini boss, and other special room. (P T M Room)
    void placePTMR(Random random, Room[][] grid, Queue<Point> endpoint){
        for (Room[] rooms : grid){
            for(Room room : rooms){
                if(room.conected_room == 1 && room.type == RoomType.SINGLE) {
                    room.setSchem_name("trap");
                    Bukkit.broadcastMessage("FOUND EDGE!!");
                }
            }
        }
    }
    private void renderDungeon(Room[][] grid){
        for (int i = 0; i < p; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < l; j++) {
                try{
                    grid[i][j].loadScheme();
                    stringBuilder.append(grid[i][j].logo).append(", ");
                }catch (NullPointerException e){
                    stringBuilder.append(0).append(", ");
                }
            }
            Bukkit.broadcastMessage(stringBuilder.toString());
        }
    }
}
