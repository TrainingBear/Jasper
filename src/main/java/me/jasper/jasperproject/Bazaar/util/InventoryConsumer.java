package me.jasper.jasperproject.Bazaar.util;


import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryConsumer {
    void update(Player player, Inventory inventory, ItemStack clickedItem);

}
