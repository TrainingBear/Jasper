package me.jasper.jasperproject.JasperItem.Product;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemStats;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

import java.util.List;

public class TestItem extends Jitem implements Factory {

    public TestItem(){
        super("Test Item", Material.NETHERITE_AXE, Rarity.MYTHIC, ItemType.SWORD, 2363474L, "TEST");
        this.getStats()
                .setBaseCrit(100)
                .setBaseCritChance(100)
                .setBaseMana(100)
                .setBaseDamage(33)
                .setBaseSwingRange(100)
                .setBaseAttackSpeed(100);
        this.getStats().addModifiers(ItemStats.MODIFIER.crit_chance, 11);
        this.getStats().addModifiers(ItemStats.MODIFIER.Crit_damage, 20);
        this.getStats().addModifiers(ItemStats.MODIFIER.damage, 50f);
        this.getStats().addModifiers(ItemStats.MODIFIER.Damage, 10);

        this.getAbilities().add(new Teleport((short) 12, 0));
        this.getEnchants().add(ENCHANT.SharpnesV2);
        this.setUpgradeable(true);
        this.getCustom_lore().addAll(List.of(
                "",
                "This is the first item line",
                "This is the Second item line",
                "so on",
                ""

        ));
    }

    @Override
    public Jitem create() {
        return this;
    }
}
