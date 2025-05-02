package me.jasper.jasperproject.Bazaar.util;

import me.jasper.jasperproject.Bazaar.Component.TaskID;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onClick(@NotNull InventoryClickEvent e){
        ItemStack curentItem = e.getCurrentItem();
        if(e.getClick().equals(ClickType.DOUBLE_CLICK)
                || curentItem==null ) return;

        try {
            PersistentDataContainer container = e.getCurrentItem().getItemMeta().getPersistentDataContainer();
        if(!contain(container, JKey.BAZAAR_COMPONENT_TASK_ID)) return;
        Player whoClicked = (Player) e.getWhoClicked();
        if(contain(container, JKey.BAZAAR_PRODUCT)){
            String name = container.get(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING);
            byte taskID = container.get(JKey.BAZAAR_COMPONENT_TASK_ID, PersistentDataType.BYTE);
            whoClicked.sendMessage("You invoked "+taskID);
            TaskID.PRODUCT_MAP.get(taskID)
                    .execute(
                            whoClicked,
                            e.getClickedInventory(),
                            name
                    );
            return;
        }
        if(contain(container, JKey.BAZAAR_COMPONENT_ID)){
            byte taskID = container.get(JKey.BAZAAR_COMPONENT_TASK_ID, PersistentDataType.BYTE);
            TaskID.MAP.get(taskID)
                    .update(
                            whoClicked,
                            e.getClickedInventory(),
                            curentItem
                    );
        }
        }catch(NullPointerException ignore){}
    }

    private boolean contain(@NotNull PersistentDataContainer container, NamespacedKey key){
        return container.has(key);
    }
}
