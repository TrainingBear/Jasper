package me.jasper.jasperproject.JasperItem.Product.Weapons;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Burst_Arrow;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class Burst_Bow extends Jitem implements Factory {
    public Burst_Bow(){
        super("Burst Bow", Material.BOW, Rarity.RARE, ItemType.BOW, 2114L, "BURST_BOW");
        this.getStats()
                .setBaseDamage(15)
                .setBaseAttackSpeed(3)
                .setBaseCrit(30)
                .setBaseCritChance(10);
        this.getAbilities().add(new Burst_Arrow(25,5));
    }

    @Override
    public Jitem create() {
        return this;
    }
}
