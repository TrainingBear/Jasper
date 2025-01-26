package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class Execute {
    Random rand = new Random();
    long seed = rand.nextLong();
    public void setSeed(long seed) {
        this.seed = seed;
    }

    Room entrance = new Room("Entrance","special",1,"entrance");
    Room fairy = new Room("Blood Room","special",2,"fairy");
    Room blood = new Room("Blood Room","special",3,"blood");
    Room path1 = new Room("PATH","path",4,"path1");
    Room path2 = new Room("PATH2","path",5,"path2");
    public void generate(int p, int l){
        DungeonUtil util = new DungeonUtil();
        Random random = new Random(seed);
        double distance1,distance2,distance3;
        int x,y,x2,y2,x3,y3;
        Room[][] grid = new Room[p][l];

        do{
            x = random.nextInt(p);
            y = random.nextInt(l);
            x3 = random.nextInt(p);
            y3 = random.nextInt(l);
            distance3 = Point.distance(x,y,x3,y3);

        }while (distance3 < (p/2)+1);
        grid[x][y] = entrance;
        grid[x][y].setLoc(new Point(x*32,y*32));
        grid[x3][y3] = blood;
        grid[x3][y3].setLoc(new Point(x3*32,y3*32));

        do{
            x2 = random.nextInt(p);
            y2 = random.nextInt(l);
        }while (grid[x2][y2] != null);

        boolean found1 = false;
        boolean found2 = false;
        int tried = 0;
        int previousx,previousy;
        Map<Point, Point> parrentMap = new HashMap<>();
        do{
            parrentMap.clear();
            previousx = x2;
            previousy = y2;
            grid[previousx][previousy] = null;
            for (int i = 0; i < p; i++) {
                for (int j = 0; j < l; j++) {
                    if(grid[i][j] != null && grid[i][j].ID == path1.ID){
                        grid[i][j]=null;
                    }
                }
            }
            do{
                x2 = random.nextInt(p);
                y2 = random.nextInt(l);
                distance1 = Point.distance(x,y,x2,y2);
                distance2 = Point.distance(x2,y2,x3,y3);
            }while (distance1 < (double) p /2 || distance2 < (double) (p /2)+1);
            grid[x2][y2] = fairy;
            found1 = util.findPath(new Point(x,y), new Point(x2,y2), grid, path1, parrentMap);
//            Bukkit.broadcastMessage("path1 "+found1);
            found2 = util.findPath(new Point(x2,y2), new Point(x3,y3), grid, path2, parrentMap);
//            Bukkit.broadcastMessage("path2 "+found2);
            tried++;
            if(tried > 100){
                Bukkit.broadcastMessage("Break after "+tried+" tries, Please try again Later!");
                break;
            }
        }while(!found2);
        grid[x2][y2].setLoc(new Point(x2*32,y2*32));

        Stack<Point> queue = new Stack<>();
        Stack<Point> history = new Stack<>();
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < l; j++) {
                if(grid[i][j] != null && (grid[i][j].ID == path1.ID || grid[i][j].ID == path2.ID) ){
                    queue.add(new Point(i,j));
                    history.add(new Point(i,j));
                }
            }
        }

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < l; j++) {
                if(grid[i][j] != null && (grid[i][j].ID == path1.ID || grid[i][j].ID == path2.ID) ){
                    util.defineRoom(grid,i,j,path1);
                    util.defineRoom(grid,i,j,path2);
                }
            }
        }
        util.random_dir(grid,history);



//        Bukkit.broadcastMessage(x3+", "+y3 +" parrentmapsize: "+ history.size());
        util.buildDoor(parrentMap, new Point(x2,y2), new Point(x3,y3), grid, path2);
        util.buildDoor(parrentMap, new Point(x,y), new Point(x2,y2), grid, path1);


        for (int i = 0; i < p; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < l; j++) {
                try{
                    if(grid[i][j] == entrance){
                        grid[i][j].loadSchem(util.wichDirection(grid,
                                new Point(i,j),path1.ID));
                    }
                    if(grid[i][j] == blood){
                        grid[i][j].loadSchem(util.wichDirection(grid,
                                new Point(i,j),path2.ID));
                    }
                    if(grid[i][j] != null && (grid[i][j].ID != blood.ID || grid[i][j].ID != entrance.ID)){
                        grid[i][j].loadSchem();
                    }
                    stringBuilder.append(grid[i][j].ID).append(", ");
                }catch (NullPointerException e){
                    stringBuilder.append(0).append(", ");
                }
            }
            Bukkit.broadcastMessage(stringBuilder.toString());
        }
        TextComponent message = new TextComponent("Dungeon Seed [" +ChatColor.GREEN+ seed +ChatColor.WHITE+"]");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(seed)));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy the seeds!")));
        Bukkit.spigot().broadcast(message);
    }
}
