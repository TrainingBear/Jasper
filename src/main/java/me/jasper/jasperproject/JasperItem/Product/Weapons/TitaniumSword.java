package me.jasper.jasperproject.JasperItem.Product.Weapons;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

public class TitaniumSword extends Jitem implements Factory {
    public TitaniumSword() {
        super("Titanium Sword", Material.IRON_SWORD, Rarity.UNCOMMON, ItemType.SWORD, 21147L, "TITANIUM_SWORD");
        this.getStats()
                .setBaseDamage(15)
                .setBaseCrit(30)
                .setBaseCritChance(15)
                .setBaseAttackSpeed(3);
        this.getCustom_lore().addAll(List.of(
                MiniMessage.miniMessage().deserialize("")
                ,MiniMessage.miniMessage().deserialize("")
        ));
    }
    @Override
    public Jitem create() {
        return this;
    }
}
