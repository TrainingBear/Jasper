package me.jasper.jasperproject.JasperItem.Product.Utilities;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Heal;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Stats;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class Healing_Wand extends JItem implements Factory {
    public Healing_Wand(){
        super("Healing Wand", Material.BREEZE_ROD, Rarity.RARE, ItemType.WAND, "HEALING_WAND");
        this.setVersion(getVersion());
        this.setMaxStack(1);
        this.getStats().put(Stats.HEALTH, 50f);
        this.getStats().put(Stats.MANA, 35f);
        this.getStats().put(Stats.DEFENCE, 10f);
        this.getAbilities().add(new Heal(20,3));
    }

    @Override
    public JItem create() {
        return this;
    }
}
