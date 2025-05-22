package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JMinecraft.Entity.Mobs.JZombie;
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
        if (!(commandSender instanceof Player player)) return false;
        JZombie zombie = new JZombie(player.getWorld());
        zombie.getDelegate().spawn(player.getLocation());
        return true;
    }

}
