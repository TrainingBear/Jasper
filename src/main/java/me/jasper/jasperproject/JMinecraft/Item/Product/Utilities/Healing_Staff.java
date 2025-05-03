package me.jasper.jasperproject.JMinecraft.Item.Product.Utilities;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Heal;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Healing_Staff extends JItem implements Factory {
    public Healing_Staff(){
        super("Healing Staff", Material.BREEZE_ROD, Rarity.EPIC, ItemType.STAFF, "HEALING_STAFF");
        this.setVersion(getVersion());
        this.setMaxStack(1);
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.HEALTH, 75f);
        stats.put(Stats.MANA, 50f);
        stats.put(Stats.MENDING, 10f);
        stats.put(Stats.DEFENCE, 15f);
        this.getAbilities().add(new Heal(40,2));

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
