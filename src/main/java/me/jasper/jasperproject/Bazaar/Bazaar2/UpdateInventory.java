package me.jasper.jasperproject.Bazaar.Bazaar2;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface UpdateInventory {
    void run(Inventory inventory, final int ContentID);
}
