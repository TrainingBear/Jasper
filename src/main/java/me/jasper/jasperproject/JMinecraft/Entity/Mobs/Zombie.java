package me.jasper.jasperproject.JMinecraft.Entity.Mobs;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Entity.JMob;
import me.jasper.jasperproject.JMinecraft.Entity.NMS_ENTITY_1_21;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.World;

@Getter
public class Zombie extends EntityZombie {
    private final JMob<EntityZombie> delegate;
    public Zombie(World world) {
        super(NMS_ENTITY_1_21.ZOMBIE, world);
        this.delegate = new JMob<>(this);
        this.delegate.setLevel((short) 1).setName("Zombie");
        this.delegate.setSpeed(0.1f).setDefence(10f).setMaxHealth(1000).setDamage(1000);
        delegate.getMob().setMaximumNoDamageTicks(0);
        delegate.getMob().setFireTicks(20);
    }

    public boolean a(DamageSource s, float f){
        return super.a(s, f);
    }
}
