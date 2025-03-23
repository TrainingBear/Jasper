package me.jasper.jasperproject.JasperEntity;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;


public class JasperEntity implements Listener {
    private Location location;
    private String b4_health_display = ChatColor.RED+" ";
    private EntityType type;
    @Getter private String name;
    @Getter private String health_display;
    @Getter private ArmorStand mob_nameAttribute;
    @Getter private LivingEntity mob;

    public JasperEntity(LivingEntity initializedEntity){
        mob = initializedEntity;
        mob_nameAttribute = (ArmorStand) initializedEntity.getPassengers().stream().filter(entity -> entity.getScoreboardTags().contains("MobName")).findFirst().orElse(null);
        health_display = initializedEntity.getHealth() + " ❤ ";
        name = mob_nameAttribute.getCustomName().substring(health_display.length()+b4_health_display.length());
    }
    public JasperEntity(EntityType entity, Location location){
        type = entity;
        this.location = location;
        mob_nameAttribute = (ArmorStand) JasperEntity.this.location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
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
        mob_nameAttribute.setVisible(false);
        mob_nameAttribute.setCustomNameVisible(true);
        mob_nameAttribute.setSmall(true);
        mob_nameAttribute.setGravity(false);
        mob_nameAttribute.setMarker(true);
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

    public JasperEntity setName(String name){
        this.name = name;
        mob_nameAttribute.setCustomName(b4_health_display+health_display+ name);
        return this;
    }

    public JasperEntity damageThisMob(double damage){
        mob.setHealth(Math.max(0, mob.getHealth() - damage));
        return this;
    }

    public JasperEntity updateHealthDisplay(double damage) {
        this.health_display = Math.floor(mob.getHealth() - damage)+" ❤ ";
        mob_nameAttribute.setCustomName(b4_health_display+health_display+ name);
        return this;
    }

    public JasperEntity setHealth(double v){
        mob.setHealth(v);
        mob_nameAttribute.setCustomName(b4_health_display+health_display+ name);
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
