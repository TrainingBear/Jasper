package me.jasper.jasperproject.Bazaar.Bazaar2;

import me.jasper.jasperproject.Util.JKey;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e){
        ItemStack curentItem = e.getCurrentItem();
        if(curentItem==null || !curentItem.hasItemMeta()) return;

        PersistentDataContainer container = e.getCurrentItem().getItemMeta().getPersistentDataContainer();
        if(contain(container, JKey.BAZAAR_COMPONENT_ID)){
            byte taskID = container.get(JKey.BAZAAR_COMPONENT_ID, PersistentDataType.BYTE);
            Bukkit.broadcastMessage("[Listener] Getting "+taskID);
            TaskID.MAP.get(taskID).update(e.getInventory(), container.get(JKey.BAZAAR_COMPONENT_TASK_ID, PersistentDataType.INTEGER));
        }

    }

    private boolean contain(PersistentDataContainer container, NamespacedKey key){
        return container.has(key);
    }
}
