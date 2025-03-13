package me.jasper.jasperproject.JasperEntity;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;


public class JasperEntity implements Listener {
    private Location location;
    private String b4_health_display = ChatColor.RED+" ";
    private EntityType type;
    @Getter private String name;
    @Getter private String health_display;
    @Getter private LivingEntity mob;

    public JasperEntity(LivingEntity initializedEntity){
    }
        type = entity;
        this.location = location;
        init();
    }

    public static JasperEntity getMob(LivingEntity entity){
        if(entity.getScoreboardTags().contains("JasperMob")){
            return new JasperEntity(entity);
        }
        return null;
    }

    private void init(){
        mob = (LivingEntity) location.getWorld().spawnEntity(location, type);
        health_display = mob.getHealth() + " ❤ ";
        mob.addScoreboardTag("JasperMob");
        mob_nameAttribute.getScoreboardTags().add("MobName");
        mob.addPassenger(mob_nameAttribute);
        Bukkit.broadcastMessage("MOB INITIALIZED");
    }

    public JasperEntity playHurtAnimation(Player player){
        mob.playHurtAnimation(0f);
        mob.getWorld().playSound(mob.getLocation(), mob.getHurtSound(), 1, 0.5f);
        mob.setVelocity(
                mob.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1/mob.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue())
        );
        return this;
    }

    public void kill(){
        if(!mob.isDead()) mob.setHealth(0);
        mob_nameAttribute.remove();
    }

    public void despawn(){
        mob.remove();
        mob_nameAttribute.remove();
    }

        mob.setHealth(Math.max(0, mob.getHealth() - damage));
        return this;
    }

        return this;
    }

        mob.setHealth(v);
        return this;
    }

    public JasperEntity setAgroRange(double v){
        mob.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(v);
        return this;
    }

    public JasperEntity setDamage(double v){
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(v);
        return this;
    }

    public JasperEntity setKB_Resistance(double v){
        mob.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(v);
        return this;
    }

    public JasperEntity setKB_Attack(double v){
        mob.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(v);
        return this;
    }

    public JasperEntity setDefense(double v){
        mob.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(v);
        return this;
    }

}
