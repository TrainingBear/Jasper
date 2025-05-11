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

public class Titanium_Boots extends JItem implements Factory {
    public Titanium_Boots(){
        super("Titanium Boots", Material.IRON_BOOTS, Rarity.UNCOMMON, ItemType.HELMET, "TITANIUM_BOOTS");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DEFENCE, 18f);
        stats.put(Stats.HEALTH, 9f);
        stats.put(Stats.MINING_SPEED, 5f);
        stats.put(Stats.DURABILITY, 500f);// melebihi diamond
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
