package me.jasper.jasperproject.JasperItem;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.NamespacedKey;

import java.awt.*;

public final class JKey {
    public static final NamespacedKey Main = new NamespacedKey(JasperProject.getPlugin(), "JasperItem");
    public static final NamespacedKey Stats = new NamespacedKey(JasperProject.getPlugin(), "item_stats");
    public static final NamespacedKey Ability = new NamespacedKey(JasperProject.getPlugin(), "item_ability");
    public static final NamespacedKey Enchant = new NamespacedKey(JasperProject.getPlugin(), "item_enchant");
    public static final NamespacedKey key_range = new NamespacedKey(JasperProject.getPlugin(), "range");
    public static final NamespacedKey key_cooldown = new NamespacedKey(JasperProject.getPlugin(), "cooldown");
    public static final NamespacedKey Version = new NamespacedKey(JasperProject.getPlugin(), "Version");
}
