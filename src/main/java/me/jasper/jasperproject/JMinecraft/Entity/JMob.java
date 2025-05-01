package me.jasper.jasperproject.JMinecraft.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JMinecraft.Player.PlayerManager;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftTextDisplay;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class JMob<T extends EntityLiving> implements Listener {
    @Getter private CraftLivingEntity mob;
    @Getter private TextDisplay display;

    public JMob(EntityLiving entityLiving, World world){
        CraftWorld world1 = world.getWorld();
        this.mob = (CraftLivingEntity) entityLiving.getBukkitEntity();
        this.display = world1.spawn(world1.getSpawnLocation(), TextDisplay.class);
        this.display.setVisualFire(false);
        this.display.setBillboard(Display.Billboard.CENTER);
        mob.getPersistentDataContainer().set(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING, display.getUniqueId().toString());
    }

    public JMob setLevel(short level){
        mob.getPersistentDataContainer().set(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT, level);
        return this;
    }

    public JMob setName(String name){
        mob.getPersistentDataContainer().set(JKey.MOBATRIBUTE_NAME, PersistentDataType.STRING, name);
        return this;
    }

    public JMob setMaxHealth(float d){
        AttributeInstance attribute = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(attribute!=null) {
            short level = mob.getPersistentDataContainer().get(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT);
            float v = d * level / 10;
            attribute.setBaseValue(v);
            mob.setHealth(d);
        }
        return this;
    }
    public JMob setSpeed(float d){
        if(d>0.5f) return this;
        AttributeInstance attribute = mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if(attribute!=null)attribute.setBaseValue(d);
        return this;
    }
    public JMob setDefence(float defence){
        mob.getPersistentDataContainer().set(Stats.DEFENCE.getKey(), PersistentDataType.FLOAT, defence);
        return this;
    }
    public JMob setDamage(int damage){
        AttributeInstance attribute = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if(attribute!=null) attribute.setBaseValue(damage);
        return this;
    }

    private void updateDisplay(){
        CraftPersistentDataContainer pdc = mob.getPersistentDataContainer();
        String string_uuid = pdc.get(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING);
        TextDisplay display = (TextDisplay) Bukkit.getEntity(UUID.fromString(string_uuid));
        String name = pdc.get(JKey.MOBATRIBUTE_NAME, PersistentDataType.STRING);
        int health = (int) mob.getHealth();
        int level = (int) pdc.get(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT);
        display.text(Util.deserialize(level +" | "+name+" | "+ health +" ❤ "));
    }

    public static void updateDisplay(LivingEntity entity){
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if(!pdc.has(JKey.MOBATRIBUTE_DISPLAY)) return;
        String string_uuid = pdc.get(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING);
        TextDisplay display = (TextDisplay) Bukkit.getEntity(UUID.fromString(string_uuid));
        String name = pdc.get(JKey.MOBATRIBUTE_NAME, PersistentDataType.STRING);
        int health = (int) entity.getHealth();
        int level = (int) pdc.get(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT);
        display.text(Util.deserialize(level +" | "+name+" | "+ health +" ❤ "));
    }

    public void spawn(Location location){
        updateDisplay();
        mob.addPassenger(display);
        mob.spawnAt(location);
    }

    public String getHealthDisplay(double health){
        if(health >= 1000000000) return Util.round((float) health/1000000000, 2) + "B ❤ "; //milyar/billion
        else if(health >= 1000000) return Util.round((float) health/1000000, 2)+"M ❤ ";//juta
        else if(health >= 1000) return Util.round((float) health/1000, 2)+"k ❤ "; //seribu
        else return health +" ❤ ";
    }

    public static class MobListener implements org.bukkit.event.Listener {
        @EventHandler
        public void onHurt(EntityDamageByEntityEvent e){
            if(!(e.getDamager() instanceof Player )){
                Player player = (Player) e.getDamager();
                JPlayer jPlayer = PlayerManager.getJPlayer(player);
                float defence = e.getEntity().getPersistentDataContainer().get(Stats.DEFENCE.getKey(), PersistentDataType.FLOAT);
                e.setDamage(jPlayer.attack(JPlayer.AttackType.MELEE, defence));
                updateDisplay((LivingEntity) e.getEntity());
            }
        }
    }
}
