package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Burst_Arrow;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Burst_Bow extends JItem implements Factory {
    public Burst_Bow(){
        super("Burst Bow", Material.BOW, Rarity.RARE, ItemType.BOW,  "BURST_BOW");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DAMAGE, 40f);
        stats.put(Stats.STRENGTH, 20f);
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
