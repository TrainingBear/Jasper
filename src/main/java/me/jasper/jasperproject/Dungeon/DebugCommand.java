package me.jasper.jasperproject.Dungeon;

import me.jasper.jasperproject.Dungeon.Shapes.FOUR_BY_FOUR;
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
    private FOUR_BY_FOUR four = new FOUR_BY_FOUR();
    private int anchor = 0;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        if (strings.length==0){
            Location location = player.getLocation();
            byte[][][] fourByFour = four.getFOUR_BY_FOUR();
            for (byte[] point : fourByFour[anchor]) {
                Location add = location.clone().add(point[0], 0, point[1]);
                Block block = add.getBlock();
                block.setType(Material.GREEN_WOOL);
                player.sendMessage(Arrays.toString(point));
            }
            player.sendMessage(anchor+" Pasted with rotation of "+four.getRotation());
            return true;
        }
        switch (strings[0]){
            case "rotate" -> {
                int anchor = Integer.parseInt(strings[1]);
                four.rotate(anchor);
                player.sendMessage("Rotated to "+four.getRotation());
            }
            case "anchor" -> {
                this.anchor = Integer.parseInt(strings[1]);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of("rotate", "anchor");
    }
}
