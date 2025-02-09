package me.jasper.jasperproject;

import com.sk89q.worldedit.math.BlockVector3;
import me.jasper.jasperproject.Dungeon.DungeonUtil;
import me.jasper.jasperproject.Dungeon.Generator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ExecuteCommand extends DungeonUtil implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)){
            return false;
        }
        this.loadAndPasteSchematic("clear",new BlockVector3(48,70,48),0, false);
        Generator room = new Generator();
        if(strings.length == 0){
            room.generate();
            return true;
        }if(strings.length == 1){
            room.setSeed(Long.parseLong(strings[0]));
            room.generate();
            return true;
        }
        if(strings.length == 2){
            room.setL(Integer.parseInt(strings[0]));
            room.setP(Integer.parseInt(strings[1]));
            room.generate();
            return true;
        }
        if(strings.length == 3){
            room.setSeed(Long.parseLong(strings[2]));
            room.setL(Integer.parseInt(strings[0]));
            room.setP(Integer.parseInt(strings[1]));
            room.generate();
            return true;
        }
        return false;
    }
}
