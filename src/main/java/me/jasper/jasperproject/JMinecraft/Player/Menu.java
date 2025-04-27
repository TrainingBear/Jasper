package me.jasper.jasperproject.JMinecraft.Player;

import me.jasper.jasperproject.JMinecraft.Player.MenuContent.StatsDisplay;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.ContainerMenu.Container;
import me.jasper.jasperproject.Util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Menu {
    private static final Map<UUID, Inventory> instance = new HashMap<>();
    private static final int[] layout = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
    };
    public static void open(Player player){
        if(true || !instance.containsKey(player.getUniqueId())){
            Container menu = new Container(player, Util.deserialize("<gold><bold>Player profile"), layout);
            menu.addContent(
                    List.of(
                            new Border(0, Material.BROWN_STAINED_GLASS_PANE, false),
                            new StatsDisplay(1, player)
                    )
            );
            menu.load();
            instance.put(player.getUniqueId(), menu.getContainer());
        }
        player.openInventory(instance.get(player.getUniqueId()));
    }
}
