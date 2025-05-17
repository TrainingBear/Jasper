package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JMinecraft.Block.LootableChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R3.block.data.type.CraftChest;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Debug implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        if(strings.length==1){

            return true;
        }

        Block block = player.getLocation().getBlock();
        block.setType(Material.CHEST);
        Chest block1 = (Chest) block.getState();
//        block1.getPersistentDataContainer().set(LootableChest.key, PersistentDataType.BOOLEAN, true);
        block1.update();
        return true;
    }

}
