package me.jasper.jasperproject.JasperItem.Product.Utilities;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Heal;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class Healing_Staff extends Jitem implements Factory {
    public Healing_Staff(){
        super("Healing Staff", Material.BREEZE_ROD, Rarity.EPIC, ItemType.STAFF, 2114L, "HEALING_STAFF");
        this.setMaxStack(1).getStats()
                .setBaseHealth(75)
                .setBaseMana(45)
                .setBaseDefense(15);
        this.getAbilities().add(new Heal(40,2));
    }
    @Override
    public Jitem create() {
        return this;
    }
}
