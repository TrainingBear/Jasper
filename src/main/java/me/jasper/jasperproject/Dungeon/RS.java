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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public enum RS {
    LShape0D {
        @Override
        public int getAngle() {
            return -90;
        }
        @Override
        public char getID() {
            return 'L';
        }
    },    //   []
    LShape_90D {
        @Override
        public int getAngle() {
            return -180;
        }
        @Override
        public char getID() {
            return 'L';
        }

    },  //   []
    LShape90D {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return 'L';
        }
    },   //   []
    LShape180D {
        @Override
        public int getAngle() {
            return 90;
        }
        @Override
        public char getID() {
            return 'L';
        }
    },  //   [] []  []

    a2x2Shape {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return '#';
        }
    },// #
    a1x1Shape {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return '1';
        }
    },// 1
    a2x1Shape {
        @Override
        public int getAngle() {
            return -90;
        }
        @Override
        public char getID() {
            return '2';
        }
    },// 2
    a1x2Shape {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return '2';
        }
    },// 2
    a3x1Shape {
        @Override
        public int getAngle() {
            return -90;
        }
        @Override
        public char getID() {
            return '3';
        }
    },// 3
    a1x3Shape {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return '3';
        }
    },// 3
    a4x1Shape {
        @Override
        public int getAngle() {
            return -90;
        }
        @Override
        public char getID() {
            return '4';
        }
    },// 4
    a1x4Shape {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return '4';
        }
    },// 4

    entrance {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return 'E';
        }
    }, // E
    fairy {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return 'F';
        }
    },    // F
    blood {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return 'B';
        }
    },    // B
    yellow {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return 'Y';
        }
    },   // Y
    puzzle {
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return 'P';
        }
    },   // P

    path{
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return '-';
        }
    },
    path2{
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return '+';
        }
    },
    clear{
        @Override
        public int getAngle() {
            return 0;
        }
        @Override
        public char getID() {
            return '0';
        }
    };





    public abstract int getAngle();
    public static BlockVector3 getPastepoint(int i, int j){
        Location pastepoint = new org.bukkit.Location(Bukkit.getWorld("test"),i*32,70,j*32);
        return new BlockVector3((int)pastepoint.getX(),(int)pastepoint.getY(),(int)pastepoint.getZ());
    }
    public static BlockVector3 getPastepoint(int i, int j,int id){
        if(id==1){
            Location pastepoint = new org.bukkit.Location(Bukkit.getWorld("test"),((i*32)+16),70,((j*32)-16));
            return new BlockVector3((int)pastepoint.getX(),(int)pastepoint.getY(),(int)pastepoint.getZ());
        }if(id==2){
            Location pastepoint = new org.bukkit.Location(Bukkit.getWorld("test"),((i*32)+16),70,((j*32)+16));
            return new BlockVector3((int)pastepoint.getX(),(int)pastepoint.getY(),(int)pastepoint.getZ());
        }if(id==3){
            Location pastepoint = new org.bukkit.Location(Bukkit.getWorld("test"),((i*32)-16),70,((j*32)-16));
            return new BlockVector3((int)pastepoint.getX(),(int)pastepoint.getY(),(int)pastepoint.getZ());
        }if(id==4){
            Location pastepoint = new org.bukkit.Location(Bukkit.getWorld("test"),((i*32)-16),70,((j*32)+16));
            return new BlockVector3((int)pastepoint.getX(),(int)pastepoint.getY(),(int)pastepoint.getZ());
        }
        Location pastepoint = new org.bukkit.Location(Bukkit.getWorld("test"),((i*32)+16),70,((j*32)+16));
        return new BlockVector3((int)pastepoint.getX(),(int)pastepoint.getY(),(int)pastepoint.getZ());
    }



    public abstract char getID();
    public static void loadAndPasteSchematic(String fileName, BlockVector3 location,int rotationDegrees) {
        File file = new File("C:\\Users\\user\\AppData\\Roaming\\.feather\\player-server\\servers\\7a1e3607-139e-4341-a6b9-6340739908da\\plugins\\WorldEdit\\schematics\\" + fileName + ".schem");

        if (!file.exists()) {
            Bukkit.broadcastMessage("Schematic file not found.");
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            Bukkit.broadcastMessage("Invalid schematic format.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             ClipboardReader reader = format.getReader(fis)) {
            Clipboard clipboard = reader.read();

            //rotating
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            AffineTransform transform = new AffineTransform();
            transform = transform.rotateY(rotationDegrees);
            holder.setTransform(holder.getTransform().combine(transform));

            try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                    .world(BukkitAdapter.adapt(Bukkit.getWorld("test")))
                    .build()) {
                Operation operation = holder.createPaste(editSession)
                        .to(location)
                        .ignoreAirBlocks(true)
                        .build();

                Operations.complete(operation);
//                Bukkit.broadcastMessage("Schematic "+fileName+" pasted with a " + rotationDegrees + "° rotation! at: "+location.toString());
            }
        } catch (IOException | WorldEditException e) {
            Bukkit.broadcastMessage("Failed to load or paste schematic: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
