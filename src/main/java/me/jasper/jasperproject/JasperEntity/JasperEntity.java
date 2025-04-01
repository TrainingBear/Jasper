package me.jasper.jasperproject.JasperEntity;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;

import java.util.Objects;


public class JasperEntity implements Listener {
    private Location location;
    private String b4_health_display = ChatColor.RED+" ";
    private EntityType type;
    @Getter private String name;
    @Getter private String health_display;
    @Getter private TextDisplay mob_nameAttribute;
    @Getter private LivingEntity mob;

    public JasperEntity(LivingEntity initializedEntity){
        this.mob = initializedEntity;
        this.mob_nameAttribute = (TextDisplay) initializedEntity.getPassengers().stream().filter(entity -> entity.getScoreboardTags().contains("MobName")).findFirst().orElse(null);
        this.health_display = initializedEntity.getHealth() + " ❤ ";
        this.name = initializedEntity.getScoreboardTags().stream().filter(s -> !Objects.equals(s, "JasperMob")).findFirst().orElse("JasperMob");
    }
    public JasperEntity(EntityType entity, Location location, String name){
        type = entity;
        this.name = name;
        this.location = location;
        mob_nameAttribute = (TextDisplay) JasperEntity.this.location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
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
        mob.addScoreboardTag(this.name);
        mob_nameAttribute.getScoreboardTags().add("MobName");
        mob_nameAttribute.setText(b4_health_display+health_display+name);
        mob_nameAttribute.setBackgroundColor(Color.ORANGE.setAlpha(1));
//        mob_nameAttribute.getBackgroundColor().setAlpha(100);
        mob_nameAttribute.setBillboard(Display.Billboard.CENTER);
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

    public JasperEntity damageThisMob(int damage){
        mob.setHealth(Math.max(0, mob.getHealth() - damage));
        return this;
    }

    public JasperEntity updateHealthDisplay() {
        mob_nameAttribute.setText(b4_health_display+
                getHealthDisplay(this.mob.getHealth())+
                name);
        return this;
    }

    private String getHealthDisplay(double health){
        if(health >= 1000000000) return ItemAbility.round((float) health/1000000000, 2) + "B ❤ "; //milyar/billion
        else if(health >= 1000000) return ItemAbility.round((float) health/1000000, 2)+"M ❤ ";//juta
        else if(health >= 1000) return ItemAbility.round((float) health/1000, 2)+"k ❤ "; //seribu
        else return health +" ❤ ";
    }

    public JasperEntity setMaxHealth(double v){
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(v);
        mob.setHealth(v);
        mob_nameAttribute.setText(b4_health_display+getHealthDisplay(mob.getHealth())+ name);
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
