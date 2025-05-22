package me.jasper.jasperproject.Jam2;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public class ClockExecutod implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String @NotNull [] args) {
        if(args != null) switch(args[0].toLowerCase()){
            case "start" ->{

            }
            case "move" ->{

            }
            case "stop" ->{

            }
            case "remove" ->{

            }
        }
        return false;
    }

    List<String> list = List.of(

    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] strings) {
        return list.stream().filter(name -> name.contains(strings[0].toUpperCase())).toList();
    }

}
