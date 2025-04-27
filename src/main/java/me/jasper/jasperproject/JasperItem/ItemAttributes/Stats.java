package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Getter;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Stats {
    DAMAGE("⚔","Damage","<red>"),
    STRENGTH("❁","Strength","<color:#ff1e00>"),
    CRIT_DAMAGE("✴","Crit damage","<color:#6245ff>"),
    CRIT_CHANCE("✧","Crit Chance","<color:#8B76ff>"),
    MANA("✎","Mana","<color:#3f9fff>"),
    SPEED("➠","Speed","<color:#ff4fd0>"),
    ATTACK_SPEED("⥂","Attack speed","<yellow>"),
    DOUBLE_ATTACK("⫻","Double attack","<color:#ffB94C>"),
    SWING_RANGE("⌀","Swing range","<color:#ff8a63>"),
    DEFENCE("🛡","Defense","<color:#00ff3c>"),
    TRUE_DEFENCE("⛨","True defense","<color:#b5ff7f>"),
    HEALTH("❤","Health" , "<red>"),
    MENDING("☄","Mending","<color:#7aff6e>"),
    MAGIC_LUCK("☆","Magic Luck","<color:#87ffc5>"),
    MINING_SPEED("⛏","Mining Speed","<color:#ffcc00>"),
    DURABILITY("\uD83D\uDEE0","Durability","");

    @Getter private String symbol;
    @Getter private String name;
    @Getter private String color;
    @Getter private NamespacedKey key;
    Stats(String symbol, String name, String color){
        this.symbol = symbol;
        this.name = name;
        this.color = color;
        this.key = new NamespacedKey(JasperProject.getPlugin(), Util.escapeRegex(name).replaceAll(" ", ""));
    }

    public Component getColorAsCompo(){
        return MiniMessage.miniMessage().deserialize(color);
    }

    public static PersistentDataContainer toPDC(PersistentDataAdapterContext context, Map<Stats, Float> stats){
        PersistentDataContainer pdc = context.newPersistentDataContainer();
        for (Stats stat : stats.keySet()) {
            pdc.set(stat.key, PersistentDataType.FLOAT , stats.get(stat));
        }
        return pdc;
    }

    public static @Nullable Map<Stats, Float> fromItem(ItemStack item){
        Map<Stats, Float> stats = new HashMap<>();
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER);
        if(pdc==null) return null;
        for (Stats stat : Stats.values()) {
            NamespacedKey key = stat.getKey();
            if(pdc.has(key)){
                stats.put(stat, pdc.get(key, PersistentDataType.FLOAT));
            }
        }
        return stats;
    }

    public static List<Component> toLore(Map<Stats, Float> stats){
        List<Component> lore = new ArrayList<>();
        for (Stats stat : stats.keySet()) {
            lore.add(
                Util.deserialize("<!i><gray>"+stat.symbol+" "+stat.name+": "
                        +stat.color+Util.round(stats.get(stat),1)));
        }
        return lore;
    }
}
