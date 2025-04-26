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
    DAMAGE("⚔","Damage",MiniMessage.miniMessage().deserialize("<red>")),
    STRENGTH("❁","Strength",MiniMessage.miniMessage().deserialize("<color:#ff1e00>")),
    CRIT_DAMAGE("✴","Crit damage",MiniMessage.miniMessage().deserialize("<color:#6245ff>")),
    CRIT_CHANCE("✧","Crit Chance",MiniMessage.miniMessage().deserialize("<color:#8B76ff>")),
    MANA("✎","Mana",MiniMessage.miniMessage().deserialize("<color:#3f9fff>")),
    SPEED("➠","Speed",MiniMessage.miniMessage().deserialize("<color:#ff4fd0>")),
    ATTACK_SPEED("⥂","Attack speed",MiniMessage.miniMessage().deserialize("<yellow>")),
    DOUBLE_ATTACK("⫻","Double attack",MiniMessage.miniMessage().deserialize("<color:#ffB94C>")),
    SWING_RANGE("⌀","Swing range",MiniMessage.miniMessage().deserialize("<color:#ff8a63>")),
    DEFENCE("🛡","Defense",MiniMessage.miniMessage().deserialize("<color:#00ff3c>")),
    TRUE_DEFENCE("⛨","True defense",MiniMessage.miniMessage().deserialize("<color:#b5ff7f>")),
    HEALTH("❤","Health" , MiniMessage.miniMessage().deserialize("<red>"));

    @Getter private String symbol;
    @Getter private String name;
    @Getter private Component color;
    @Getter private NamespacedKey key;
    Stats(String symbol, String name, Component color){
        this.symbol = symbol;
        this.name = name;
        this.color = color;
        this.key = new NamespacedKey(JasperProject.getPlugin(), name);
    }

    public String getNameColor(){
        return MiniMessage.miniMessage().serialize(color);
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
        for (var entry : stats.entrySet()) {
            lore.add(
                Util.deserialize("<gray>"+entry.getKey().symbol+" "+entry.getKey().name+": "+entry.getKey().getColor()+format(entry.getValue()))
            );
        }
        return lore;
    }
    private static String format(float number){
        if (number == (int) number) return String.format("%d", (int) number);
        else return Float.toString(number);
    }
}
