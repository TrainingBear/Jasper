package me.jasper.jasperproject.JasperItem.Product.Utilities;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Heal;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class Healing_Wand extends Jitem implements Factory {
    public Healing_Wand(){
        super("Healing Wand", Material.BREEZE_ROD, Rarity.RARE, ItemType.WAND, 213454L, "HEALING_WAND");
        this.setMaxStack(1).getStats()
                .setBaseHealth(50)
                .setBaseMana(35)
                .setBaseDefense(10);
        this.getAbilities().add(new Heal(20,3));
    }

    @Override
    public Jitem create() {
        return this;
    }
}
