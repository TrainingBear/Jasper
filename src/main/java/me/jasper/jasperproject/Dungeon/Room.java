package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import lombok.Setter;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

public class Room extends DungeonUtil{
    @Setter String name;
    @Setter RoomType type;
    @Setter int ID;
    @Setter String schem_name;
    @Setter Point loc = new Point(0,0);
    @Setter int rotation = 0;boolean isLoaded = false;
    @Setter char logo = 'N';
    @Setter int[][] body;
    @Setter int[][] shape;
    @Setter Point foundIndexation = new Point(0,0);

    /**The Point, Point is refer to the grid[][] location. not the actual*/
    HashMap<Point, HashSet<Point>> conected_room = new HashMap<>();

    Room room = this;

    Room(String name, RoomType type, int ID, String schem_name, Point loc){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schem_name = schem_name;
        this.loc = loc;
    }
    Room(String name, RoomType type, int ID, String schem_name, char logo){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schem_name = schem_name;
        this.logo = logo;
    }

    //This replace the room identification but location
    void replace(Room room){
        this.name = room.name;
        this.type = room.type;
        this.ID = room.ID;
        this.schem_name = room.schem_name;
        this.logo = room.logo;
    }

     protected Room clone(){
        return new Room(this.name,this.type,this.ID,this.schem_name,this.logo);
    }

    //clone but also change the paste location
     Room clone(Point loc){
        return new Room(this.name,this.type,this.ID,this.schem_name,loc);
    }

    //this gonna load schema with custom rotation
     void loadScheme(int rot){
        if(isLoaded){
            return;
        }
        this.loadAndPasteSchematic(this.schem_name, new BlockVector3(loc.x, 70, loc.y),rot, true);
        isLoaded = true;
    }

     void loadScheme(){
        if(isLoaded){
//            Bukkit.broadcastMessage(this.schem_name+" is already loaded");
            return;
        }
        this.loadAndPasteSchematic(this.schem_name, new BlockVector3(loc.x, 70, loc.y), this.rotation, true);
//        Bukkit.broadcastMessage("Schematic "+this.schem_name+" pasted with a " + this.rotation + "° rotation! at: "+new BlockVector3(loc.x*32, 70, loc.y*32).toString());
        isLoaded = true;
    }
    //load schema for specific location
    void loadScheme(int i, int j){
        if(isLoaded){
            return;
        }
        this.loadAndPasteSchematic(this.schem_name, new BlockVector3(i*32, 70, i*32), this.rotation, true);
        isLoaded = true;
    }
    void addConection(Point current, Point neighbor){
        this.conected_room.putIfAbsent(current, new HashSet<>());
        this.conected_room.get(current).add(neighbor);
    }

    void decreaseConection(Point current, Point neighbor){
        this.conected_room.get(current).remove(neighbor);
    }
}
