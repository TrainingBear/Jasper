package me.jasper.jasperproject.JMinecraft.Item.Product.Tools;

import java.util.Map;

import org.bukkit.Material;

import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import net.kyori.adventure.text.Component;
import java.util.List;

public class Farmer_Scythe extends JItem implements Factory {
    public Farmer_Scythe() {
        super("Farmer Scythe", Material.IRON_HOE, Rarity.UNCOMMON, ItemType.TOOL, "FARMER_SCYTHE");
        Map<Stats,Float> stats = this.getStats();
        stats.put(Stats.FARMING_FORTUNE, 8f);
        stats.put(Stats.DURABILITY, 720f);
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
