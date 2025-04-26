package me.jasper.jasperproject.JasperItem.Product.Weapons;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

public class Titanium_Sword extends JItem implements Factory {
    public Titanium_Sword() {
        super("Titanium Sword", Material.IRON_SWORD, Rarity.UNCOMMON, ItemType.SWORD, 21147L, "TITANIUM_SWORD");
        this.getStats()
                .setBaseDamage(18)
                .setBaseStrength(10)
                .setBaseCrit(30)
                .setBaseCritChance(15)
                .setBaseAttackSpeed(3);
        this.getCustom_lore().addAll(List.of(
                MiniMessage.miniMessage().deserialize("<!i><dark_gray>This type of blade was once forged"),
                MiniMessage.miniMessage().deserialize("<!i><dark_gray>by the greatest blacksmith of his"),
                MiniMessage.miniMessage().deserialize("<!i><dark_gray>time, but nowadays this blade just"),
                MiniMessage.miniMessage().deserialize("<!i><dark_gray>an ordinary titanium blade"),
                MiniMessage.miniMessage().deserialize("")
        ));
    }
    @Override
    public JItem create() {
        return this;
    }
}
