package me.jasper.jasperproject.JMinecraft.Block;

import me.jasper.jasperproject.Bazaar.util.ProductManager;
import me.jasper.jasperproject.Dungeon.DungeonHandler;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.JasperEvent;
import me.jasper.jasperproject.JMinecraft.Item.Util.ItemManager;
import me.jasper.jasperproject.JMinecraft.Loot.Loot;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class LootableChest implements Listener {
    public final NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), this.getClass().getSimpleName());
    protected abstract Loot getLoot();
    protected abstract void onLoot(InventoryOpenEvent e);
    public void place(Location location){
        if (location.getBlock().getState() instanceof Chest chest) {
            chest.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
            chest.update();
        }
    }
    @EventHandler
    public void openchest(InventoryOpenEvent e){
        if (e.getInventory().getHolder() instanceof BlockInventoryHolder bholder) {
            if(bholder.getBlock().getState() instanceof Chest chest){
                PersistentDataContainer pdc = chest.getPersistentDataContainer();
                if(pdc.has(key)){
                    Inventory inventory = Bukkit.createInventory(e.getPlayer(), 27);
                    Map<String, Integer> roll = getLoot().roll();
                    Iterator<String> iterator = roll.keySet().iterator();
                    for (int i = 0; i < roll.size(); i++) {
                        int index = ThreadLocalRandom.current().nextInt(27);
                        String next = iterator.next();
                        ItemStack item = ItemManager.getItem(next);
                        item.setAmount(roll.get(next));
                        inventory.setItem(index, item);
                    }
                    e.getPlayer().openInventory(inventory);
                    chest.setType(Material.AIR);
                    onLoot(e);
                    e.setCancelled(true);
                }
            }
        }
    }
}
