package me.jasper.jasperproject.JMinecraft.Item.Product.Series.Titanium;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Titanium_Leggings extends JItem implements Factory {
    public Titanium_Leggings(){
        super("Titanium Leggings", Material.IRON_LEGGINGS, Rarity.UNCOMMON, ItemType.HELMET, "TITANIUM_LEGGINGS");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DEFENCE, 9f);
        stats.put(Stats.HEALTH, 8f);
        stats.put(Stats.MINING_FORTUNE, 4f);
        stats.put(Stats.DURABILITY, 580f);// melebihi diamond
    }
    @Override
    public JItem create(){
        return this;
    }

    @Override
    protected List<Component> createLore() {
        return List.of();
    }
}
