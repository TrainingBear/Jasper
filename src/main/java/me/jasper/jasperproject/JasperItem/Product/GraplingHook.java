package me.jasper.jasperproject.JasperItem.Product;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class GraplingHook extends Jitem implements Factory {

    public GraplingHook(){
        super("Grappling Hook", Material.FISHING_ROD, Rarity.COMMON, ItemType.ROD, 15L, "GRAPPLING_HOOK");
        this.setUpgradeable(false);
        this.getAbilities().add(new Grappling_Hook(1.5f));
        this.getEnchants().add(ENCHANT.SharpnesV2);
        this.getLore().add("line1");
    }
    @Override
    public Jitem create() {
        return this;
    }
}
