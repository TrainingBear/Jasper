package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Test_Bow extends JItem implements Factory {
    public Test_Bow() {
        super("Admin Bow", Material.BOW, Rarity.MYTHIC, ItemType.BOW, "ADMIN-BOW");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DAMAGE, 10f);
        stats.put(Stats.STRENGTH, 600f);
        stats.put(Stats.CRIT_CHANCE, 50f);
        stats.put(Stats.CRIT_DAMAGE, 50f);
    }

    @Override
    protected List<Component> createLore() {
        return List.of();
    }

    @Override
    public JItem create() {
        return this;
    }
}
