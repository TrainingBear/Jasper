package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;

import javax.xml.stream.Location;
import java.awt.*;

public class Room {
    String name;
    String type;
    int ID;
    String schem_name;
    Point loc = new Point(0,0);
    int rotation = 0;
    boolean isLoaded = false;

    public Room(String name, String type, int ID, String schem_name, Point loc){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schem_name = schem_name;
        this.loc = loc;
    }public Room(String name, String type, int ID, String schem_name){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schem_name = schem_name;
    }public Room(String name, String type, int ID, String schem_name, int Rotation){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schem_name = schem_name;
        this.rotation = Rotation;
    }
    void setName(String name){
        this.name = name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSchem_name(String schem_name) {
        this.schem_name = schem_name;
    }

    public void setLoc(Point loc) {
        this.loc = loc;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public Room clone(){
        return new Room(this.name,this.type,this.ID,this.schem_name);
    }
    public Room clone(Point loc){
        return new Room(this.name,this.type,this.ID,this.schem_name,loc);
    }

    public void loadSchem(int rot){
        if(isLoaded){
            return;
        }
        DungeonUtil util = new DungeonUtil();
        util.loadAndPasteSchematic(this.schem_name, new BlockVector3(loc.x, 70, loc.y),rot);
        isLoaded = true;
    }

    public void loadSchem(){
        if(isLoaded){
            return;
        }
        DungeonUtil util = new DungeonUtil();
        util.loadAndPasteSchematic(this.schem_name, new BlockVector3(loc.x, 70, loc.y), this.rotation);
//        Bukkit.broadcastMessage("Schematic "+this.schem_name+" pasted with a " + this.rotation + "° rotation! at: "+new BlockVector3(loc.x*32, 70, loc.y*32).toString());
        isLoaded = true;
    }
    public void loadSchem(int i, int j){
        if(isLoaded){
            return;
        }
        DungeonUtil util = new DungeonUtil();
        util.loadAndPasteSchematic(this.schem_name, new BlockVector3(i*32, 70, i*32), this.rotation);
        isLoaded = true;
    }
}
