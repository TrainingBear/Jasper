package me.jasper.jasperproject.JasperItem.Product.Utilities;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Jumper;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class Feather_Jumper extends Jitem implements Factory {
    public Feather_Jumper(){
        super("Feather Jumper", Material.FEATHER, Rarity.UNCOMMON, ItemType.ITEM, 2342L,"FEATHER_JUMPER");
        this.getAbilities().add(new Jumper(5,1));
    }
    @Override
    public Jitem create(){
        return this;
    }
}
