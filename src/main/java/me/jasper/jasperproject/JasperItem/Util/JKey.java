package me.jasper.jasperproject.JasperItem.Util;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.NamespacedKey;

public final class JKey {
    public static final NamespacedKey Main = new NamespacedKey(JasperProject.getPlugin(), "JasperItem");
    public static final NamespacedKey CustomName = new NamespacedKey(JasperProject.getPlugin(), "CustomName");
    public static final NamespacedKey Stats = new NamespacedKey(JasperProject.getPlugin(), "item_stats");
    public static final NamespacedKey BaseStats = new NamespacedKey(JasperProject.getPlugin(), "baseStats");
    public static final NamespacedKey StatsModifier = new NamespacedKey(JasperProject.getPlugin(), "StatsModifier");
    public static final NamespacedKey Ability = new NamespacedKey(JasperProject.getPlugin(), "item_ability");
    public static final NamespacedKey Enchant = new NamespacedKey(JasperProject.getPlugin(), "item_enchant");
    public static final NamespacedKey Version = new NamespacedKey(JasperProject.getPlugin(), "Version");
    public static final NamespacedKey Rarity = new NamespacedKey(JasperProject.getPlugin(), "Rarity");
    public static final NamespacedKey BaseRarity = new NamespacedKey(JasperProject.getPlugin(), "BaseRarity");
    public static final NamespacedKey Category = new NamespacedKey(JasperProject.getPlugin(), "Category");
    public static final NamespacedKey RarityUpdated = new NamespacedKey(JasperProject.getPlugin(), "UpdatedOCCUR");
    public static final NamespacedKey UpgradeAble = new NamespacedKey(JasperProject.getPlugin(), "UpgradeAble");
    public static final NamespacedKey UnlimitedUpgradeAble = new NamespacedKey(JasperProject.getPlugin(), "UnlimitedUpgradeAble");
    public static final NamespacedKey Upgraded = new NamespacedKey(JasperProject.getPlugin(), "Upgraded");




    public static final NamespacedKey key_range = new NamespacedKey(JasperProject.getPlugin(), "range");
    public static final NamespacedKey key_cooldown = new NamespacedKey(JasperProject.getPlugin(), "cooldown");
    public static final NamespacedKey key_abilityCost = new NamespacedKey(JasperProject.getPlugin(), "ability_cost");
    public static final NamespacedKey key_damage = new NamespacedKey(JasperProject.getPlugin(), "damage");

    public static final NamespacedKey bazaar_Item_GUI = new NamespacedKey(JasperProject.getPlugin(), "Bazaar");
    public static final NamespacedKey bazaar_Action = new NamespacedKey(JasperProject.getPlugin(), "Bazaar-Action");
}
