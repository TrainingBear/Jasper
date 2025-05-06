package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Room extends DungeonUtil implements Cloneable{

    @Setter String name;
    @Setter RoomType type;
    @Setter int ID;
    @Setter String schema_name;
    @Setter Point loc = new Point(0,0);
    @Setter int rotation = 0;
    boolean isLoaded = false;
    @Setter char logo = 'N';
    List<Point> body = new ArrayList<>();
    @Setter Point foundIndexation = new Point(0,0);

    /**The Point, Point is refer to the grid[][] location. not the actual*/
    HashMap<Point, HashSet<Point>> conected_room = new HashMap<>();

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
        if(isLoaded){
            return;
        }
        this.loadAndPasteSchematic(this.schema_name, BlockVector3.at(loc.x, 70, loc.y), this.rotation, true);
        isLoaded = true;
    }

    void addConection(Point current, Point neighbor){
        this.conected_room.computeIfAbsent(current, k-> new HashSet<>()).add(neighbor);
    }

    void addBody(Point point){
        this.body.add(point);
    }

}

