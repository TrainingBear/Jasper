package me.jasper.jasperproject.JasperItem.Product.Weapons;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Warper;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class Warp_Gateway extends JItem implements Factory {
    public Warp_Gateway(){
        super("Warp Gateway", Material.DIAMOND_SHOVEL, Rarity.LEGENDARY, ItemType.SWORD, 2114L, "WARP_GATEWAY");
        this.getStats()
                .setBaseDamage(45)
                .setBaseStrength(36)
                .setBaseMana(75)
                .setBaseSpeed(15)
                .setBaseAttackSpeed(8);
        this.getAbilities().add(new Teleport(12, 0.2f));
        this.getAbilities().add(new Warper(20,20));
    }

    @Override
    public JItem create() {
        return this;
    }
}
