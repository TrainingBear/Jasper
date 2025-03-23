
import jline.internal.Preconditions;
import java.lang.reflect.Field;


class ItemStats_test {
    int damage;
    int crit_damage;
    int crit_chance;
    int mana;
    int speed;
    int attack_speed;
    int double_attack;
    int swing_range;
    int true_defense;

    public ItemStats_test(int damage, int crit_damage, int crit_chance, int mana, int speed, int attack_speed, int double_attack, int swing_range, int true_defense) {
        Preconditions.checkNotNull(damage);
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

    public ItemStats_test setDamage(int damage) {
        this.damage = damage;
        return this;
    }

    public ItemStats_test setCrit_damage(int crit_damage) {
        this.crit_damage = crit_damage;
        return this;

    }

    public ItemStats_test setCrit_chance(int crit_chance) {
        this.crit_chance = crit_chance;
        return this;

    }

    public ItemStats_test setMana(int mana) {
        this.mana = mana;
        return this;
    }


    public ItemStats_test setSpeed(int speed) {
        this.speed = speed;
        return this;
    }


    public ItemStats_test setAttack_speed(int attack_speed) {
        this.attack_speed = attack_speed;
        return this;

    }

    public ItemStats_test setDouble_attack(int double_attack) {
        this.double_attack = double_attack;
        return this;

    }

    public ItemStats_test setSwing_range(int swing_range) {
        this.swing_range = swing_range;
        return this;

    }

    public ItemStats_test setTrue_defense(int true_defense) {
        this.true_defense = true_defense;
        return this;

    }

    public ItemStats_test(){

    }

    public void getStats() throws IllegalAccessException {
//        HashMap<String, Integer> stats = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.getInt(this) <= 0) continue;
            System.out.print(field.getName()+" : "+field.getInt(this));
        }
//        return new HashMap<String, Integer>(Map.of(
//                "damage", damage,
//                "crit_damage", crit_damage,
//                "crit_chance",crit_chance,
//                "mana",mana,
//                "speed",speed,
//                "attack_speed",attack_speed,
//                "double_attack",double_attack,
//                "swing_range",swing_range,
//                "true_defense",true_defense
//        ));
    }

}

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        ItemStats_test test = new ItemStats_test().setDamage(1).setMana(999);
        test.getStats();
    }
}

