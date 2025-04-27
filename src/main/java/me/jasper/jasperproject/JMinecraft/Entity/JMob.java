package me.jasper.jasperproject.JMinecraft.Entity;

import lombok.Getter;
import me.jasper.jasperproject.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;

import java.util.Objects;


public class JMob implements Listener {
    private Location location;
    private String prefix = ChatColor.RED+" ";
    private EntityType type;
    @Getter private String name;
    @Getter private String health_display;
    @Getter private TextDisplay mob_nameAttribute;
    @Getter private LivingEntity mob;

    public JMob(LivingEntity initializedEntity){
        this.mob = initializedEntity;
        this.mob_nameAttribute = (TextDisplay) initializedEntity.getPassengers().stream().filter(entity -> entity.getScoreboardTags().contains("MobName")).findFirst().orElse(null);
        this.health_display = initializedEntity.getHealth() + " ❤ ";
        this.name = initializedEntity.getScoreboardTags().stream().filter(s -> !Objects.equals(s, "JasperMob")).findFirst().orElse("JasperMob");
    }

    public JMob(EntityType entity, Location location, String name){
        type = entity;
        this.name = name;
        this.location = location;
        mob_nameAttribute = (TextDisplay) JMob.this.location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
        init();
    }

    public static JMob getMob(LivingEntity entity){
        if(entity.getScoreboardTags().contains("JasperMob")){
            return new JMob(entity);
        }
        return null;
    }

    private void init(){
        mob = (LivingEntity) location.getWorld().spawnEntity(location, type);
        health_display = mob.getHealth() + " ❤ ";
        mob.addScoreboardTag("JasperMob");
        mob.addScoreboardTag(this.name);
        mob_nameAttribute.getScoreboardTags().add("MobName");
        mob_nameAttribute.setText(prefix +health_display+name);
        mob_nameAttribute.setBackgroundColor(Color.ORANGE.setAlpha(1));
//        mob_nameAttribute.getBackgroundColor().setAlpha(100);
        mob_nameAttribute.setBillboard(Display.Billboard.CENTER);
        mob.addPassenger(mob_nameAttribute);
        Bukkit.broadcastMessage("MOB INITIALIZED");
    }

    public JMob playHurtAnimation(Player player){
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

    public JMob damageThisMob(int damage){
        mob.setHealth(Math.max(0, mob.getHealth() - damage));
        return this;
    }

    public JMob updateHealthDisplay() {
        mob_nameAttribute.setText(prefix +
                getHealthDisplay(this.mob.getHealth())+
                name);
        return this;
    }

    private String getHealthDisplay(double health){
        if(health >= 1000000000) return Util.round((float) health/1000000000, 2) + "B ❤ "; //milyar/billion
        else if(health >= 1000000) return Util.round((float) health/1000000, 2)+"M ❤ ";//juta
        else if(health >= 1000) return Util.round((float) health/1000, 2)+"k ❤ "; //seribu
        else return health +" ❤ ";
    }

    public JMob setMaxHealth(double v){
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(v);
        mob.setHealth(v);
        mob_nameAttribute.setText(prefix +getHealthDisplay(mob.getHealth())+ name);
        return this;
    }

    public JMob setAgroRange(double v){
        mob.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(v);
        return this;
    }

    public JMob setDamage(double v){
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(v);
        return this;
    }

    public JMob setKB_Resistance(double v){
        mob.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(v);
        return this;
    }

    public JMob setKB_Attack(double v){
        mob.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(v);
        return this;
    }

    public JMob setDefense(double v){
        mob.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(v);
        return this;
    }

}
