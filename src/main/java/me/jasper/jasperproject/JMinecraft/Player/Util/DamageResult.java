package me.jasper.jasperproject.JMinecraft.Player.Util;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

@Getter
public abstract class DamageResult {

    private Map<Stats ,Float> stats;
    private DamageType type;
    @Setter private int defence = 1;
    @Setter private boolean trueDamage;
    @Setter private int true_defence;
    @Setter private float final_damage;
    private float force = 0f;
    private Component display;
    private boolean critical;

    DamageResult(Map<Stats, Float> stat){
        this.stats = stat;
    }

    DamageResult(){

    }

    public static Builder builder(@NotNull Map<Stats, Float> a){
        return new Builder(a);
    }

    public static DamageResult patch(float final_damage, @Nullable LivingEntity entity, DamageType type, float multiplier){
        return patch(final_damage, entity, type, false, multiplier);
    }
    public static DamageResult patch(float final_damage, @Nullable LivingEntity entity, DamageType type){
        return patch(final_damage, entity, type, false, 1);
    }
    public static DamageResult patch(float final_damage, @Nullable LivingEntity entity, DamageType type, boolean trueDamage, float multiplier){
        float true_defence = entity!=null && entity.getPersistentDataContainer().has(Stats.TRUE_DEFENCE.getKey()) ?
                entity.getPersistentDataContainer().get(Stats.TRUE_DEFENCE.getKey(), PersistentDataType.FLOAT) :
                0;
        float defence = entity!=null && entity.getPersistentDataContainer().has(Stats.DEFENCE.getKey()) ?
                entity.getPersistentDataContainer().get(Stats.DEFENCE.getKey(), PersistentDataType.FLOAT) :
                0;
        DamageResult result = new DamageResult() {};
        if(!trueDamage){
            final_damage-=  true_defence;
            final_damage =  ( final_damage / ((defence/100f)+1));
        }
        result.final_damage = final_damage * multiplier;
        result.display = Util.deserialize(type.getSymbol()+" "+ (int) result.final_damage).color(type.getColor());
//        Bukkit.broadcast(result.display.append(Component.text("patched")));
        return result;
    }

    public void recalculate(){
        if(stats==null)return;
        float final_damage = stats.get(Stats.DAMAGE);
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
                if(force <=0) force = stats.get(Stats.STRENGTH)/200;
                final_damage = final_damage * stats.get(Stats.ARROW_MODIFIER);
                if(critical) final_damage += final_damage * (stats.get(Stats.CRIT_DAMAGE)/150);
                final_damage*=force;
            }
            this.final_damage = final_damage;
            this.display = Util.deserialize(
                            (type !=null ? type.getSymbol() : "?")+" "+ (int) this.final_damage)
                    .color(type!=null ? type.getColor() : NamedTextColor.DARK_PURPLE);
//            Bukkit.broadcast(display.append(Component.text("build" + this.final_damage)));
        }
    }

    public static class Builder{
        private final Map<Stats, Float> stats;
        private DamageType type;
        private float force;
        private boolean critical;

        Builder(Map<Stats, Float> stat){
            this.stats = stat;
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
        public DamageResult build(){
            DamageResult result = new DamageResult(stats) {};
            result.type = this.type;
            result.force = this.force;
            result.critical = this.critical;

            result.recalculate();
            return result;
        }
    }
}
