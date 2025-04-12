package me.jasper.jasperproject.JasperItem;

import lombok.val;
import me.jasper.jasperproject.JasperItem.Util.ItemManager;
import me.jasper.jasperproject.JasperItem.Util.ItemPatcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class JasperItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        val manager = ItemManager.getInstance().getItems();
        switch(strings[0]){
            case "debug" -> {
//                Items.register();
            }
            case "update" -> {
                try {
                    ItemPatcher.runJitemUpdater();
                } catch (IOException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            case "EndGateway" -> {
                manager.get("END_GATEWAY").send(player);
                player.sendMessage("Sending "+strings[0]+" item");
            }
            case "WarpGateway" ->{
                manager.get("END_GATEWAY").send(player);
                player.sendMessage("Sending "+strings[0]+" item");
            }
            case "grapple"->{
                manager.get("GRAPPLING_HOOK").send(player);
                player.sendMessage("Sending "+strings[0]+" item");
            }case "test"->{
                manager.get("TEST").send(player);
                player.sendMessage("Sending "+strings[0]+" item");
            }case "animate"->{
                manager.get("ANIMATE").send(player);
                player.sendMessage("Sending "+strings[0]+" item");
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
                "update",
                "animate"
                );
    }
}
