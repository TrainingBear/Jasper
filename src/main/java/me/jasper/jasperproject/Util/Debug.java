package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.Animation.Animation;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.ContainerMenu.Container;
import me.jasper.jasperproject.Util.ContainerMenu.ContentListener;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Debug implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;

        int[][] layout = {
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
        Border border = new Border(1, Material.BEDROCK, true);
        Container newGUI = new Container(player, layout);
        newGUI.addContent(border);
        newGUI.load();

        player.openInventory(newGUI.getContainer());

        player.sendMessage("contain:"+ ContentListener.contain(player.getInventory().getItemInMainHand().getItemMeta(), JKey.GUI_BORDER));
        return true;
    }
}
