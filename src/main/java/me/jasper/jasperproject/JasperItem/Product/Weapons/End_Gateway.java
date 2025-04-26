package me.jasper.jasperproject.JasperItem.Product.Weapons;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.*;

import java.util.List;
import java.util.Map;

public class End_Gateway extends JItem implements Factory {
    @Getter private final static End_Gateway instance = new End_Gateway();
    public End_Gateway() {
        super("End Gateway", Material.GOLDEN_SHOVEL, Rarity.EPIC, ItemType.SWORD, 2114L, "END_GATEWAY");
//                .setBaseDamage(39)
//                .setBaseStrength(30)
//                .setBaseMana(50)
//                .setBaseSpeed(10)
//                .setBaseAttackSpeed(5);
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DAMAGE, 39f);
        stats.put(Stats.STRENGTH, 30f);
        stats.put(Stats.MANA, 50f);
        stats.put(Stats.SPEED, 10f);
        stats.put(Stats.ATTACK_SPEED, 5f);

        this.getEnchants().addAll(List.of(
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel()));
        this.getAbilities().add(new Teleport(10, 0.2f));
    }

    @Override
    public JItem create() {
        return this;
    }
}
