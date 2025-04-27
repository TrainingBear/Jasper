package me.jasper.jasperproject.JasperItem.Util;

import me.jasper.jasperproject.Util.JKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtils {

    public static void playPSound(Player player, Sound sound, float volume, float pitch){
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static boolean hasAbility(ItemStack item){
        return  item != null &&
                item.hasItemMeta() &&
                item.getItemMeta().getPersistentDataContainer().has(JKey.Ability);
    }
    public static boolean hasAbility(ItemStack item, NamespacedKey key){
        return  item != null &&
                item.hasItemMeta() &&
                item.getItemMeta().getPersistentDataContainer().has(JKey.Ability) &&
                item.getItemMeta().getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER).has(key);
    }

    public static PersistentDataContainer getAbilityComp(ItemStack item, NamespacedKey ItemAbility){
        return item.getItemMeta().getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER)
                .get(ItemAbility, PersistentDataType.TAG_CONTAINER);
    }
    public static PersistentDataContainer getAbilityComp(ItemStack item){
        return item.getItemMeta().getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER);
    }

    public static boolean hasStats(ItemMeta item){
        return  item.getPersistentDataContainer().has(JKey.Stats);
    }
    public static boolean hasEnchants(ItemMeta item){
        return item.getPersistentDataContainer().has(JKey.ENCHANT);
    }


    public static PersistentDataContainer getStats(ItemMeta item){
        return item.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER);
    }

    public static boolean isInventoryEmpty(Inventory inv){
        for(ItemStack item : inv.getContents()) if(item != null && item.getAmount() > 0 ) return false;
        return true;
    }

}
