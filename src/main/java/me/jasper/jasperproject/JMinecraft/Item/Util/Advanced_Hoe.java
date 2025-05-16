package me.jasper.jasperproject.JMinecraft.Item.Util;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Plower;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import net.kyori.adventure.text.Component;

public class Advanced_Hoe extends JItem implements Factory {
    public Advanced_Hoe() {
        super("Advanced Hoe", Material.DIAMOND_HOE, Rarity.RARE, ItemType.TOOL, "ADVANCED_HOE");
        Map<Stats,Float> stats = this.getStats();
        stats.put(Stats.FARMING_FORTUNE, 10f);
        stats.put(Stats.DURABILITY, 1000f);
        this.getAbilities().add(new Plower(5f));
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
