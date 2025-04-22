package me.jasper.jasperproject.Bazaar.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface ProductExecutor {
    void execute(Player pelaku, Inventory inventory, String product_name);
}
