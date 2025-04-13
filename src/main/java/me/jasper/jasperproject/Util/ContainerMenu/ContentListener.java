package me.jasper.jasperproject.Util.ContainerMenu;

import me.jasper.jasperproject.Util.JKey;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ContentListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        ItemMeta meta=e.getCurrentItem().getItemMeta();
        boolean contain = contain(meta, JKey.GUI_BORDER);
        if(contain) e.setCancelled(true);
    }

    public static boolean contain(ItemMeta meta, NamespacedKey key){
        return meta.getPersistentDataContainer().has(key);
    }
}
