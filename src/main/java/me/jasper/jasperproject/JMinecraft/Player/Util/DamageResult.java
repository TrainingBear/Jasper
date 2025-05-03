package me.jasper.jasperproject.JMinecraft.Player.Util;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Map;
import java.util.Random;

@Getter
public abstract class DamageResult {

    private Map<Stats ,Float> stats;
    private DamageType type;
    @Setter private int defence = 1;
    @Setter private boolean trueDamage;
    @Setter private int true_defence;
    private int final_damage;
    private float force = .1f;
    private Component display;
    private boolean critical;

    DamageResult(Map<Stats, Float> stat){
        this.stats = stat;
    }

    DamageResult(){

    }

    public static Builder builder(Map<Stats, Float> a){
        return new Builder(a);
    }

    public static DamageResult patch(float final_damage, float defence, float true_defence, DamageType type){
        DamageResult result = builder(null).build();
        final_damage-=  true_defence;
        final_damage =  ( final_damage / ((defence/100f)+1));
        result.final_damage = (int) final_damage;
        result.display = Util.deserialize(type.getSymbol()+" "+final_damage).color(type.getColor());
        return result;
    }

    public void recalculate(){
        float final_damage;
        if(stats==null){
            this.final_damage-=true_defence;
            this.final_damage = (int) ((float) this.final_damage / ((float) defence/100f)+1);
            return;
        }else {
            final_damage = stats.get(Stats.DAMAGE);
        }
        label: {
            if(type==null) break label;
            Random random = new Random();
            if(type.equals(DamageType.MAGIC)) {
                critical = critical && (stats.get(Stats.CRIT_CHANCE) >= random.nextInt(100)+1);
                final_damage += final_damage *(stats.get(Stats.MANA)/100f);
                final_damage = final_damage * stats.get(Stats.MAGIC_MODIFIER);
                if(critical) final_damage += final_damage * (stats.get(Stats.CRIT_DAMAGE)/400);
            }
            if(type.equals(DamageType.MELEE)){
                final_damage = final_damage * stats.get(Stats.MELEE_MODIFIER);
                final_damage += final_damage * stats.get(Stats.STRENGTH)/100;
                if(critical) final_damage += final_damage * (stats.get(Stats.CRIT_DAMAGE)/50);
            }
            if(type.equals(DamageType.PROJECTILE)){
                critical = critical && (stats.get(Stats.CRIT_CHANCE) >= random.nextInt(100)+1);
                final_damage = final_damage * stats.get(Stats.ARROW_MODIFIER);
                final_damage += final_damage * stats.get(Stats.STRENGTH)/100;
                if(critical) final_damage += final_damage * (stats.get(Stats.CRIT_DAMAGE)/150);
                final_damage*=force;
            }
            final_damage-=true_defence;
            final_damage = (final_damage / (((float) defence/100f)+1));
            this.final_damage = (int) final_damage;
        }
        this.display = Util.deserialize(type !=null ? type.getSymbol() : "??"+" "+this.final_damage).color(type!=null ? type.getColor() : NamedTextColor.DARK_PURPLE);
    }

    public static class Builder{
        private final Map<Stats, Float> stats;
        private int defence;
        private int true_defence = 0;
        private boolean trueDamage;
        private DamageType type;
        private float force;
        private boolean critical;
        private int final_damage = 0;

        Builder(Map<Stats, Float> stat){
            this.stats = stat;
        }
        public Builder final_damage(int damage){
            final_damage = damage;
            return this;
        }
        public Builder critical(boolean b){
            critical = b;
            return  this;
        }
        public Builder force(float f){
            this.force =f;
            return this;
        }
        public Builder type(DamageType type){
            this.type = type;
            return this;
        }
        public Builder defence(int defence){
            this.defence = defence;
            return this;
        }
        public Builder true_defence(int t){
            this.true_defence = t;
            return this;
        }
        public Builder trueDamage(boolean b){
            trueDamage = b;
            return this;
        }
        public DamageResult build(){
            DamageResult result = new DamageResult(stats) {};
            result.setTrue_defence(true_defence);
            result.setDefence(defence);
            result.type = this.type;
            result.setTrueDamage(trueDamage);
            result.force = this.force;
            result.critical = this.critical;
            if(this.final_damage<=0) result.final_damage = this.final_damage;

            result.recalculate();
            return result;
        }
    }
}
