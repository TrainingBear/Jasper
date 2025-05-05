package me.jasper.jasperproject.JMinecraft.Player;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorType;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.*;

@Getter
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
    ENCHANTING_FORTUNE("🍀","Combat Fortune","<color:#ff9500>", 0),

    MELEE_MODIFIER("", "Melee Multiplier", NamedTextColor.RED, 1, false),
    MAGIC_MODIFIER("", "Ability Multiplier", NamedTextColor.LIGHT_PURPLE, 1, false),
    ARROW_MODIFIER("", "Projectile Multiplier", NamedTextColor.AQUA, 1, false);

    private final boolean visible;
    private final String symbol;
    private final String name;
    private final String color;
    private final NamespacedKey key;
    private final float baseValue;
    private TextColor textColor;

    Stats(String symbol, String name, TextColor color, float base_value, boolean visible){
        this(symbol, name, "<"+color.asHexString()+">", base_value, visible);
        this.textColor = color;
    }
    Stats(String symbol, String name, String color, float baseValue){
        this(symbol, name, color, baseValue, true);
    }
    Stats(String symbol, String name, String color , float base_value, boolean visible){
        this.visible = visible;
        this.symbol = symbol;
        this.name = name;
        this.color = color;
        this.baseValue = base_value;
        this.key = new NamespacedKey(JasperProject.getPlugin(), Util.escapeRegex(name).replaceAll(" ", ""));
    }

    public static void putIfAbsent(Player player){
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        PersistentDataContainer stats = pdc.getAdapterContext().newPersistentDataContainer();
        if(!pdc.has(JKey.Stats)) pdc.set(JKey.Stats, PersistentDataType.TAG_CONTAINER, stats);
        stats = pdc.get(JKey.Stats, PersistentDataType.TAG_CONTAINER);
        for (Stats stat : Stats.values()) {
            if(stats.has(stat.getKey())) continue;
            stats.set(stat.getKey(), PersistentDataType.FLOAT, 0f);
        }
        pdc.set(JKey.Stats, PersistentDataType.TAG_CONTAINER, stats);
    }

    public static PersistentDataContainer toPDC(PersistentDataAdapterContext context, Map<Stats, Float> stats){
        PersistentDataContainer pdc = context.newPersistentDataContainer();
        for (Stats stat : stats.keySet()) {
            pdc.set(stat.key, PersistentDataType.FLOAT , stats.get(stat));
        }
        return pdc;
    }

    public static void apply(Player player, ItemStack item, ArmorType type){
       apply(player, fromItem(item), type);
       JPlayer jPlayer = PlayerManager.getJPlayer(player);
       jPlayer.setLastItems(type, item);
    }
    public static void apply(Player player, Map<Stats, Float> stats, ArmorType type){
        putIfAbsent(player);
        PersistentDataContainer pdc = player.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER);

        ///     CLEAR LAST STATS EFFECT
        JPlayer jPlayer = PlayerManager.getJPlayer(player);
        ItemStack lastItems = jPlayer.getLastItems(type);
        if(lastItems!=null){
            Map<Stats, Float> lastItemStats = fromItem(lastItems);
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
        if(item == null || !item.hasItemMeta()) return stats;
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
        return fromPlayer(player, null, null);
    }
    public static Map<Stats, Float> fromPlayer(Player player, ItemStack item){
        return fromPlayer(player, item, null);
    }
    public static Map<Stats, Float> fromPlayer(Player player, @Nullable ItemStack mainHand, @Nullable ItemStack offHand){
        putIfAbsent(player);
        Map<Stats, Float> stats = new HashMap<>();
        for (Stats stat : Stats.values()) {
            float p_stat = player.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER)
                    .get(stat.getKey(), PersistentDataType.FLOAT);
            float main = mainHand !=null && mainHand.hasItemMeta() && mainHand.getPersistentDataContainer().has(JKey.Stats) &&
                    mainHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER).has(stat.getKey()) ?
                    mainHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER)
                            .get(stat.getKey(), PersistentDataType.FLOAT) : 0;
            float off = offHand !=null &&offHand.hasItemMeta() && offHand.getPersistentDataContainer().has(JKey.Stats) &&
                    offHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER).has(stat.getKey()) ?
                    offHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER)
                            .get(stat.getKey(), PersistentDataType.FLOAT) : 0;
            stats.put(stat, p_stat+main+off+stat.getBaseValue());
        }
        return stats;
    }

    public static Map<Stats, Float> getCombatStats(Player player){
        return getCombatStats(player, null, null);
    }
    public static Map<Stats, Float> getCombatStats(Player player, @Nullable ItemStack weapon){
        return getCombatStats(player, weapon, null);
    }
    public static Map<Stats, Float> getCombatStats(@Nullable Player player, @Nullable ItemStack mainHand, @Nullable ItemStack offHand){
        if (player!=null) putIfAbsent(player);
        Set<Stats> combat = Set.of(
                Stats.DAMAGE, Stats.CRIT_DAMAGE, Stats.CRIT_CHANCE, Stats.SWING_RANGE,
                Stats.DEFENCE, Stats.TRUE_DEFENCE, Stats.STRENGTH, Stats.ARROW_MODIFIER,
                Stats.MELEE_MODIFIER, Stats.MAGIC_MODIFIER, Stats.MANA, Stats.DOUBLE_ATTACK
        );
        Map<Stats, Float> player_stats = new HashMap<>();
        for (Stats stat : combat) {
            float p_stat = player!=null ? player.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER)
                    .get(stat.getKey(), PersistentDataType.FLOAT) : 0f;
            float main = mainHand !=null && mainHand.hasItemMeta() && mainHand.getPersistentDataContainer().has(JKey.Stats) &&
                    mainHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER).has(stat.getKey()) ?
                    mainHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER)
                            .get(stat.getKey(), PersistentDataType.FLOAT) : 0;
            float off = offHand !=null && offHand.hasItemMeta() && offHand.getPersistentDataContainer().has(JKey.Stats) &&
                    offHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER).has(stat.getKey()) ?
                    offHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER)
                            .get(stat.getKey(), PersistentDataType.FLOAT) : 0;
            player_stats.put(stat, p_stat+main+off+stat.getBaseValue());
        }
        return player_stats;
    }

    public static float getStats(Player player, Stats stat){
        return getStats(player, null, null, stat);
    }
    public static float getStats(Player player, @Nullable ItemStack mainHand, Stats stat){
        return getStats(player, mainHand, null, stat);
    }
    public static float getStats(Player player, @Nullable ItemStack mainHand, @Nullable ItemStack offHand, Stats stat){
        float p_stat = player!=null ? player.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER)
                .get(stat.getKey(), PersistentDataType.FLOAT) : 0f;
        float main = mainHand !=null && mainHand.hasItemMeta() && mainHand.getPersistentDataContainer().has(JKey.Stats) &&
                mainHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER).has(stat.getKey()) ?
                mainHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER)
                        .get(stat.getKey(), PersistentDataType.FLOAT) : 0;
        float off = offHand !=null && offHand.hasItemMeta() && offHand.getPersistentDataContainer().has(JKey.Stats) &&
                offHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER).has(stat.getKey()) ?
                offHand.getPersistentDataContainer().get(JKey.Stats, PersistentDataType.TAG_CONTAINER)
                        .get(stat.getKey(), PersistentDataType.FLOAT) : 0;
        return p_stat+main+off+stat.getBaseValue();
    }

    public static boolean roll(float critical_chance){
        Random random = new Random();
        return random.nextInt((int) critical_chance) <= critical_chance;
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
