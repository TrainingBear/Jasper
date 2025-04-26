package me.jasper.jasperproject.JasperItem.Product.Weapons;

import me.jasper.jasperproject.JasperItem.ItemAttributes.*;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

public class TestItem extends JItem implements Factory {
    private static TestItem instance;
    public static TestItem getInstance(){
        if(instance==null){
            instance=new TestItem();
        }
        return instance;
    }
    public TestItem(){
        super("Test Items", Material.NETHERITE_AXE, Rarity.MYTHIC, ItemType.SWORD, 2363474L, "TEST");
        this.getStats()
                .setBaseCrit(100)
                .setBaseCritChance(100)
                .setBaseMana(100)
                .setBaseDamage(33)
                .setBaseStrength(100)
                .setBaseSwingRange(100)
                .setBaseAttackSpeed(100)
                .setBaseHealth(100)
                .setBaseDoubleAttack(100)
                .setBaseTrueDefense(100)
                .setBaseHealth(100)
                .setBaseDefense(100);
        this.getStats().addModifiers(StatsEnum.CRIT_CHANCE, 11);
        this.getStats().addModifiers(StatsEnum.Crit_damage, 20);
        this.getStats().addModifiers(StatsEnum.DAMAGE, 50f);
        this.getStats().addModifiers(StatsEnum.Damage, 10);

        this.getAbilities().add(new Teleport((short) 12, 0));
        this.getEnchants().add(ENCHANT.SharpnesV2);
        this.setUpgradeable(true);
        this.getCustom_lore().addAll(List.of(
                MiniMessage.miniMessage().deserialize("")
                ,MiniMessage.miniMessage().deserialize("This is the first item line")
                ,MiniMessage.miniMessage().deserialize("This is the Second item line")
                ,MiniMessage.miniMessage().deserialize("so on")
                ,MiniMessage.miniMessage().deserialize("")

        ));
    }

    @Override
    public JItem create() {
        return this;
    }
}
