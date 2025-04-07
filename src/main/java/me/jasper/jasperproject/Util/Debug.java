package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.Animation.Animation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Debug implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        if(strings.length==1) {
//            Animation.stop(player, "myanimation");

            return true;
        }
//        Animation.play(player, "myanimation");
        Structure.createBox(player);
        return true;
    }
}
