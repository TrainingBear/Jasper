package me.jasper.jasperproject.JasperItem;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Warper;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemStats;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class Items {
    public static final Jitem EndGateway;
    static {
        EndGateway = new Jitem("End Gateway", Material.GOLDEN_SHOVEL, Rarity.EPIC, ItemType.SWORD, 2114L, "END_GATEWAY");
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
    public static final Jitem WarpGateway;
    static {
        WarpGateway = new Jitem("End Gateway", Material.GOLDEN_SHOVEL, Rarity.EPIC, ItemType.SWORD, 2114L, "END_GATEWAY");
        WarpGateway.getStats()
                .setBaseDamage(50)
                .setBaseMana(65)
                .setBaseSpeed(18)
                .setBaseAttackSpeed(10);
        WarpGateway.getAbilities().add(new Teleport(15, 0.2f));
        WarpGateway.getAbilities().add(new Warper(30));
        WarpGateway.update();
    }
    public static final Jitem grapling;
    static {
        grapling = new Jitem("Grappling Hook", Material.FISHING_ROD, Rarity.COMMON, ItemType.ROD, 13L, "GRAPPLING_HOOK");
        grapling.setUpgradeable(false);
        grapling.getAbilities().add(new Grappling_Hook(1.5f));
        grapling.getEnchants().add(ENCHANT.SharpnesV2);
        grapling.getLore().add("line1");
        grapling.update();
    }

    public static final Jitem test;
    static {
        test = new Jitem("Test Item",Material.NETHERITE_AXE, Rarity.MYTHIC, ItemType.SWORD, 2363474L, "TEST");
        test.getStats()
                .setBaseCrit(100)
                .setBaseCritChance(100)
                .setBaseMana(100)
                .setBaseDamage(33)
                .setBaseSwingRange(100)
                .setBaseAttackSpeed(100);
        test.getStats().addModifiers(ItemStats.MODIFIER.crit_chance, 11);
        test.getStats().addModifiers(ItemStats.MODIFIER.Crit_damage, 20);
        test.getStats().addModifiers(ItemStats.MODIFIER.damage, 50f);
        test.getStats().addModifiers(ItemStats.MODIFIER.Damage, 10);

        test.getAbilities().add(new Teleport((short) 12, 0));
        test.getEnchants().add(ENCHANT.SharpnesV2);
        test.setUpgradeable(true);
        test.getCustom_lore().addAll(List.of(
                "",
                "This is the first item line",
                "This is the Second item line",
                "so on",
                ""

        ));
        test.update();
    }

    public static final Jitem animate_wannd;
    static {
        animate_wannd = new Jitem("Blender", Material.DIAMOND_HORSE_ARMOR, Rarity.MYTHIC, ItemType.ITEM, 1132L, "ANIMATE");
        animate_wannd.getAbilities().add(new Animator());
        animate_wannd.getCustom_lore().addAll(
                List.of(
                        ChatColor.translateAlternateColorCodes('&',"&4&lWARNING!! THIS IS TEST ITEM"),
                        ChatColor.translateAlternateColorCodes('&',"&4&lMAY BE DELETED IN THE FUTURE"),
                        ""
                )
        );
        animate_wannd.update();
    }

    public static void sendItems(Player player, Jitem item){
        ItemStack itemStack = item.getItem();
        player.getInventory().addItem(itemStack);
    }

    public static void register(){
        grapling.setUpdateable(true);
        test.setUpdateable(true);
        EndGateway.setUpdateable(true);
        animate_wannd.setUpdateable(true);
        new Teleport().setShowCooldown(false);
        new Grappling_Hook().setShowCooldown(true);
        new Animator().register();
    }

}
