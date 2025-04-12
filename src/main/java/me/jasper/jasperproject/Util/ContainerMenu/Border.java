package me.jasper.jasperproject.Util.ContainerMenu;

import lombok.Setter;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.Serializable;

public class Border implements Serializable, Content, Listener {
    @Setter private ItemStack border;
    private final int ID;

    public Border(int ID, Material material, boolean glint){
        this.ID = ID;
        border = new ItemStack(material);
        ItemMeta meta = border.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(""));
        meta.setHideTooltip(true);
        if(glint) meta.setEnchantmentGlintOverride(true);
        meta.getPersistentDataContainer().set(JKey.GUI_BORDER, PersistentDataType.BOOLEAN, true);
        border.setItemMeta(meta);
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public ItemStack getItem() {
        return border;
    }
}
