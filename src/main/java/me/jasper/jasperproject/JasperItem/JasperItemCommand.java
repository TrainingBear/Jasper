package me.jasper.jasperproject.JasperItem;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Util.ItemPatcher;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.JKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class JasperItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;

        switch(strings[0]){
            case "debug" -> {
                Items.register();
            }
            case "update" -> {
                try {
                    ItemPatcher.runJitemUpdater();
                } catch (IOException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            case "EndGateway" -> {
                Jitem test;
                test = Items.EndGateway.clone();
                test.send(player);
            }
            case "grapple"->{
                Jitem test;
                test = Items.grapling.clone();
                test.send(player);

            }case "test"->{
                Jitem test;
                test = Items.test.clone();
                test.send(player);

            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of(
                "grapple",
                "EndGateway",
                "WarpGateway",
                "test",
                "update"
                );
    }
}
