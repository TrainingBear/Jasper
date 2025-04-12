package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.Animation.Animation;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Debug implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        if(strings.length==2){
            Animation.setRegion(player, "a");
            return true;
        }
        if(strings.length==1) {
            Animation.stop(player, "a");

            return true;
        }
//        File schem = new File("C:\\Users\\user\\AppData\\Roaming\\.feather\\player-server\\servers\\7a1e3607-139e-4341-a6b9-6340739908da\\plugins\\JasperProject\\Animations\\a\\f15.schem");
//        Structure.render(schem, player.getLocation(), 10);
        Animation.play(player, "a");
        return true;
    }
}
