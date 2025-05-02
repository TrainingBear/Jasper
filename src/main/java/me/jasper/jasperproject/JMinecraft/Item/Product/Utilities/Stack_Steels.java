package me.jasper.jasperproject.JMinecraft.Item.Product.Utilities;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Burnt;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
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
