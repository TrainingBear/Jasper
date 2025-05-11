package me.jasper.jasperproject.JMinecraft.Entity;

import me.jasper.jasperproject.JMinecraft.Entity.Mobs.JZombie;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Location location = player.getLocation();
        switch (strings[0]){
            case "zombie" -> {
                JZombie zombie = new JZombie(((CraftWorld) location.getWorld()).getHandle());
                zombie.getDelegate().spawn(location);
            }
        }
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of("zombie");
    }
}
