package me.jasper.jasperproject.Bazaar.Bazaar2;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface InventoryUpdater {
    void update(Inventory inventory, final int ContentID, InventoryClickEvent e);
}
