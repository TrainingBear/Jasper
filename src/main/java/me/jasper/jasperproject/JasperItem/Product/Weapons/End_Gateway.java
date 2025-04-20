package me.jasper.jasperproject.JasperItem.Product.Weapons;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.*;

import java.util.List;

public class End_Gateway extends Jitem implements Factory {
    @Getter private final static End_Gateway instance = new End_Gateway();
    public End_Gateway() {
        super("End Gateway", Material.GOLDEN_SHOVEL, Rarity.EPIC, ItemType.SWORD, 2114L, "END_GATEWAY");
        this.getStats()
                .setBaseDamage(39)
                .setBaseStrength(30)
                .setBaseMana(50)
                .setBaseSpeed(10)
                .setBaseAttackSpeed(5);
        this.getEnchants().addAll(List.of(
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel()));
        this.getAbilities().add(new Teleport(10, 0.2f));
    }

    @Override
    public Jitem create() {
        return this;
    }
}
