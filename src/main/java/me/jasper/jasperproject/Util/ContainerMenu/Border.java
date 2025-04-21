package me.jasper.jasperproject.Util.ContainerMenu;

import lombok.Setter;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.Serializable;

public class Border implements Serializable, Content, Listener {
    private ItemStack border;
    private final int ID;

    public Border(int ID, Material material, boolean glint){
        this(ID, material, glint, MiniMessage.miniMessage().deserialize(""), true);
    }
    public Border(int ID, Material material, boolean glint, Component dispName, boolean hide_tooltip){
        this.ID = ID;
        if(material == Material.AIR) return;
        border = new ItemStack(material);
        border.editMeta(meta -> {
            meta.displayName(dispName);
            if(hide_tooltip) meta.setHideTooltip(true);
            if(glint) meta.setEnchantmentGlintOverride(true);
            meta.getPersistentDataContainer().set(JKey.GUI_BORDER, PersistentDataType.BOOLEAN, true);
        });
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
