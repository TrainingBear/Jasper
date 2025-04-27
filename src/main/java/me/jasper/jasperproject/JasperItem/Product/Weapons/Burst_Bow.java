package me.jasper.jasperproject.JasperItem.Product.Weapons;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Burst_Arrow;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Stats;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Burst_Bow extends JItem implements Factory {
    public Burst_Bow(){
        super("Burst Bow", Material.BOW, Rarity.RARE, ItemType.BOW, 1l, "BURST_BOW");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DAMAGE, 40f);
        stats.put(Stats.ATTACK_SPEED, 10f);
        stats.put(Stats.CRIT_DAMAGE, 40f);
        stats.put(Stats.CRIT_CHANCE, 5f);
        this.getAbilities().add(new Burst_Arrow(25,5));
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
