package me.jasper.jasperproject.JMinecraft.Entity.Mobs;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Entity.JMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

@Getter
public class JZombie extends Zombie {
    private final JMob delegate;
    public JZombie(Level world) {
        super(EntityType.ZOMBIE, world);
        this.delegate = new JMob(this);
        this.delegate.setLevel((short) 0).setName("Zombie");
        this.delegate.setSpeed(0.1f).setDefence(10f).setMaxHealth(1000).setDamage(1000);
        delegate.getMob().setMaximumNoDamageTicks(20);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damagesource, float f) {
        return super.hurtServer(level, damagesource, f);
    }
}
