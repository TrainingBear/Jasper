package me.jasper.jasperproject.JMinecraft.Player;

import me.jasper.jasperproject.JMinecraft.Player.Ability.Mage;
import me.jasper.jasperproject.Util.JKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlayerBukkitCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        final Mage mage = new Mage();
        switch (strings[0]){
            case "add" -> {
                if (strings[1].equals("Mage")) {
                    PersistentDataContainer mage_holder = player.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
                    PersistentDataContainer ability_holder = player.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
                    ability_holder.set(Mage.Shoot.key,PersistentDataType.BOOLEAN , true);
                    mage_holder.set(Mage.key, PersistentDataType.TAG_CONTAINER, ability_holder);
                    player.getPersistentDataContainer().set(JKey.Ability, PersistentDataType.TAG_CONTAINER,
                            mage_holder
                    );
                    player.sendMessage("you has ability? "+player.getPersistentDataContainer().has(JKey.Ability));
                    player.sendMessage("you has Mage ability? "+player.getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER).has(Mage.key));
                    player.sendMessage("you has Shoot ability? "+player.getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER).get(Mage.key, PersistentDataType.TAG_CONTAINER).has(Mage.Shoot.key));
                }
            }
            case "remove" -> {
                if (strings[1].equals("Mage")) {
                    PersistentDataContainer pdc = player.getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER);
                    pdc.remove(Mage.key);
                    player.sendMessage("you has ability? "+player.getPersistentDataContainer().has(JKey.Ability));
                    player.sendMessage("you has Mage ability? "+player.getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER).has(Mage.key));
                }
            }
            case "repair" -> {
                player.getPersistentDataContainer().remove(JKey.Ability);
                player.getPersistentDataContainer().remove(JKey.Stats);
                player.sendMessage("you has ability? "+player.getPersistentDataContainer().has(JKey.Ability));
                player.sendMessage("you has Mage ability? "+player.getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER).has(mage.key));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length==2){
            return List.of("Mage");
        }
        return List.of("add", "remove", "repair");
    }
}
