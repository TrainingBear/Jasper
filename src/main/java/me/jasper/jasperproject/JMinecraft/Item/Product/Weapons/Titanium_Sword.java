package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Titanium_Sword extends JItem implements Factory {
    public Titanium_Sword() {
        super("Titanium Sword", Material.IRON_SWORD, Rarity.UNCOMMON, ItemType.SWORD,  "TITANIUM_SWORD");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DAMAGE, 18f);
        stats.put(Stats.STRENGTH, 10f);
        stats.put(Stats.CRIT_DAMAGE, 30f);
        stats.put(Stats.CRIT_CHANCE, 15f);
        stats.put(Stats.ATTACK_SPEED, 3f);

    }
    @Override
    public JItem create() {
        return this;
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                MiniMessage.miniMessage().deserialize("<!i><dark_gray>This type of blade was once forged"),
                MiniMessage.miniMessage().deserialize("<!i><dark_gray>by the greatest blacksmith of his"),
                MiniMessage.miniMessage().deserialize("<!i><dark_gray>time, but nowadays this blade just"),
                MiniMessage.miniMessage().deserialize("<!i><dark_gray>an ordinary titanium blade"),
                MiniMessage.miniMessage().deserialize("")
        );
    }
}
