package me.jasper.jasperproject.Dungeon;

import me.jasper.jasperproject.Dungeon.Shapes.*;
import me.jasper.jasperproject.Dungeon.Shapes.Shape;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DebugCommand implements CommandExecutor, TabCompleter {
    Location location;
    private Shape four = new FOUR_BY_FOUR();
    private int anchor = 0;
    private Room room = CreatedRoom.FOUR.clone();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        int rot = four.getRotation().getOrDefault(anchor, 0);
        if (strings.length==0){
            Location location = player.getLocation();
            byte[][][] fourByFour = four.getShape();
            for (byte[] point : fourByFour[anchor]) {
                Location add = location.clone().add(point[0], 0, point[1]);
                Block block = add.getBlock();
                block.setType(Material.GREEN_WOOL);
                player.sendMessage(Arrays.toString(point));
            }
            player.sendMessage(anchor+" Pasted with rotation of "+ rot);
            return true;
        }
        switch (strings[0]){
            case "rotate" -> {
                int anchor = Integer.parseInt(strings[1]);
                four.rotate(anchor);
                Integer i = four.getRotation().get(anchor);
                player.sendMessage("Rotated to "+ i);
            }
            case "anchor" -> {
                this.anchor = Integer.parseInt(strings[1]);
            }
            case "setLocation" -> {
                this.location = player.getLocation();
            }
            case "setRoom" -> {
                player.sendMessage(room.getName());
            }
            case "paste" -> {
                Room clone = room.clone();
                Point pastePoint = four.getPastePoint(anchor);
                clone.setLoc(new Point(1, 1));
                clone.setLocTranslate(pastePoint);
                clone.setRotation(rot);
                clone.loadScheme(true);
            }
            case "setType" -> {
                switch (strings[1]){
                    case "FOUR" -> {
                        this.four = new FOUR_BY_FOUR();
                        this.room = CreatedRoom.FOUR.clone();
                    }
                    case "THREE" -> {
                        this.four = new THREE_BY_THREE();
                        this.room = CreatedRoom.THREE.clone();
                    }
                    case "TWO" -> {
                        this.four = new TOW_BY_TWO();
                        this.room = CreatedRoom.TWO.clone();
                    }
                    case "ONE" -> {
                        this.four = new ONE_BY_ONE();
                        this.room = CreatedRoom.SINGLE.clone();
                    }
                    case "BOX" -> {
                        this.four = new BOX_BY_BOX();
                        this.room = CreatedRoom.BOX.clone();
                    }
                    case "L" -> {
                        this.four = new L_BY_L();
                        this.room = CreatedRoom.L.clone();
                    }
                }
                player.sendMessage(strings[1]+" -> "+this.room.getName());
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 2 && strings[0].equals("setType")){
            return List.of("FOUR", "THREE", "TWO", "ONE", "L", "BOX");
        }
        return List.of("rotate", "anchor", "setLocation", "setRoom", "paste", "setType");
    }
}
