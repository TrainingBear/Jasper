package me.jasper.jasperproject.JasperEntity;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

public class EntityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if(strings.length == 0) return false;
        JasperEntity mob = new JasperEntity(EntityType.ZOMBIE, player.getLocation(), ChatColor.GRAY+"Zombies")
                .setDamage(5)
                .setAgroRange(10)
                .setKB_Resistance(2)
                .setMaxHealth(Double.parseDouble(strings[0]));
        return true;
    }
}
