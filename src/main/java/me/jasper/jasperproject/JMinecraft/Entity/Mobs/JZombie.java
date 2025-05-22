package me.jasper.jasperproject.JMinecraft.Entity.Mobs;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Entity.JMob;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;

import java.util.Set;

@Getter
public class JZombie extends Zombie {
    private final JMob delegate;
    public JZombie(World world) {
        this(((CraftWorld) world).getHandle());
    }
    public JZombie(Level world) {
        super(EntityType.ZOMBIE, world);
        this.delegate = new JMob(this);
        this.delegate.setLevel((short) 0);
        this.delegate.setName("Zombie");
        this.delegate.setMovementSpeed(0.1f).setArmor(10f).setMaxHealth(1000).setDamage(10);
        this.delegate.getMob().setMaximumNoDamageTicks(0);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damagesource, float f) {
        return super.hurtServer(level, damagesource, f);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void lavaHurt() {
        if (!this.fireImmune()) {
            if (this.getRemainingFireTicks() <= 0) {
                this.igniteForSeconds(15, false);
            } else {
                this.igniteForSeconds(15.0F, false);
            }
            Level world = this.level();
            if (this.tickCount%20==0 && world instanceof ServerLevel worldserver) {
                float lava_damage = ((int) (this.getMaxHealth()/5f));
                if (super.hurtServer(worldserver, super.damageSources().lava(), lava_damage)
                        && this.shouldPlayLavaHurtSound() && !this.isSilent()) {
                    worldserver.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_BURN, this.getSoundSource(), 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
                }
            }
        }
    }

}
