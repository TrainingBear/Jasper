package me.jasper.jasperproject.JasperItem.Product.Tools;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class Titanium_Pickaxe extends Jitem implements Factory {
    public Titanium_Pickaxe(){
        super("Titanium Pickaxe", Material.IRON_PICKAXE, Rarity.UNCOMMON, ItemType.PICKAXE,1121L,"TITANIUM_PICKAXE");
        //here is the blablablablablablalblabla
        //samehere maybe blabal
        //this one also blablablabla
    }
    @Override
    public Jitem create(){
        return this;
    }

}
