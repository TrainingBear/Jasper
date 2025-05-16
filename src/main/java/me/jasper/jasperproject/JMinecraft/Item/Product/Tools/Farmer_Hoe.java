package me.jasper.jasperproject.JMinecraft.Item.Product.Tools;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import net.kyori.adventure.text.Component;

public class Farmer_Hoe extends JItem implements Factory {
    public Farmer_Hoe() {
        super("Farmer Hoe", Material.IRON_HOE, Rarity.COMMON, ItemType.TOOL, "FARMER_HOE");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.FARMING_FORTUNE, 5f);
        stats.put(Stats.DURABILITY, 650f);
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
