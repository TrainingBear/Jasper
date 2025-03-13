package me.jasper.jasperproject.JasperItem;

import jline.internal.Preconditions;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStats {
    int damage;
    int crit_damage;
    int crit_chance;
    int mana;
    int speed;
    int attack_speed;
    int double_attack;
    int swing_range;
    int true_defense;

    public ItemStats(){
    }

    public ItemStats(int damage, int crit_damage, int crit_chance, int mana, int speed, int attack_speed, int double_attack, int swing_range, int true_defense) {
        this.damage = damage;
        this.crit_damage = crit_damage;
        this.crit_chance = crit_chance;
        this.mana = mana;
        this.speed = speed;
        this.attack_speed = attack_speed;
        this.double_attack = double_attack;
        this.swing_range = swing_range;
        this.true_defense = true_defense;
    }

    public ItemStats setDamage(int Damage) {
        this.crit_damage = Damage;
        return this;

    }

    public ItemStats setCrit(int crit_damage) {
        this.crit_damage = crit_damage;
        return this;

    }

    public ItemStats setCritChance(int crit_chance) {
        this.crit_chance = crit_chance;
        return this;

    }

    public ItemStats setMana(int mana) {
        this.mana = mana;
        return this;
    }


    public ItemStats setSpeed(int speed) {
        this.speed = speed;
        return this;
    }


    public ItemStats setAttackSpeed(int attack_speed) {
        this.attack_speed = attack_speed;
        return this;

    }

    public ItemStats setDoubleAttack(int double_attack) {
        this.double_attack = double_attack;
        return this;

    }

    public ItemStats setSwingRange(int swing_range) {
        this.swing_range = swing_range;
        return this;

    }

    public ItemStats setTrueDefense(int true_defense) {
        this.true_defense = true_defense;
        return this;

    }

    public HashMap<String, Integer> getHashMapStats() throws IllegalAccessException {
        HashMap<String, Integer> stats = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.getInt(this) <= 0) continue;
            stats.put(field.getName(), field.getInt(this));
        }
        return stats;
    }

    public PersistentDataContainer getStatsContainer(PersistentDataContainer data) throws IllegalAccessException {
        PersistentDataContainer stats = data.getAdapterContext().newPersistentDataContainer();
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.getInt(this) <= 0) continue;
            stats.set(new NamespacedKey(JasperProject.getPlugin(), field.getName()), PersistentDataType.INTEGER, field.getInt(this));
        }

        return stats;
    }

    public List<String> getLore() throws IllegalAccessException {
        List<String> lore = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.getInt(this) <= 0) continue;

            StringBuilder name_builder = new StringBuilder();
            for(String word : field.getName().replace('_', ' ').split(" ")){
                name_builder.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
            }
            lore.add(ChatColor.GRAY+name_builder.toString() + ": "+ChatColor.RED + field.getInt(this));
        }
        return lore;
    }

}
