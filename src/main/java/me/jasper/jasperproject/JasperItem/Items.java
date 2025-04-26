package me.jasper.jasperproject.JasperItem;

import me.jasper.jasperproject.JasperItem.ItemAttributes.*;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Warper;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Deprecated
public final class Items {
    public static final JItem EndGateway;
    static {
        EndGateway = new JItem("End Gateway", Material.GOLDEN_SHOVEL, Rarity.EPIC, ItemType.SWORD, 2114L, "END_GATEWAY");
        EndGateway.getStats()
                .setBaseDamage(39)
                .setBaseMana(50)
                .setBaseSpeed(10)
                .setBaseAttackSpeed(5);
        EndGateway.getEnchants().addAll(List.of(
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel()));
        EndGateway.getAbilities().add(new Teleport(10, 0.2f));
        EndGateway.update();
    }
    public static final JItem WarpGateway;
    static {
        WarpGateway = new JItem("Warp Gateway", Material.DIAMOND_SHOVEL, Rarity.EPIC, ItemType.SWORD, 2114L, "END_GATEWAY");
        WarpGateway.getStats()
                .setBaseDamage(50)
                .setBaseMana(65)
                .setBaseSpeed(18)
                .setBaseAttackSpeed(10);
        WarpGateway.getAbilities().add(new Teleport(10, 0.2f));
        WarpGateway.getAbilities().add(new Warper(20,20));
        WarpGateway.update();
    }
    public static final JItem grapling;
    static {
        grapling = new JItem("Grappling Hook", Material.FISHING_ROD, Rarity.COMMON, ItemType.ROD, 3565L, "GRAPPLING_HOOK");
        grapling.setUpgradeable(false);
        grapling.getAbilities().add(new Grappling_Hook(1.5f));
        grapling.getEnchants().add(ENCHANT.SharpnesV2);
        grapling.update();
    }

    public static final JItem test;
    static {
        test = new JItem("Test Items",Material.NETHERITE_AXE, Rarity.MYTHIC, ItemType.SWORD, 2363474L, "TEST");
        test.getStats()
                .setBaseCrit(100)
                .setBaseCritChance(100)
                .setBaseMana(100)
                .setBaseDamage(33)
                .setBaseSwingRange(100)
                .setBaseAttackSpeed(100);
        test.getStats().addModifiers(StatsEnum.CRIT_CHANCE, 11);
        test.getStats().addModifiers(StatsEnum.Crit_damage, 20);
        test.getStats().addModifiers(StatsEnum.DAMAGE, 50f);
        test.getStats().addModifiers(StatsEnum.Damage, 10);

        test.getAbilities().add(new Teleport((short) 12, 0));
        test.getEnchants().add(ENCHANT.SharpnesV2);
        test.setUpgradeable(true);
        test.getCustom_lore().addAll(List.of(
                MiniMessage.miniMessage().deserialize("")
                ,MiniMessage.miniMessage().deserialize("This is the first item line")
                ,MiniMessage.miniMessage().deserialize("This is the Second item line")
                ,MiniMessage.miniMessage().deserialize("so on")
                ,MiniMessage.miniMessage().deserialize("")
        ));
        test.update();
    }

    public static final JItem animate_wannd;
    static {
        animate_wannd = new JItem("Blender", Material.DIAMOND_HORSE_ARMOR, Rarity.MYTHIC, ItemType.ITEM, 1132L, "ANIMATE");
        animate_wannd.getAbilities().add(new Animator());
//        animate_wannd.getCustom_lore().addAll(
//                List.of(
//                        ChatColor.translateAlternateColorCodes('&',"&4&lWARNING!! THIS IS TEST ITEM"),
//                        ChatColor.translateAlternateColorCodes('&',"&4&lMAY BE DELETED IN THE FUTURE"),
//                        ""
//                )
//        );
        animate_wannd.update();
    }

    public static void sendItems(Player player, JItem item){
        ItemStack itemStack = item.getItem();
        player.getInventory().addItem(itemStack);
    }

}
