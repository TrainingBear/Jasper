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

public class Titanium_Pickaxe extends JItem implements Factory {
    public Titanium_Pickaxe(){
        super("Titanium Pickaxe", Material.IRON_PICKAXE, Rarity.UNCOMMON, ItemType.PICKAXE,"TITANIUM_PICKAXE");
        this.setVersion(getVersion());
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.MINING_SPEED,60f);
        stats.put(Stats.MINING_FORTUNE,13f);
        stats.put(Stats.DAMAGE,10f);
        stats.put(Stats.DURABILITY, 1930f);// melebihi diamond
        stats.put(Stats.BREAK_POWER,2f);
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
