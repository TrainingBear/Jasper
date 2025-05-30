package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JMinecraft.Entity.MobFactory;
import me.jasper.jasperproject.JMinecraft.Entity.MobPlayer.PlayerEntity;
import me.jasper.jasperproject.JMinecraft.Entity.MobRegistry;
import me.jasper.jasperproject.JMinecraft.Entity.Mobs.DreadLord;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Debug implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        if(strings.length==1){
            if(strings[0].equals("kill")){
                PlayerEntity.kill(player.getLocation());
                return true;
            }
            if(strings[0].equals("killall")){
                MobRegistry.getInstance().deregisterAll();
                return true;
            }
        }
        Location location = player.getLocation().clone();
        MobFactory factory = new DreadLord();
        factory.spawn(location);
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of("kill", "killall");
    }
}
