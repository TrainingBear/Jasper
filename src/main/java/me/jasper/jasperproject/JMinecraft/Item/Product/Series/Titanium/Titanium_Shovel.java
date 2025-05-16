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

public class Titanium_Shovel extends JItem implements Factory {
    public Titanium_Shovel() {
        super("Titanium Shovel", Material.IRON_SHOVEL, Rarity.UNCOMMON, ItemType.SHOVEL,"TITANIUM_SHOVEL");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.MINING_SPEED,45f);
        stats.put(Stats.DAMAGE,6f);
        stats.put(Stats.DURABILITY, 1930f);// melebihi diamond shovel
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