package me.jasper.jasperproject.JasperItem.Product.Tools;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Enchants.Sharpness;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class GraplingHook extends JItem implements Factory {

    public GraplingHook(){
        super("Grappling Hook", Material.FISHING_ROD, Rarity.COMMON, ItemType.ROD, 15L, "GRAPPLING_HOOK");
        this.setUpgradeable(false);
        this.getAbilities().add(new Grappling_Hook(1.5f));
        this.getEnchants().add(new Sharpness());
    }
    @Override
    public JItem create() {
        return this;
    }
}
