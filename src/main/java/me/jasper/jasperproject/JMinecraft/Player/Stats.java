package me.jasper.jasperproject.JMinecraft.Player;

import lombok.Getter;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.*;

public enum Stats {
    DAMAGE("⚔","Damage","<red>", 1),
    STRENGTH("❁","Strength","<color:#ff1e00>", 0),
    CRIT_DAMAGE("✴","Critical damage","<color:#6245ff>", 0),
    CRIT_CHANCE("✧","Critical Chance","<color:#8B76ff>", 0),
    MANA("✎","Mana","<color:#3f9fff>", 100),
    SPEED("➠","Speed","<color:#ff4fd0>", 100),
    ATTACK_SPEED("⥂","Attack speed","<yellow>", 0),
    DOUBLE_ATTACK("⫻","Double attack","<color:#ffB94C>", 0),
    SWING_RANGE("⌀","Swing range","<color:#ff8a63>", 4),
    DEFENCE("🛡","Defense","<color:#00ff3c>", 1),
    TRUE_DEFENCE("⛨","True defense","<color:#b5ff7f>", 0),
    HEALTH("❤","Health" , "<red>", 20),
    MENDING("☄","Mending","<color:#7aff6e>", 10),
    MAGIC_LUCK("☆","Magic Luck","<color:#87ffc5>", 0),
    MINING_SPEED("⛏","Mining Speed","<color:#ffcc00>", 0),
    DURABILITY("\uD83D\uDEE0","Durability","", -1),
    REPAIR("🔨","Repair","<color:#33ff7e>", 0),

    POISON("💀","Posion","<color:#00c20d>", 0),

    MINING_SPREAD("☄","Mining Spread","<color:#ffea00>", 0),

    COMBAT_WISDOM("🧠","Combat Wisdom","<color:#00c9b2>", 0),
    MINING_WISDOM("🧠","Mining Wisdom","<color:#00c9b2>", 0),
    FORAGING_WISDOM("🧠","Foraging Wisdom","<color:#00c9b2>", 0),
    FISHING_WISDOM("🧠","Fishing Wisdom","<color:#00c9b2>", 0),
    ENCHANTING_WISDOM("🧠","Enchanting Wisdom","<color:#00c9b2>", 0),

    COMBAT_FORTUNE("🍀","Combat Fortune","<color:#ff9500>", 0),
    MINING_FORTUNE("🍀","Combat Fortune","<color:#ff9500>", 0),
    FORAGING_FORTUNE("🍀","Combat Fortune","<color:#ff9500>", 0),
    FISHING_FORTUNE("🍀","Combat Fortune","<color:#ff9500>", 0),
    ENCHANTING_FORTUNE("🍀","Combat Fortune","<color:#ff9500>", 0);

    @Getter private final String symbol;
    @Getter private final String name;
    @Getter private final String color;
    @Getter private final NamespacedKey key;
    @Getter private final float baseValue;
    @Getter private final NamespacedKey baseKey;
    Stats(String symbol, String name, String color, float base_value){
        this.symbol = symbol;
        this.name = name;
        this.color = color;
        this.baseValue = base_value;
        this.key = new NamespacedKey(JasperProject.getPlugin(), Util.escapeRegex(name).replaceAll(" ", ""));
        this.baseKey = new NamespacedKey(JasperProject.getPlugin(), key.getKey()+"BASE");
    }

    public static void putIfAbsent(Player player){
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        boolean hasStats = pdc.has(JKey.Stats);
        if(true || !hasStats){
            PersistentDataContainer stats = pdc.getAdapterContext().newPersistentDataContainer();
            for (Stats stat : Stats.values()) {
                if(stat.getBaseValue() == -1) continue;
                stats.set(stat.getKey(), PersistentDataType.FLOAT, 0f);
                stats.set(stat.getBaseKey(), PersistentDataType.FLOAT, stat.getBaseValue());
            }
            pdc.set(JKey.Stats, PersistentDataType.TAG_CONTAINER, stats);
        }

    }

    public static PersistentDataContainer toPDC(PersistentDataAdapterContext context, Map<Stats, Float> stats){
        PersistentDataContainer pdc = context.newPersistentDataContainer();
        for (Stats stat : stats.keySet()) {
            pdc.set(stat.key, PersistentDataType.FLOAT , stats.get(stat));
        }
        return pdc;
    }

    public static void apply(Player player, ItemStack item, JPlayer.Type type){
       apply(player, fromItem(item), type);
       JPlayer jPlayer = PlayerManager.getJPlayer(player);
       jPlayer.setLastItems(type, item);
    }
    public static void apply(Player player, Map<Stats, Float> stats, JPlayer.Type type){
        putIfAbsent(player);
        PersistentDataContainer pdc = player.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER);

        ///     CLEAR LAST STATS EFFECT
        JPlayer jPlayer = PlayerManager.getJPlayer(player);
        ItemStack lastItems = jPlayer.getLastItems(type);
        Map<Stats, Float> lastItemStats = fromItem(lastItems);
        if(lastItems!=null){
            for (Stats stat : lastItemStats.keySet()) {
                pdc.set(stat.getKey(),
                        PersistentDataType.FLOAT,
                        pdc.get(stat.getKey(), PersistentDataType.FLOAT) - lastItemStats.get(stat));
            }
        }
        ///     REPLACE TO NEW STATS
        for (Stats stat : stats.keySet()) {
            pdc.set(stat.getKey(),
                    PersistentDataType.FLOAT,
                    pdc.get(stat.getKey(), PersistentDataType.FLOAT) + stats.get(stat));
        }
        player.setWalkSpeed(Math.min(Math.max(pdc.get(Stats.POISON.getKey(), PersistentDataType.FLOAT)/500, 0), 2f));
    }

    public static Map<Stats, Float> fromItem(ItemStack item){
        Map<Stats, Float> stats = new HashMap<>();
        if(!item.hasItemMeta()) return stats;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if(!pdc.has(JKey.Stats)) return stats;
        pdc = pdc.get(JKey.Stats, PersistentDataType.TAG_CONTAINER);
        for (Stats stat : Stats.values()) {
            NamespacedKey key = stat.getKey();
            if(pdc.has(key)){
                stats.put(stat, pdc.get(key, PersistentDataType.FLOAT));
            }
        }
        return stats;
    }

    public static Map<Stats, Float> fromPlayer(Player player){
        Map<Stats, Float> stats = new HashMap<>();
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if(!pdc.has(JKey.Stats)) putIfAbsent(player);
        pdc = pdc.get(JKey.Stats, PersistentDataType.TAG_CONTAINER);
        for (Stats value : Stats.values()) {
            if(value.getBaseValue() == -1) continue;
            float stat = pdc.get(value.getKey(), PersistentDataType.FLOAT);
            float base_stat = pdc.get(value.getBaseKey(), PersistentDataType.FLOAT);
            stats.put(value, stat+base_stat);
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
