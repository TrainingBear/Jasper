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

    Room entrance = new Room("Entrance","special",1,"entrance", 'E');
    Room fairy = new Room("Fairy","special",2,"fairy", 'F');
    Room blood = new Room("Blood Room","special",3,"blood", 'B');
    Room path1 = new Room("PATH","path",4,"path1",'1');
    Room path2 = new Room("PATH2","path",5,"path2", '1');

    //room limit
    Map<Character,Integer> CURRENT_LIMIT = new HashMap<>(Map.of(
            '2', 0,
            '3', 0,
            '4', 0,
            '#', 0,
            'L', 0,
            '1', 0,
            'S', 0
    ));
    Rooms avaibleRooms = new Rooms();
    HashMap<Character,Integer> MAX_LIMIT = new HashMap<>(Map.of(
            '2', 2,
            '3', 1,
            '4', 1,
            '#', 1,
            'L', 1,
            '1', 10,
            'S', 3
    ));

    // main method
    public void generate(){
        random.setSeed(seed);
        Room[][] grid = new Room[p][l];
        Queue<Point> endpoint = new LinkedList<>();
        Stack<Point> history = new Stack<>();
        Map<Point, Point> parrentMap = new HashMap<>();

        TextComponent message = new TextComponent("Dungeon Seed [" +ChatColor.GREEN+ seed +ChatColor.WHITE+"]");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(seed)));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy the seeds!")));
        Bukkit.spigot().broadcast(message);
        long current_time = System.currentTimeMillis();

        placeMainDungeon(grid, parrentMap, history);

        //Parsing current path into the actual shaped rooms
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < l; j++) {
                this.defineRoom(avaibleRooms, CURRENT_LIMIT, MAX_LIMIT, random, grid, i, j, path1, history, false);
                this.defineRoom(avaibleRooms, CURRENT_LIMIT, MAX_LIMIT, random, grid, i, j, path2, history, false);
            }
        }
        this.buildDoor(parrentMap, new Point(x,y), new Point(x3,y3), grid);

        placePTMR(random, grid, endpoint);

        //Fill Dungeon room
        Map<Point, Point> parrentMap2 = new HashMap<>();
        this.random_dir(avaibleRooms, CURRENT_LIMIT,MAX_LIMIT,random, grid, history, endpoint, parrentMap2);


        for (Point end : endpoint){
            this.buildEmtyDoor(parrentMap2, end, grid);
        }

        Bukkit.broadcastMessage(ChatColor.GREEN+"Initializing dungeon took "+(System.currentTimeMillis()-current_time)+" ms");
        current_time = System.currentTimeMillis();
        renderDungeon(grid);
        Bukkit.broadcastMessage(ChatColor.GREEN+"Rendering dungeon took "+(System.currentTimeMillis()-current_time)+" ms");
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

        do{
            parrentMap.clear();
            do{
                x2 = random.nextInt(p);
                y2 = random.nextInt(l);
                distance1 = Point.distance(x,y,x2,y2);
                distance2 = Point.distance(x2,y2,x3,y3);
                if(OCCUR++ > MAX_RECUR_TRIES){
                    Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. OCCUR outbeat MAX_RECUR_TRIES");
                    break;
                }
            }while (distance1 < ((double) p /2)-((double) p /4)+1 || distance2 < ((double) p /2)-((double) p /4)+1);
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
    void placePTMR(Random random, Room[][] room, Queue<Point> endpoint){
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < l; j++) {
                if(avaibleRooms.SPECIAL.isEmpty()){
                    return;
                }
                if(this.suit(room, i, j)){
                    room[i][j] = avaibleRooms.SPECIAL.removeFirst();
                    room[i][j].setLoc(new Point(i*32, j*32));
                    Bukkit.broadcastMessage("placed "+room[i][j].type+" at "+i+", "+j);
//                endpoint.add(new Point(x, y));
                }
            }

        }
    }
    private void renderDungeon(Room[][] grid){
        for (int i = 0; i < p; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < l; j++) {
                try{
                    if(grid[i][j] == entrance){
                        grid[i][j].loadScheme();
                    }
                    if(grid[i][j] == blood){
                        grid[i][j].loadScheme();
                    }
                    if(grid[i][j] != null && (grid[i][j].ID != blood.ID || grid[i][j].ID != entrance.ID)){
                        grid[i][j].loadScheme();
                    }
                    stringBuilder.append(grid[i][j].logo).append(", ");
                }catch (NullPointerException e){
                    stringBuilder.append(0).append(", ");
                }
            }
            Bukkit.broadcastMessage(stringBuilder.toString());
        }
    }
}
