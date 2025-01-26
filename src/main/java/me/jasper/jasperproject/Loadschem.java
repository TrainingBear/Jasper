package me.jasper.jasperproject;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Loadschem {
    public Loadschem(Player player,int rotation, BlockVector3 location) {
        File file = new File("C:\\Users\\user\\AppData\\Roaming\\.feather\\player-server\\servers\\7a1e3607-139e-4341-a6b9-6340739908da\\plugins\\WorldEdit\\schematics\\air.schem");

        if (!file.exists()) {
            Bukkit.broadcastMessage("Schematic file not found.");
            player.sendMessage("Schematic file not found");
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
            transform = transform.rotateY(rotation);
            holder.setTransform(holder.getTransform().combine(transform));

            try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                    .world(BukkitAdapter.adapt(Bukkit.getWorld("test")))
                    .build()) {
                Operation operation = holder.createPaste(editSession)
                        .to(location)
                        .ignoreAirBlocks(false)
                        .build();

                Operations.complete(operation);
                Bukkit.broadcastMessage("Schematic pasted with a ° rotation!");
                player.sendMessage("Schematic pasted with a ° rotation!");
            }
        } catch (IOException | WorldEditException e) {
            Bukkit.broadcastMessage("Failed to load or paste schematic: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
