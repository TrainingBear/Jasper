package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

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

     protected Room clone(){
        return new Room(this.name,this.type,this.ID,this.schema_name,this.logo);
    }

    void loadScheme(){
        loadScheme(null);
    }
    void loadScheme(@Nullable Point location){
        if(isLoaded){
            return;
        }
        if(location!=null){
            this.loadAndPasteSchematic(this.schema_name, BlockVector3.at(location.x, 70, location.y), this.rotation, true);
            Bukkit.broadcast(Util.deserialize(name +" loaded at = "+location.x +", "+70+", "+location.y).color(NamedTextColor.GOLD));
            isLoaded = true;
            return;
        }
        int x = (loc.x * 32) + locTranslate.x;
        int z = (loc.y * 32) + locTranslate.y;
        this.loadAndPasteSchematic(this.schema_name, BlockVector3.at(x, 70, z), this.rotation, true);
        Bukkit.broadcast(Util.deserialize(name +" loaded at = "+x +", "+70+", "+z).color(NamedTextColor.GOLD));
        isLoaded = true;
    }

    void addConection(Point current, Point neighbor){
        this.conected_room.computeIfAbsent(current, k-> new HashSet<>()).add(neighbor);
    }

    void addBody(Point point){
        this.body.add(point);
    }

}

