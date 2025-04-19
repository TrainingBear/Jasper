package me.jasper.jasperproject.Bazaar.Bazaar2;

import me.jasper.jasperproject.Bazaar.Bazaar2.Component.TaskID;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e){
        ItemStack curentItem = e.getCurrentItem();
        if(e.getClick().equals(ClickType.DOUBLE_CLICK)
                || !MiniMessage.miniMessage().serialize(e.getView().title()).startsWith((String) BazaarEnum.TITLE_STRING.get())
                || curentItem==null || !curentItem.hasItemMeta()) return;

        PersistentDataContainer container = e.getCurrentItem().getItemMeta().getPersistentDataContainer();

        if(contain(container, JKey.BAZAAR_COMPONENT_ID)){
            byte taskID = container.get(JKey.BAZAAR_COMPONENT_TASK_ID, PersistentDataType.BYTE);
            try {
                TaskID.MAP.get(taskID).update((Player) e.getWhoClicked(), e.getClickedInventory(), container.get(JKey.BAZAAR_COMPONENT_ID, PersistentDataType.INTEGER));
            }catch (NullPointerException ignored){}
        }

    }

    private boolean contain(PersistentDataContainer container, NamespacedKey key){
        return container.has(key);
    }
}
