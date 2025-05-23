package me.jasper.jasperproject.Clock;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClockExecutod implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String @NotNull [] args) {
        if (args != null && sender instanceof Player pler) {
            switch (args[0].toLowerCase()) {
                case "start" -> Clock.start();
                case "move" -> Clock.move(pler);
                case "stop" -> Clock.stop();
                case "remove" -> Clock.remove();
                case "setup" -> Clock.setup(pler);
            }
            return true;
        }
        return false;
    }

    List<String> list = List.of(
            "start",
            "move",
            "stop",
            "remove",
            "setup");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] strings) {
        return list.stream().filter(name -> name.contains(strings[0].toLowerCase())).toList();
    }

}
