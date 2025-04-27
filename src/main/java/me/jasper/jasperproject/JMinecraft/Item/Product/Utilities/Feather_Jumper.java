package me.jasper.jasperproject.JMinecraft.Item.Product.Utilities;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Jumper;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

public class Feather_Jumper extends JItem implements Factory {
    public Feather_Jumper(){
        super("Feather Jumper", Material.FEATHER, Rarity.UNCOMMON, ItemType.ITEM, "FEATHER_JUMPER");
        this.getAbilities().add(new Jumper(5,1));
    }
    @Override
    public JItem create(){
        return this;
    }

    @Override
    protected List<Component> createLore() {
        return List.of();
    }
}
