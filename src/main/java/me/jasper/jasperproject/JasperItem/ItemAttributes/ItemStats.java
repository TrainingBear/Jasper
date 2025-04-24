package me.jasper.jasperproject.JasperItem.ItemAttributes;

import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.*;

public class ItemStats {
    private int health;
    private int defence;
    private int damage;
    private int strength;
    private int crit_damage;
    private int crit_chance;
    private int mana;
    private int speed;
    private int true_defence;
    private int attack_speed;
    private int double_attack;
    private int swing_range;


    private HashMap<StatsEnum, Float> modifiers = new HashMap<>();
    public void addModifiers(StatsEnum m, float v){
        modifiers.compute(m, (a, b) -> (b==null ? v : b+v));
    }

    public void removeModifiers(StatsEnum m){
        modifiers.put(m, 0f);
    }

    /**
     * @param v MUST BE GREATER THAN 0! DO NOT INSERT TRIGGER 0 OR BELLOW!
     * */
    public void removeModifiers(StatsEnum m, float v){
        modifiers.put(m,0f);
    }

    /**
     * @param v float Field name
     * @param multiplier is multiplier?
     * */
    private float getModifiersFromString(String v, boolean multiplier){
        return !multiplier ? modifiers.get(StatsEnum.valueOf(v)) : modifiers.get(StatsEnum.valueOf(v.substring(0,1).toUpperCase() + v.substring(1).toLowerCase()));
    }

    /**
     * Stats color Getter
     * @param v Float field name
     * */
    private String getStatsFormat(String v,float placeholder){
//        ChatColor.translateAlternateColorCodes('a',"ab");
        StatsEnum stat = StatsEnum.valueOf(v);
        return "<!i><gray>"+stat.rawSym()+" "+stat.getName()+": "+stat.rawColor()+"+"+placeholder;
    }
    /**
    * kalau case1 dan case2 true, bakal ke gabung jadi (+v1 | *v2)
     * @param stats Float field name
    * @param case1 cuma ada + modifier doang
     * @param case2 cuma ada * modifier doang
    * */
    private String getModifierFormat(String stats, boolean case1, boolean case2){
        StatsEnum statEnum = StatsEnum.valueOf(stats);
        if(case1 && case2) return "<gray>("+statEnum.rawColor()+"%.0f <gray>| "+statEnum.rawColor()+"%.0f%%<gray>)";
        if(case1) return "<gray>("+statEnum.rawColor()+"%.0f<gray>)";
        if(case2) return "<gray>("+statEnum.rawColor()+"%.0f%%<gray>)";
        return "";
    }

    /**
     * @param stats Float field name
    * @param v1 + modifier
     * @param v2 * modifier
    * */
    private String getModifierFormat(String stats, float v1, float v2){
        if(v1 <= 0 && v2 <=0) return "";

        if(v1 >= 0 && v2 <= 0) return String.format(getModifierFormat(stats, true, false),v1);

        if(v2 >= 0 && v1 <= 0) return String.format(getModifierFormat(stats, false, true),v2);

        return String.format(getModifierFormat(stats, true, true),v1,v2);
    }

    private float DAMAGE;
    private float STRENGTH;
    private float CRIT_DAMAGE;
    private float CRIT_CHANCE;
    private float DOUBLE_ATTACK;
    private float ATTACK_SPEED;
    private float SWING_RANGE;
    private float MANA;
    private float SPEED;
    private float TRUE_DEFENCE;
    private float HEALTH;
    private float DEFENCE;

    public void calculateFinalStats(){
        this.DAMAGE = 0;
        this.DAMAGE += (damage + modifiers.get(StatsEnum.DAMAGE));
        this.DAMAGE += (damage * (modifiers.get(StatsEnum.Damage))/100);

        this.STRENGTH = 0;
        this.STRENGTH += (strength + modifiers.get(StatsEnum.STRENGTH));
        this.STRENGTH += (strength * (modifiers.get(StatsEnum.Strength))/100);

        this.CRIT_DAMAGE = 0;
        this.CRIT_DAMAGE += crit_damage + modifiers.get((StatsEnum.CRIT_DAMAGE));
        this.CRIT_DAMAGE += crit_damage * (modifiers.get((StatsEnum.Crit_damage))/100);

        this.CRIT_CHANCE = 0;
        this.CRIT_CHANCE += crit_chance + modifiers.get((StatsEnum.CRIT_CHANCE));
        this.CRIT_CHANCE += crit_chance * (modifiers.get((StatsEnum.Crit_chance))/100);

        this.MANA = 0;
        this.MANA += mana + modifiers.get((StatsEnum.MANA));
        this.MANA += mana * (modifiers.get((StatsEnum.Mana))/100);

        this.SPEED = 0;
        this.SPEED += speed + modifiers.get((StatsEnum.SPEED));
        this.SPEED += speed * (modifiers.get((StatsEnum.Speed))/100);

        this.ATTACK_SPEED = 0;
        this.ATTACK_SPEED += attack_speed + modifiers.get((StatsEnum.ATTACK_SPEED));
        this.ATTACK_SPEED += attack_speed * (modifiers.get((StatsEnum.Attack_speed))/100);

        this.DOUBLE_ATTACK = 0;
        this.DOUBLE_ATTACK += double_attack + modifiers.get((StatsEnum.DOUBLE_ATTACK));
        this.DOUBLE_ATTACK += double_attack * (modifiers.get((StatsEnum.Double_attack))/100);

        this.SWING_RANGE = 0;
        this.SWING_RANGE += ((float) swing_range /10) + modifiers.get((StatsEnum.SWING_RANGE));
        this.SWING_RANGE += ((float) swing_range /10) * (modifiers.get((StatsEnum.Swing_range))/100);

        this.TRUE_DEFENCE = 0;
        this.TRUE_DEFENCE += true_defence + modifiers.get((StatsEnum.TRUE_DEFENCE));
        this.TRUE_DEFENCE += true_defence * (modifiers.get((StatsEnum.True_defence))/100);

        this.HEALTH =0;
        this.HEALTH += health + modifiers.get((StatsEnum.HEALTH));
        this.HEALTH += health * (modifiers.get((StatsEnum.Health))/100);

        this.DEFENCE =0;
        this.DEFENCE += defence + modifiers.get((StatsEnum.DEFENCE));
        this.DEFENCE += defence * (modifiers.get((StatsEnum.Defence))/100);
    }

    public ItemStats(){
        for (StatsEnum value : StatsEnum.values()) {
            modifiers.putIfAbsent(value,0f);
        }
    }

    //Setter method

        public ItemStats setBaseDamage ( int Damage){
        this.damage = Damage;
        return this;

    }
        public ItemStats setBaseStrength ( int strength){
        this.strength = strength;
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
        this.true_defence = true_defense;
        return this;
    }
        public ItemStats setBaseHealth( int health){
        this.health = health;
        return this;
    }
        public ItemStats setBaseDefense(int defense){
        this.defence = defense;
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
        for(StatsEnum m : StatsEnum.values()){
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

    public List<Component> getLore() throws IllegalAccessException {
        List<Component> lore = new ArrayList<>();
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
            lore.add(MiniMessage.miniMessage().deserialize("<!i>"+
                    getStatsFormat(field.getName(), field.getFloat(this))
                    +" "
                    +getModifierFormat(field.getName(),modifier,xmodifier)
            ));
        }
        return lore;
    }

    public ItemStats getStatsFromItem(ItemStack items) throws IllegalAccessException {
        ItemMeta item = items.getItemMeta();
        Field[] fields = this.getClass().getDeclaredFields();

        //stats getter
        PersistentDataContainer stats = ItemUtils.getStats(item);
        for (Field field : fields) {
            if(field.getType() != float.class) continue;
            float value = stats.get(new NamespacedKey(JasperProject.getPlugin(), field.getName()), PersistentDataType.FLOAT);
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
        for(StatsEnum m : StatsEnum.values()){
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
        this.true_defence = stats.true_defence;
        this.defence = stats.defence;
        this.health = stats.health;
    }

    public void setModifiers(ItemStats stats){
        this.modifiers = stats.modifiers;
    }

}
