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

public class Titanium_Chestplate extends JItem implements Factory {
    public Titanium_Chestplate(){
        super("Titanium Chestplate", Material.IRON_CHESTPLATE, Rarity.UNCOMMON, ItemType.CHESTPLATE, "TITANIUM_CHESTPLATE");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DEFENCE, 20f);
        stats.put(Stats.HEALTH, 10f);
        stats.put(Stats.MINING_FORTUNE, 5f);
        stats.put(Stats.DURABILITY, 600f);// melebihi netherite
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
