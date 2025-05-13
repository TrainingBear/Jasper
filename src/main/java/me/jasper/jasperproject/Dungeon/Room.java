package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Getter
public class Room extends DungeonUtil implements Cloneable{
    @Setter private String name;
    @Setter private RoomType type;
    @Setter private int ID;
    @Setter private String schema_name;
    @Setter private Point loc = new Point(0,0);
    @Setter private Point locTranslate = new Point(0,0);
    @Setter private int rotation = 0;
    @Setter private boolean isLoaded = false;
    @Setter private char logo = 'N';
    private List<Point> body = new ArrayList<>();
    @Setter private Point foundIndexation = new Point(0,0);

    /**The Point, Point is refer to the grid[][] location. not the actual*/
    private final HashMap<Point, HashSet<Point>> conected_room = new HashMap<>();

    Room(String name, RoomType type, int ID, String schema_name, Point loc, List<Point> body){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schema_name = schema_name;
        this.loc = loc;
        this.body = body;
    }
    public Room(String name, RoomType type, int ID, String schema_name, char logo){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schema_name = schema_name;
        this.logo = logo;
    }

    //This replace the room identification but location
    void replace(Room room, boolean replace_body){
        this.name = room.name;
        this.type = room.type;
        this.ID = room.ID;
        this.schema_name = room.schema_name;
        this.logo = room.logo;
        this.body = replace_body? room.body : this.body;
    }

     public Room clone(){
         try {
             Room room = (Room) super.clone();
             List<Point> body = new ArrayList<>();
             for (Point point : this.getBody()) {
                 body.add((Point) point.clone());
             }
             room.body = body;
             room.loc = ((Point) this.loc.clone());
             room.locTranslate = ((Point) this.locTranslate.clone());
             room.type = this.type;
             room.foundIndexation = this.foundIndexation;
             return room;
         } catch (CloneNotSupportedException e) {
             throw new RuntimeException(e);
         }
    }

    void loadScheme(boolean debug){
        loadScheme(null, debug);
    }
    void loadScheme(){
        loadScheme(null, false);
    }
    void loadScheme(@Nullable Point location, boolean debug){
        if(isLoaded){
            return;
        }
        if(location!=null){
            this.loadAndPasteSchematic(this.schema_name, BlockVector3.at(location.x, 70, location.y), this.rotation, true);
            isLoaded = true;
            if(debug) Bukkit.broadcast(Component.text("Loaded "+this.getName()+" with rotation of "+this.rotation).color(NamedTextColor.YELLOW));
            return;
        }
        int x = (loc.x * 32) + locTranslate.x;
        int z = (loc.y * 32) + locTranslate.y;
        this.loadAndPasteSchematic(this.schema_name, BlockVector3.at(x, 70, z), this.rotation, true);
        if(debug) Bukkit.broadcast(Component.text("Loaded "+this.getName()+" with rotation of "+this.rotation).color(NamedTextColor.YELLOW));
        isLoaded = true;
    }

    void addConection(Point current, Point neighbor){
        this.conected_room.computeIfAbsent(current, k-> new HashSet<>()).add(neighbor);
    }

    void addBody(Point point){
        this.body.add(point);
    }

}

