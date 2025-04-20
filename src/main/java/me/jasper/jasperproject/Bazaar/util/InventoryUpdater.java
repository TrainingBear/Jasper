package me.jasper.jasperproject.Bazaar.util;


import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface InventoryUpdater {
    void update(Player player, Inventory inventory, int ContentID);

}
