package me.jasper.jasperproject.JMinecraft.Item.Product.Tools;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchants.Sharpness;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

public class GraplingHook extends JItem implements Factory {

    public GraplingHook(){
        super("Grappling Hook", Material.FISHING_ROD, Rarity.COMMON, ItemType.ROD,  "GRAPPLING_HOOK");
        this.setUpgradeable(false);
        this.getAbilities().add(new Grappling_Hook(1.5f));
        this.getEnchants().add(new Sharpness());
    }
    @Override
    public JItem create() {
        return this;
    }

    @Override
    protected List<Component> createLore() {
        return List.of();
    }
}
