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

public class Execute {
    Random rand = new Random();
    @Setter
    long seed = rand.nextLong();
    @Setter
    int p = 4;
    @Setter
    int l = 4;
    int MAX_RECUR_TRIES = 1000;
    int OCCUR;

    Room entrance = new Room("Entrance","special",1,"entrance", 'E');
    Room fairy = new Room("Blood Room","special",2,"fairy", 'F');
    Room blood = new Room("Blood Room","special",3,"blood", 'B');
    Room path1 = new Room("PATH","path",4,"path1",'1');
    Room path2 = new Room("PATH2","path",5,"path2", '1');

    Map<Character,Integer> CURRENT_LIMIT = new HashMap<>(Map.of(
            '2', 0,
            '3', 0,
            '4', 0,
            '#', 0,
            'L', 0,
            '1', 0
    ));
    HashMap<Character,Integer> MAX_LIMIT = new HashMap<>(Map.of(
            '2', 2,
            '3', 1,
            '4', 1,
            '#', 1,
            'L', 1,
            '1', 10
    ));

    public void generate(){
        DungeonUtil util = new DungeonUtil();
        Random random = new Random(seed);
        double distance1,distance2,distance3;
        int x,y,x2,y2,x3,y3;
        Room[][] grid = new Room[p][l];
        Queue<Point> endpoint = new LinkedList<>();
        Stack<Point> history = new Stack<>();

        TextComponent message = new TextComponent("Dungeon Seed [" +ChatColor.GREEN+ seed +ChatColor.WHITE+"]");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(seed)));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy the seeds!")));
        Bukkit.spigot().broadcast(message);

        int tries = 0;
        do{
            x = random.nextInt(p);
            y = random.nextInt(l);
            x3 = random.nextInt(p);
            y3 = random.nextInt(l);
            distance3 = Point.distance(x,y,x3,y3);
            if(tries++ > MAX_RECUR_TRIES){
                Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. tries outbeat MAX_RECUR_TRIES");
                break;
            }
        }while (distance3 < p-1);

        grid[x][y] = entrance;
        grid[x][y].setLoc(new Point(x*32,y*32));
        grid[x3][y3] = blood;
        grid[x3][y3].setLoc(new Point(x3*32,y3*32));


        boolean found;
        Map<Point, Point> parrentMap = new HashMap<>();
        Map<Point, Point> parrentMap2 = new HashMap<>();
        do{
            parrentMap.clear();
            do{
                x2 = random.nextInt(p);
                y2 = random.nextInt(l);
                distance1 = Point.distance(x,y,x2,y2);
                distance2 = Point.distance(x2,y2,x3,y3);
                if(tries++ > MAX_RECUR_TRIES){
                    Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. tries outbeat MAX_RECUR_TRIES");
                    break;
                }
            }while (distance1 < ((double) p /2)-((double) p /4)+1 || distance2 < ((double) p /2)-((double) p /4)+1);
            grid[x2][y2] = fairy;
            found = util.findPath(new Point(x,y),new Point(x2,y2), new Point(x3,y3), grid, path1, path2, parrentMap, history);

            if(tries++ > MAX_RECUR_TRIES){
                Bukkit.broadcastMessage(ChatColor.RED+"Failed to load Dungeon. tries outbeat MAX_RECUR_TRIES");
                break;
            }
        }while(!found);
        grid[x2][y2].setLoc(new Point(x2*32,y2*32));

        history.remove(new Point(x2,y2));
        history.addFirst(new Point(x2,y2));
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < l; j++) {
                util.defineRoom(CURRENT_LIMIT, MAX_LIMIT,false, random, grid, i, j, path1, history, true);
                util.defineRoom(CURRENT_LIMIT, MAX_LIMIT,false, random, grid, i, j, path2, history, true);
            }
        }
        util.random_dir(CURRENT_LIMIT,MAX_LIMIT,random, grid, history, endpoint, parrentMap2);

        util.buildDoor(parrentMap, new Point(x,y), new Point(x3,y3), grid);
        for (Point end : endpoint){
            util.buildEmtyDoor(parrentMap2, end, grid);
        }



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
