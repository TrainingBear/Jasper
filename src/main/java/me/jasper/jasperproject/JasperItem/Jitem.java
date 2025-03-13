package me.jasper.jasperproject.JasperItem;

import lombok.Getter;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Jitem {
    private ItemStack item;
    private Rarity rarity;
    private ItemMeta meta;
    @Getter private ItemStats stats;
    @Getter private List<ENCHANT> enchants;
    @Getter private List<ItemAbility> abilities;

    public Jitem(Material material, Rarity rarity){
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public void addLore(String lore){
        meta.getLore().add(lore);
    }

    public void applyItemStats() throws IllegalAccessException {
        meta.getPersistentDataContainer()
                .set(
                        new NamespacedKey(JasperProject.getPlugin(), "item_stats"),
                        PersistentDataType.TAG_CONTAINER,
                        stats.getStatsContainer(meta.getPersistentDataContainer())
                );
        item.setItemMeta(this.meta);
        meta.setLore(stats.getLore());
    }

    public void applyItemEnchantments(){
        if(enchants.isEmpty()) return;
        meta.getPersistentDataContainer()
                .set(
                        new NamespacedKey(JasperProject.getPlugin(), "item_enchants"),
                        PersistentDataType.TAG_CONTAINER,
                        getItem_enchantsValue(meta.getPersistentDataContainer())
                );
    }

    public void applyItemAbilities(){
        if(abilities.isEmpty()) return;
    }

    private PersistentDataContainer getItem_enchantsValue(PersistentDataContainer data){
        PersistentDataContainer container = data.getAdapterContext().newPersistentDataContainer();

        enchants.forEach(enchant -> container.set(
                    new NamespacedKey(JasperProject.getPlugin(), enchant.name),
                    PersistentDataType.BYTE, enchant.level
                )
        );
        return container;
    }
}
