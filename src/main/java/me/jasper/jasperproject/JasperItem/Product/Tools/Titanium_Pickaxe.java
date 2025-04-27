package me.jasper.jasperproject.JasperItem.Product.Tools;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Stats;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
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
