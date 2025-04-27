package me.jasper.jasperproject.JasperItem.Product.Weapons;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Warper;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Stats;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Warp_Gateway extends JItem implements Factory {
    public Warp_Gateway(){
        super("Warp Gateway", Material.DIAMOND_SHOVEL, Rarity.LEGENDARY, ItemType.SWORD, "WARP_GATEWAY");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DAMAGE, 45f);
        stats.put(Stats.STRENGTH, 36f);
        stats.put(Stats.MANA, 75f);
        stats.put(Stats.SPEED, 15f);
        stats.put(Stats.ATTACK_SPEED, 8f);
        this.getAbilities().add(new Teleport(12, 0.2f));
        this.getAbilities().add(new Warper(20,20));
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
