package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.Dungeon.Loot.TIER_ONE_CHEST;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import me.jasper.jasperproject.Util.FileConfiguration.Configurator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Getter
public class Room implements Cloneable{
    @Setter private String name;
    @Setter private RoomType type;
    private String schema_path;
    @Setter private Point loc = new Point(0,0);
    @Setter private Point locTranslate = new Point(0,0);
    @Setter private int rotation = 0;
    private boolean isLoaded = false;
    @Setter private char logo = 'N';
    private List<Point> body = new ArrayList<>();
    private final HashMap<Point, HashSet<Point>> connected_room = new HashMap<>();
    private int max_score = 0;
    private int score = 0;

    public Room(String name, RoomType type, String schem){
        this.name = name;
        this.type = type;
        this.schema_path = schem;
    }

    Room(String name, RoomType type, String schema_name, Point loc, List<Point> body){
        this.name = name;
        this.type = type;
        this.schema_path = schema_name;
        this.loc = loc;
        this.body = body;
    }

    @Deprecated
    public Room(String name, RoomType type, int id, String schema_name, char logo){
        this.name = name;
        this.type = type;
        this.schema_path = schema_name;
        this.logo = logo;
    }

    ///This replace the room identification but location
    void replace(Room room, boolean replace_body){
        this.name = room.name;
        this.type = room.type;
        this.schema_path = room.schema_path;
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
             return room;
         } catch (CloneNotSupportedException e) {
             throw new RuntimeException(e);
         }
    }

    void loadScheme(boolean debug){
        loadScheme(debug, "test");
    }

    void loadScheme(String instance_key){
        loadScheme(false, instance_key);
    }

    void loadScheme(boolean debug, String instance_key){
        if(isLoaded) return;
        int x = (loc.x * 32) + locTranslate.x;
        int z = (loc.y * 32) + locTranslate.y;
        Location location1 = new Location(Bukkit.getWorld(instance_key), x, 65, z);
        Structure.renderWFawe(new File(schema_path), location1, bs -> {
            if(bs instanceof Chest chest){
                max_score+=2;
                chest.getPersistentDataContainer().set(TIER_ONE_CHEST.INSTANCE.key, PersistentDataType.BOOLEAN, true);
                chest.update();
            }
        }, this.rotation);
        if(debug) Bukkit.broadcast(Component.text("Loaded "+this.getName()+" with rotation of "+this.rotation).color(NamedTextColor.YELLOW));
        isLoaded = true;
    }

    void addConnection(Point current, Point neighbor){
        this.connected_room.computeIfAbsent(current, k-> new HashSet<>()).add(neighbor);
    }

    void addBody(Point point){
        this.body.add(point);
    }

    public boolean isSingleDoor(){
        return this.type.equals(RoomType.TRAP) ||
        this.type.equals(RoomType.PUZZLE) ||
        this.type.equals(RoomType.START) ||
        this.type.equals(RoomType.END) ||
        this.type.equals(RoomType.END2) ||
        this.type.equals(RoomType.MINI_BOSS);
    }

    private void loadAndPasteSchematic(String fileName, BlockVector3 location, int rotationDegrees, boolean ignoreAir, String instance_key) {
        File file = new File("C:\\Users\\user\\AppData\\Roaming\\.feather\\player-server\\servers\\7a1e3607-139e-4341-a6b9-6340739908da\\plugins\\WorldEdit\\schematics\\" + fileName + ".schem");

        if (!file.exists()) {
            Bukkit.broadcastMessage(fileName+" file not found.");
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            Bukkit.broadcastMessage("Invalid schematic format.");
            return;
        }

        try (
                FileInputStream fis = new FileInputStream(file);
                ClipboardReader reader = format.getReader(fis)) {

            Clipboard clipboard = reader.read();
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            AffineTransform transform = new AffineTransform();
            transform = transform.rotateY(-rotationDegrees);
            holder.setTransform(holder.getTransform().combine(transform));
            try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                    .world(BukkitAdapter.adapt(Bukkit.getWorld(instance_key)))
                    .build()) {
                Operation operation = holder.createPaste(editSession)
                        .to(location)
                        .ignoreAirBlocks(ignoreAir)
                        .build();
                Operations.complete(operation);
            }
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }
}

