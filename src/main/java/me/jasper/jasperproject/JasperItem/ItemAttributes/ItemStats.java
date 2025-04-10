package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Setter;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.*;

public class ItemStats {
    private int damage;
    private int crit_damage;
    private int crit_chance;
    private int mana;
    private int speed;
    private int attack_speed;
    private int double_attack;
    private int swing_range;
    private int true_defense;

    public enum MODIFIER {
        damage,
        Damage(true),

        crit_damage,
        Crit_damage(true),

        crit_chance,
        Crit_chance(true),

        mana,
        Mana(true),

        speed,
        Speed(true),

        attack_speed,
        Attack_speed(true),

        double_attack,
        Double_attack(true),

        swing_range,
        Swing_range(true),

        true_defense,
        True_defense(true);

        boolean modifier = false;
        MODIFIER (){}
        MODIFIER (boolean b){
            modifier = b;
        }
    }

    private HashMap<MODIFIER, Float> modifiers = new HashMap<>();
    public void addModifiers(MODIFIER m, float v){
        modifiers.compute(m, (a, b) -> (b==null ? v : b+v));
    }

    public void removeModifiers(MODIFIER m){
        modifiers.put(m, 0f);
    }

    /**
     * @param v MUST BE GREATER THAN 0! DO NOT INSERT TRIGGER 0 OR BELLOW!
     * */
    public void removeModifiers(MODIFIER m, float v){
        modifiers.put(m,0f);
    }

    /**
     * @param v float Field name
     * @param multiplier is multiplier?
     * */
    private float getModifiersFromString(String v, boolean multiplier){

        return !multiplier ?
                switch (v){
                    case "DAMAGE" -> modifiers.get(MODIFIER.damage);
                    case "CRIT_DAMAGE" -> modifiers.get(MODIFIER.crit_damage);
                    case "CRIT_CHANCE" -> modifiers.get(MODIFIER.crit_chance);
                    case "MANA" -> modifiers.get(MODIFIER.mana);
                    case "SPEED" -> modifiers.get(MODIFIER.speed);
                    case "ATTACK_SPEED" -> modifiers.get(MODIFIER.attack_speed);
                    case "DOUBLE_ATTACK" -> modifiers.get(MODIFIER.double_attack);
                    case "SWING_RANGE" -> modifiers.get(MODIFIER.swing_range);
                    case "TRUE_DEFENCE" -> modifiers.get(MODIFIER.true_defense);
                    default -> throw new IllegalStateException("Unexpected value: " + v);
                } :
                switch (v){
                    case "DAMAGE" -> modifiers.get(MODIFIER.Damage);
                    case "CRIT_DAMAGE" -> modifiers.get(MODIFIER.Crit_damage);
                    case "CRIT_CHANCE" -> modifiers.get(MODIFIER.Crit_chance);
                    case "MANA" -> modifiers.get(MODIFIER.Mana);
                    case "SPEED" -> modifiers.get(MODIFIER.Speed);
                    case "ATTACK_SPEED" -> modifiers.get(MODIFIER.Attack_speed);
                    case "DOUBLE_ATTACK" -> modifiers.get(MODIFIER.Double_attack);
                    case "SWING_RANGE" -> modifiers.get(MODIFIER.Swing_range);
                    case "TRUE_DEFENCE" -> modifiers.get(MODIFIER.True_defense);
                    default -> throw new IllegalStateException("Unexpected value: " + v);
        };

    }

    /**
     * Stats color Getter
     * @param v Float field name
     * */
    private String getStatsFormat(String v){
//        ChatColor.translateAlternateColorCodes('a',"ab");
        return switch (v){//sekalia lu pake ChatColor translate
            case "DAMAGE" -> ChatColor.translateAlternateColorCodes('&',"&7Damage: &c+%.0f");
            case "CRIT_DAMAGE" -> ChatColor.translateAlternateColorCodes('&',"&7Crit Damage: &x&6&2&4&5&F&F+%.0f");
            case "CRIT_CHANCE" -> ChatColor.translateAlternateColorCodes('&',"&7Crit Chance: &x&8&B&7&6&F&F+%.0f");
            case "MANA" -> ChatColor.translateAlternateColorCodes('&',"&7Mana: &x&3&9&F&F&F&TRIGGER+%.0f");
            case "SPEED" -> ChatColor.translateAlternateColorCodes('&',"&7Speed: &x&F&4&F&F&D&0+%.0f");
            case "ATTACK_SPEED" -> ChatColor.translateAlternateColorCodes('&',"&7Attack Speed: &e+%.0f");
            case "DOUBLE_ATTACK" -> ChatColor.translateAlternateColorCodes('&',"&7Double Attack: &x&F&TRIGGER&B&9&4&C+%.0f");
            case "SWING_RANGE" -> ChatColor.translateAlternateColorCodes('&',"&7Swing Range: &x&F&F&8&6&3&D+%.0f");
            case "TRUE_DEFENCE" -> ChatColor.translateAlternateColorCodes('&',"&7True Defence: &x&B&5&F&F&TRIGGER&7+%.0f");
            default -> ChatColor.translateAlternateColorCodes('&',"&7Unknown stats: &0+%.0f");
        };
    }
    /**
    * kalau case1 dan case2 true, bakal ke gabung jadi (+v1 | *v2)
     * @param stats Float field name
    * @param case1 cuma ada + modifier doang
     * @param case2 cuma ada * modifier doang
    * */
    private String getModifierFormat(String stats, boolean case1, boolean case2){
        stats += "test";
        if(case1 && case2){
            return switch (stats) {
                case "DAMAGE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&E&9&4&TRIGGER&4&TRIGGER+&x&D&1&2&9&2&9%.0f &8|&r &x&E&9&4&TRIGGER&4&Ax&x&D&1&2&9&2&9+%.0f%%&8)");
                case "CRIT_DAMAGE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&E&9&4&TRIGGER&4&TRIGGER+&x&D&1&2&9&2&9%.0f &8|&r &x&E&9&4&TRIGGER&4&Ax&x&D&1&2&9&2&9+%.0f%%&8)");
                case "CRIT_CHANCE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&E&9&4&TRIGGER&4&TRIGGER+&x&D&1&2&9&2&9%.0f &8|&r &x&E&9&4&TRIGGER&4&Ax&x&D&1&2&9&2&9+%.0f%%&8)");
                case "MANA" -> ChatColor.translateAlternateColorCodes('&',"&8&l(&r&x&4&9&C&6&D&5+&x&2&B&B&2&C&2%.0f &b&l|&r &x&4&9&C&6&D&5+&x&2&B&B&2&C&2%.0f%%&8&l)");
                case "SPEED" -> ChatColor.translateAlternateColorCodes('&',"&8&l(&r&x&C&B&D&5&TRIGGER&D+&x&B&7&C&0&9&D%.0f &b&l|&r &x&C&B&D&5&TRIGGER&D+&x&B&7&C&0&9&D%.0f%%&8&l)");
                case "ATTACK_SPEED" -> ChatColor.translateAlternateColorCodes('&',"&8&l(&r&x&C&E&C&D&2&6+&x&B&7&B&6&1&6%.0f &b&l|&r &x&C&E&C&D&2&6+&x&B&7&B&6&1&6%.0f%%&8&l)");
                case "DOUBLE_ATTACK" -> ChatColor.translateAlternateColorCodes('&',"&8&l(&r&x&D&8&TRIGGER&1&4&3+&x&C&C&9&4&3&6%.0f &b&l|&r &x&D&8&TRIGGER&1&4&3+&x&C&C&9&4&3&6%.0f%%&8&l)");
                case "SWING_RANGE" -> ChatColor.translateAlternateColorCodes('&',"&8&l(&r&x&D&TRIGGER&7&B&4&2+&x&D&0&6&C&3&0%.0f &b&l|&r &x&D&TRIGGER&7&B&4&2 &x&6&0&4&7&E&F+&x&D&0&6&C&3&0%.0f%%&8&l)");
                case "TRUE_DEFENCE" -> ChatColor.translateAlternateColorCodes('&',"&8&l(&r&x&6&0&4&7&E&F+&x&9&4&B&D&8&C%.0f &b&l|&r &x&6&0&4&7&E&F+&x&9&4&B&D&8&C%.0f%%&8&l)");
                default -> "&8(&r&a+%.0f &b&l|&r &9%.0f%%&8)";
            };
        }
        if(case1){
            return switch (stats) {
                case "DAMAGE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&E&9&4&TRIGGER&4&TRIGGER+&x&D&1&2&9&2&9%.0f&8)");
                case "CRIT_DAMAGE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&6&0&4&7&E&F+&x&4&5&2&B&D&7%.0f&8)");
                case "CRIT_CHANCE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&8&B&8&4&D&F+&x&6&E&6&6&C&E%.0f&8)");
                case "MANA" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&4&9&C&6&D&5+&x&2&B&B&2&C&2%.0ff&8)");
                case "SPEED" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&C&B&D&5&TRIGGER&D+&x&B&7&C&0&9&D%.0f&8)");
                case "ATTACK_SPEED" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&C&E&C&D&2&6+&x&B&7&B&6&1&6%.0f&8)");
                case "DOUBLE_ATTACK" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&D&8&TRIGGER&1&4&3+&x&C&C&9&4&3&6%.0f&8)");
                case "SWING_RANGE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&D&TRIGGER&7&B&4&2+&x&D&0&6&C&3&0%.0f&8)");
                case "TRUE_DEFENCE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&B&5&D&7&TRIGGER&F+&x&9&4&B&D&8&C%.0f&8)");
                default -> "&8(&r&a+%.0f&8)";
            };
        }
        if(case2){
            return switch (stats) {
                case "DAMAGE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&E&9&4&TRIGGER&4&Ax&x&D&1&2&9&2&9+%.0f%%&8)");
                case "CRIT_DAMAGE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&6&0&4&7&E&Fx&x&4&5&2&B&D&7+%.0f%%&8)");
                case "CRIT_CHANCE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&8&B&8&4&D&Fx&x&6&E&6&6&C&E+%.0f%%&8)");
                case "MANA" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&4&9&C&6&D&5x&x&2&B&B&2&C&2+%.0f%%&8)");
                case "SPEED" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&C&B&D&5&TRIGGER&Dx&x&B&7&C&0&9&D+%.0f%%&8)");
                case "ATTACK_SPEED" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&C&E&C&D&2&6x&x&B&7&B&6&1&6+%.0f%%&8)");
                case "DOUBLE_ATTACK" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&D&8&TRIGGER&1&4&3x&x&C&C&9&4&3&6+%.0f%%&8)");
                case "SWING_RANGE" -> ChatColor.translateAlternateColorCodes('&',"&8(&rx&D&TRIGGER&7&B&4&2 &x&6&0&4&7&E&Fx&x&D&0&6&C&3&0+%.0f%%&8)");
                case "TRUE_DEFENCE" -> ChatColor.translateAlternateColorCodes('&',"&8(&r&x&B&5&D&7&TRIGGER&Fx&x&9&4&B&D&8&C+%.0f%%&8)");
                default -> "&8(&9+%.0f%%&8)";
            };
        }
        return "";
    }

    /**
     * @param stats Float field name
    * @param v1 + modifier
     * @param v2 * modifier
    * */
    private String getModifierFormat(String stats, float v1, float v2){
        if(v1 <= 0 && v2 <=0){
            return "";
        }
        if(v1 >= 0 && v2 <= 0){
            return ChatColor.translateAlternateColorCodes('&',
                    String.format(getModifierFormat(stats, true, false),v1));
        }
        if(v2 >= 0 && v1 <= 0){
            return ChatColor.translateAlternateColorCodes('&',
                    String.format(getModifierFormat(stats, false, true),v2));
        }
        return ChatColor.translateAlternateColorCodes('&',
                String.format(getModifierFormat(stats, true, true),v1,v2));
    }

    private float DAMAGE;
    private float CRIT_DAMAGE;
    private float CRIT_CHANCE;
    private float DOUBLE_ATTACK;
    private float ATTACK_SPEED;
    private float SWING_RANGE;
    private float MANA;
    private float SPEED;
    private float TRUE_DEFENCE;

    public void calculateFinalStats(){
        this.DAMAGE = 0;
        this.DAMAGE += (damage + modifiers.get(MODIFIER.damage));
        this.DAMAGE += (damage * (modifiers.get(MODIFIER.Damage))/100);

        this.CRIT_DAMAGE = 0;
        this.CRIT_DAMAGE += crit_damage + modifiers.get((MODIFIER.crit_damage));
        this.CRIT_DAMAGE += crit_damage * (modifiers.get((MODIFIER.Crit_damage))/100);

        this.CRIT_CHANCE = 0;
        this.CRIT_CHANCE += crit_chance + modifiers.get((MODIFIER.crit_chance));
        this.CRIT_CHANCE += crit_chance * (modifiers.get((MODIFIER.Crit_chance))/100);

        this.MANA = 0;
        this.MANA += mana + modifiers.get((MODIFIER.mana));
        this.MANA += mana * (modifiers.get((MODIFIER.Mana))/100);

        this.SPEED = 0;
        this.SPEED += speed + modifiers.get((MODIFIER.speed));
        this.SPEED += speed * (modifiers.get((MODIFIER.Speed))/100);

        this.ATTACK_SPEED = 0;
        this.ATTACK_SPEED += attack_speed + modifiers.get((MODIFIER.attack_speed));
        this.ATTACK_SPEED += attack_speed * (modifiers.get((MODIFIER.Attack_speed))/100);

        this.DOUBLE_ATTACK = 0;
        this.DOUBLE_ATTACK += double_attack + modifiers.get((MODIFIER.double_attack));
        this.DOUBLE_ATTACK += double_attack * (modifiers.get((MODIFIER.Double_attack))/100);

        this.SWING_RANGE = 0;
        this.SWING_RANGE += ((float) swing_range /10) + modifiers.get((MODIFIER.swing_range));
        this.SWING_RANGE += ((float) swing_range /10) * (modifiers.get((MODIFIER.Swing_range))/100);

        this.TRUE_DEFENCE = 0;
        this.TRUE_DEFENCE += true_defense + modifiers.get((MODIFIER.true_defense));
        this.TRUE_DEFENCE += true_defense * (modifiers.get((MODIFIER.True_defense))/100);
    }

    public ItemStats(){
        for (MODIFIER value : MODIFIER.values()) {
            modifiers.putIfAbsent(value,0f);
        }
    }

    //Setter method

        public ItemStats setBaseDamage ( int Damage){
        this.damage = Damage;
        return this;

    }
        public ItemStats setBaseCrit ( int crit_damage){
        this.crit_damage = crit_damage;
        return this;

    }
        public ItemStats setBaseCritChance ( int crit_chance){
        this.crit_chance = crit_chance;
        return this;

    }
        public ItemStats setBaseMana ( int mana){
        this.mana = mana;
        return this;
    }
        public ItemStats setBaseSpeed ( int speed){
        this.speed = speed;
        return this;
    }
        public ItemStats setBaseAttackSpeed ( int attack_speed){
        this.attack_speed = attack_speed;
        return this;

    }
        public ItemStats setBaseDoubleAttack ( int double_attack){
        this.double_attack = double_attack;
        return this;

    }
        public ItemStats setBaseSwingRange ( int swing_range){
        this.swing_range = swing_range;
        return this;

    }
        public ItemStats setBaseTrueDefense ( int true_defense){
        this.true_defense = true_defense;
        return this;

    }

    public PersistentDataContainer getBaseStats(PersistentDataContainer data) throws IllegalAccessException {
        PersistentDataContainer stats = data.getAdapterContext().newPersistentDataContainer();
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.getType() != int.class) continue;
            stats.set(new NamespacedKey(JasperProject.getPlugin(), field.getName()), PersistentDataType.INTEGER, field.getInt(this));
        }

        return stats;
    }

    public PersistentDataContainer getModifierStats(PersistentDataContainer data) {
        PersistentDataContainer stats = data.getAdapterContext().newPersistentDataContainer();
        for(MODIFIER m : MODIFIER.values()){
            if(modifiers.containsKey(m) && modifiers.get(m) > 0){
                stats.set(new NamespacedKey(JasperProject.getPlugin(), m.name()), PersistentDataType.FLOAT, modifiers.get(m));
                continue;
            }
            stats.set(new NamespacedKey(JasperProject.getPlugin(), m.name()), PersistentDataType.FLOAT, 0f);
        }
        return stats;
    }
    public PersistentDataContainer getAndApplyFinalStats(PersistentDataContainer data) throws IllegalAccessException {
        calculateFinalStats();

        PersistentDataContainer finalStats = data.getAdapterContext().newPersistentDataContainer();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getType() != float.class) continue;
            finalStats.set(new NamespacedKey(JasperProject.getPlugin(),field.getName()),
                    PersistentDataType.FLOAT, field.getFloat(this));
        }
        return finalStats;
    }

    public List<String> getLore() throws IllegalAccessException {
        List<String> lore = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.getType() != float.class) continue;
            if(field.getFloat(this) <= 0) continue;

            StringBuilder name_builder = new StringBuilder();
            for(String word : field.getName().toLowerCase().replace('_', ' ').split(" ")){
                name_builder.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1)).append(" ");
            }
            float modifier = getModifiersFromString(field.getName(), false);
            float xmodifier = getModifiersFromString(field.getName(), true);
            lore.add(
                    String.format(getStatsFormat(field.getName()), field.getFloat(this))
                    +" "
                    +getModifierFormat(field.getName(),modifier,xmodifier)
            );
        }
        return lore;
    }

    public ItemStats getStatsFromItem(ItemStack items) throws IllegalAccessException {
        ItemMeta item = items.getItemMeta();
        Field[] fields = this.getClass().getDeclaredFields();

        //stats getter
        PersistentDataContainer Stats = ItemUtils.getStats(item);
        for (Field field : fields) {
            if(field.getType() != float.class) continue;
            float value = Stats.get(new NamespacedKey(JasperProject.getPlugin(), field.getName()), PersistentDataType.FLOAT);
            field.setAccessible(true);
            field.setFloat(this, value);
        }

        //base stats getter
        PersistentDataContainer BaseStats = ItemUtils.getBaseStats(item);
        for (Field field : fields) {
            if(field.getType() != int.class) continue;
            int value = BaseStats.get(new NamespacedKey(JasperProject.getPlugin(), field.getName()), PersistentDataType.INTEGER);
            field.setAccessible(true);
            field.setInt(this, value);
        }

        //modifier stats getter
        PersistentDataContainer ModifierStats = ItemUtils.getModifier(item);
        for(MODIFIER m : MODIFIER.values()){
            float v = ModifierStats.get(new NamespacedKey(JasperProject.getPlugin(), m.name()), PersistentDataType.FLOAT);
            this.modifiers.put(m,v);
        }
        return this;
    }

    public void setBaseStats(ItemStats stats){
        this.damage = stats.damage;
        this.crit_damage = stats.crit_damage;
        this.crit_chance = stats.crit_chance;
        this.mana = stats.mana;
        this.speed = stats.speed;
        this.attack_speed = stats.attack_speed;
        this.double_attack = stats.double_attack;
        this.swing_range = stats.swing_range;
        this.true_defense = stats.true_defense;
    }

    public void setModifiers(ItemStats stats){
        this.modifiers = stats.modifiers;
    }

}
