package me.jasper.jasperproject.JasperItem;

import me.jasper.jasperproject.JasperItem.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.Abilities.Teleport;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class JasperItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;

        switch(strings[0]){
            case "EndGateway" -> {
                Jitem test;
                test = Items.EndGateway.clone();
                test.send(player);
            }
            case "grapple"->{
                Jitem test;
                test = Items.grapling.clone();
                test.send(player);

            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of(
                "grapple",
                "EndGateway");
    }
}
