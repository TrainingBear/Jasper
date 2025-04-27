package me.jasper.jasperproject.JasperItem;

import lombok.val;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Enchant;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemManager;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class JasperItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        val manager = ItemManager.getItems();
        if(strings[0].isEmpty()) player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Input the item name bruv"));
        switch(strings[0].toLowerCase()){
            case "debug" -> {
                long start = System.currentTimeMillis();
                ItemStack currentItem = player.getInventory().getItemInMainHand();
                player.getInventory().setItemInMainHand(null);
                JItem test = JItem.convertFrom(currentItem);
                for (Enchant enchant : test.getEnchants()) {
                    enchant.addLevel();
                }
                test.update();
                test.send(player);
                player.sendMessage("took "+(System.currentTimeMillis()-start)+" ms!");
            }
            case "update" -> {
                ItemManager.runUpdater();
            }
            default -> {
                try {
                    manager.get(strings[0].toUpperCase()).send(player);
                }catch (NullPointerException ignored){
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>There is no that kind of item"));
                }
            }
        }
        return true;
    }

    List<String> list = ItemManager.getItems().keySet().stream().toList();
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] strings) {
        return list.stream().filter(name -> name.contains(strings[0])).toList();
    }
}
