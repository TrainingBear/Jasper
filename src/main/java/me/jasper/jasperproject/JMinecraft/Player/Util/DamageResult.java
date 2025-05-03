package me.jasper.jasperproject.JMinecraft.Player.Util;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;

@Getter @Setter
public class DamageResult {
    private final DamageType type;
    private final int rawDamage;
    private final boolean critical;
    @Setter private int defence;
    @Setter private boolean trueDamage;
    @Setter private int true_defence;
    private int final_damage;
    private Component display;

    public DamageResult(DamageType type, int damage, boolean critical, int defence, boolean trueDamage, int true_defence, int finalDamage, Component display) {
        this.type = type;
        this.rawDamage = damage;
        this.critical = critical;
        this.defence = defence;
        this.trueDamage = trueDamage;
        this.true_defence = true_defence;
        this.final_damage = finalDamage;
        this.display = display;
    }

    public static Builder builder(){
        return new Builder();
    }

    public void recalculate(){
        int final_damage = rawDamage;
        label: {
            if(trueDamage) break label;
            final_damage-=true_defence;
            final_damage = (int) ((float) final_damage / ((float) defence/100f)+1);
        }
        Component display = Util.deserialize(type.getSymbol()+" "+final_damage).color(type.getColor());
        this.display = display;
        this.final_damage = final_damage;
    }

    public static class Builder{
        private DamageType type = DamageType.ABSTRACT;
        private int damage;
        private boolean critical;
        private int defence;
        private int true_defence = 0;
        private boolean trueDamage;

        public Builder type(DamageType type){
            this.type = type;
            return this;
        }
        public Builder damage(int damage){
            this.damage = damage;
            return this;
        }
        public Builder critical(boolean b){
            critical = b;
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
            int final_damage = damage;
            label: {
                if(trueDamage) break label;
                final_damage-=true_defence;
                final_damage = (int) ((float) final_damage / ((float) defence/100f)+1);
            }
            Component display = Util.deserialize(type.getSymbol()+" "+final_damage).color(type.getColor());
            return new DamageResult(type, damage, critical, defence, true, true_defence, final_damage, display);
        }
    }
}
