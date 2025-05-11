package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchants.Sharpness;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import net.kyori.adventure.text.Component;
import org.bukkit.*;

import java.util.List;
import java.util.Map;

public class End_Gateway extends JItem implements Factory {
    @Getter private final static End_Gateway instance = new End_Gateway();
    public End_Gateway() {
        super("End Gateway", Material.GOLDEN_SHOVEL, Rarity.EPIC, ItemType.SWORD,  "END_GATEWAY");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DAMAGE, 39f);
        stats.put(Stats.STRENGTH, 30f);
        stats.put(Stats.MANA, 50f);
        stats.put(Stats.SPEED, 10f);
        stats.put(Stats.ATTACK_SPEED, 5f);
        this.setVersion(getVersion());
        this.getEnchants().addAll(List.of(new Sharpness()));
        this.getAbilities().add(new Teleport(10, 0.2f));
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
