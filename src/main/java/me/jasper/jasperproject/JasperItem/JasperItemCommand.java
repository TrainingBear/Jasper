package me.jasper.jasperproject.JasperItem;

import lombok.val;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Enchant;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemManager;
import me.jasper.jasperproject.JasperItem.Util.ItemPatcher;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.Component;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JasperItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        val manager = ItemManager.getInstance().getItems();
        if(strings[0].isEmpty()) player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Input the item name bruv"));
        switch(strings[0].toLowerCase()){
            case "debug" -> {
                ItemStack currentItem = player.getInventory().getItemInMainHand();
                PersistentDataContainer container = currentItem.getItemMeta().getPersistentDataContainer();
                boolean b = !container.has(JKey.Ability);
                player.sendMessage(String.valueOf(b));
                container = container.get(JKey.Ability, PersistentDataType.TAG_CONTAINER);
                for (ItemAbility ability : ItemManager.getInstance().getAbilities()) {
                    if(container.has(ability.getKey())){
                        PersistentDataContainer pdc = container.get(ability.getKey(), PersistentDataType.TAG_CONTAINER);
                        player.sendMessage(ability.getKey().toString() +" -> "+pdc.get(JKey.key_range, PersistentDataType.INTEGER)+ " ");
                    }
                }
                player.getInventory().setItemInMainHand(null);

                JItem abc = JItem.convertFrom(currentItem, List.of(Component.text("abc")));
                for (Enchant enchant : abc.getEnchants()) {
                    enchant.addLevel();
                }
                abc.send(player);
            }
            case "update" -> {
                try {
                    ItemPatcher.runJitemUpdater();
                } catch (IOException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
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

    List<String> list = ItemManager.getInstance().getItems().keySet().stream().toList();
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] strings) {
        return list.stream().filter(name -> name.contains(strings[0])).toList();
    }
}
