package me.jasper.jasperproject.JasperItem.Product.Utilities;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Burnt;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

public class Stack_Steels extends JItem implements Factory {
    public Stack_Steels(){
        super("Stack Steels", Material.FLINT_AND_STEEL, Rarity.UNCOMMON, ItemType.ITEM,"STACK_STEELS");
        this.getAbilities().add(new Burnt(12f));
    }

    @Override
    protected List<Component> createLore() {
        return List.of();
    }

    @Override
    public JItem create() {
        return this;
    }
}
