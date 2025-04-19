package me.jasper.jasperproject.JasperItem.Util;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Warper;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemStats;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;

import java.util.List;

public interface Factory {
    Jitem create();


//    public static Jitem createEndGateway() {
//        Jitem product = new Jitem("End Gateway", Material.GOLDEN_SHOVEL, Rarity.EPIC, ItemType.SWORD, 2114L, "END_GATEWAY");
//        product.getStats()
//                .setBaseDamage(39)
//                .setBaseMana(50)
//                .setBaseSpeed(10)
//                .setBaseAttackSpeed(5);
//        product.getEnchants().addAll(List.of(
//                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
//                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
//                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel(),
//                ENCHANT.SharpnesV2.addLevel(),ENCHANT.SharpnesV2.addLevel()));
//        product.getAbilities().add(new Teleport(10, 0.2f));
//        product.update();
//        return product;
//    }
//    public static Jitem createWarpGateway(){
//        Jitem product = new Jitem("Warp Gateway", Material.DIAMOND_SHOVEL, Rarity.EPIC, ItemType.SWORD, 2114L, "END_GATEWAY");
//        product.getStats()
//                .setBaseDamage(50)
//                .setBaseMana(65)
//                .setBaseSpeed(18)
//                .setBaseAttackSpeed(10);
//        product.getAbilities().add(new Teleport(10, 0.2f));
//        product.getAbilities().add(new Warper(20,20));
//        product.update();
//        return product;
//    }
//    public static Jitem createGraplingHook(){
//        Jitem product = new Jitem("Grappling Hook", Material.FISHING_ROD, Rarity.COMMON, ItemType.ROD, 15L, "GRAPPLING_HOOK");
//        product.setUpgradeable(false);
//        product.getAbilities().add(new Grappling_Hook(1.5f));
//        product.getEnchants().add(ENCHANT.SharpnesV2);
//        product.update();
//        return product;
//    }
//    public static Jitem createTest(){
//        Jitem test = new Jitem("Test Items",Material.NETHERITE_AXE, Rarity.MYTHIC, ItemType.SWORD, 2363474L, "TEST");
//        test.getStats()
//                .setBaseCrit(100)
//                .setBaseCritChance(100)
//                .setBaseMana(100)
//                .setBaseDamage(33)
//                .setBaseSwingRange(100)
//                .setBaseAttackSpeed(100);
//        test.getStats().addModifiers(ItemStats.MODIFIER.crit_chance, 11);
//        test.getStats().addModifiers(ItemStats.MODIFIER.Crit_damage, 20);
//        test.getStats().addModifiers(ItemStats.MODIFIER.damage, 50f);
//        test.getStats().addModifiers(ItemStats.MODIFIER.Damage, 10);
//
//        test.getAbilities().add(new Teleport((short) 12, 0));
//        test.getEnchants().add(ENCHANT.SharpnesV2);
//        test.setUpgradeable(true);
////        test.getCustom_lore().addAll(List.of(
////                "",
////                "This is the first item line",
////                "This is the Second item line",
////                "so on",
////                ""
////
////        ));
//        test.update();
//        return test;
//    }
//    public static Jitem createAnimationWand(){
//        Jitem animate_wannd = new Jitem("Blender", Material.DIAMOND_HORSE_ARMOR, Rarity.MYTHIC, ItemType.ITEM, 1132L, "ANIMATE");
//        animate_wannd.getAbilities().add(new Animator());
////        animate_wannd.getCustom_lore().addAll(
////                List.of(
////                        ChatColor.translateAlternateColorCodes('&',"&4&lWARNING!! THIS IS TEST ITEM"),
////                        ChatColor.translateAlternateColorCodes('&',"&4&lMAY BE DELETED IN THE FUTURE"),
////                        ""
////                )
////        );
//        animate_wannd.update();
//        return animate_wannd;
//    }
//
//    public static Jitem creatNewProduct(JitemFactory product){
//        return product.create();
//    }
//    interface JitemFactory{
//        Jitem create();
//    }
//
//    private void demo(){
//        creatNewProduct(() -> {
//            Jitem item = null;
//            return null;
//        });
//    }
}
