package me.jasper.jasperproject.Bazaar.Component;

import me.jasper.jasperproject.Util.ContainerMenu.Content;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category implements Content {
    private static Map<Integer, Category> INSTANCE;
    public synchronized static Map<Integer, Category> getINSTANCE() {
        if(INSTANCE==null){
            INSTANCE = new HashMap<>();
        }
        return INSTANCE;
    }

    private int ID;
    private ItemStack item;

    public Category(int ID, Material material, Component component, byte taskID, List<Component> lore){
        this(ID, material, component, taskID, false, lore);
    }
    public Category(int ID, Material material, Component component, byte taskID){
        this(ID, material, component, taskID, false, null);
    }

    /**
     * @param ID ini harus unique, jan sama
     * @param material material
     * @param component Nama component
     * @param taskID kalo mau buat ID baru buatnya di TaskID.java
     * @param glint ench glint
     * @param lore List<Component>
     */
    public Category(int ID, Material material, Component component, byte taskID, boolean glint, @Nullable List<Component> lore){
        this.ID = ID;
        this.item = new ItemStack(material);
        item.editMeta(meta ->{
            meta.displayName(component);
            meta.getPersistentDataContainer().set(JKey.BAZAAR_COMPONENT_ID,PersistentDataType.INTEGER, ID);
            meta.getPersistentDataContainer().set(JKey.BAZAAR_COMPONENT_TASK_ID, PersistentDataType.BYTE, taskID);
            meta.getPersistentDataContainer().set(JKey.GUI_BORDER ,PersistentDataType.BOOLEAN, true);
            if(glint)meta.setEnchantmentGlintOverride(true);
            if(lore!=null) meta.lore(lore);
        });
        getINSTANCE().put(ID, this);
    }

    public String getName(){
        Component component = item.displayName();
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        String result = plain.replace(' ', '_');
        result = result.replaceAll("[\\[\\]{}]","");
        return result;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }
}
