package me.jasper.jasperproject.Bazaar.Bazaar2;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface InventoryUpdater {
    void update(Player player, Inventory inventory, final int ContentID);
}
