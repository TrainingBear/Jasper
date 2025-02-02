package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import lombok.Setter;

import java.awt.*;

public class Room {
    String name;
    @Setter
    String type;
    @Setter
    int ID;
    @Setter
    String schem_name;
    @Setter
    Point loc = new Point(0,0);
    @Setter
    int rotation = 0;
    boolean isLoaded = false;
    @Setter
    char logo = 'N';

    public Room(String name, String type, int ID, String schem_name, Point loc){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schem_name = schem_name;
        this.loc = loc;
    }
    public Room(String name, String type, int ID, String schem_name){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schem_name = schem_name;
    }public Room(String name, String type, int ID, String schem_name, char logo){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schem_name = schem_name;
        this.logo = logo;
    }
    public Room(String name, String type, int ID, String schem_name, int Rotation){
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.schem_name = schem_name;
        this.rotation = Rotation;
    }
    void setName(String name){
        this.name = name;
    }

    public Room clone(){
        return new Room(this.name,this.type,this.ID,this.schem_name,this.logo);
    }
    public Room clone(Point loc){
        return new Room(this.name,this.type,this.ID,this.schem_name,loc);
    }

    public void loadScheme(int rot){
        if(isLoaded){
            return;
        }
        DungeonUtil util = new DungeonUtil();
        util.loadAndPasteSchematic(this.schem_name, new BlockVector3(loc.x, 70, loc.y),rot, true);
        isLoaded = true;
    }

    public void loadScheme(){
        if(isLoaded){
            return;
        }
        DungeonUtil util = new DungeonUtil();
        util.loadAndPasteSchematic(this.schem_name, new BlockVector3(loc.x, 70, loc.y), this.rotation, true);
//        Bukkit.broadcastMessage("Schematic "+this.schem_name+" pasted with a " + this.rotation + "° rotation! at: "+new BlockVector3(loc.x*32, 70, loc.y*32).toString());
        isLoaded = true;
    }
    public void loadScheme(int i, int j){
        if(isLoaded){
            return;
        }
        DungeonUtil util = new DungeonUtil();
        util.loadAndPasteSchematic(this.schem_name, new BlockVector3(i*32, 70, i*32), this.rotation, true);
        isLoaded = true;
    }
}
