package me.jasper.jasperproject.JMinecraft.Entity;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorType;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JMinecraft.Player.PlayerManager;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;
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
        mob.getPersistentDataContainer().set(JKey.MOBATRIBUTE_REPLACE_HITREGIS, PersistentDataType.BOOLEAN, true);
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
        short level = mob.getPersistentDataContainer().get(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT);
        int v = (int) (d + (d*((float) level /10)));
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(v);
        mob.setHealth(v);
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

    public static void hurt(LivingEntity entity, @Nullable Entity damager, int damage){
        if (damager!=null) entity.setVelocity(damager.getLocation().toVector());
        entity.playHurtAnimation(1f);
        entity.getLocation().getWorld().playSound(entity.getLocation(), entity.getHurtSound(), 10f, 1f);
        entity.setHealth(Math.max(0, entity.getHealth() - damage));
    }

    public String getHealthDisplay(double health){
        if(health >= 1000000000) return Util.round((float) health/1000000000, 2) + "B ❤ "; //milyar/billion
        else if(health >= 1000000) return Util.round((float) health/1000000, 2)+"M ❤ ";//juta
        else if(health >= 1000) return Util.round((float) health/1000, 2)+"k ❤ "; //seribu
        else return health +" ❤ ";
    }

    public static class MobListener implements Listener {
        @EventHandler
        public void onHurt(EntityDamageByEntityEvent e){
            if(!(e.getEntity() instanceof LivingEntity entity)) return;
            DamageResult result = null;
            Bukkit.broadcastMessage(e.getCause().name());
            if((e.getDamager() instanceof Player player)){
                JPlayer jPlayer = PlayerManager.getJPlayer(player);
                 if (e.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)) {
                    result = DamageResult.builder()
                            .damage((int) e.getDamage())
                            .type(DamageType.ABSTRACT)
                            .build();
                } else {
                    result = jPlayer.attack(null, ArmorType.MAIN_HAND, DamageType.MELEE, e.isCritical(), 1);
                }
                entity.setNoDamageTicks(0);
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                result = DamageResult.builder()
                        .type(DamageType.PROJECTILE)
                        .damage((int) e.getDamage())
                        .build();
            }
            if(result==null) result = DamageResult.builder()
                    .type(DamageType.MAGIC)
                    .damage((int) e.getDamage())
                    .build();

            float true_defence = entity.getPersistentDataContainer().has(Stats.TRUE_DEFENCE.getKey()) ?
                    entity.getPersistentDataContainer().get(Stats.TRUE_DEFENCE.getKey(), PersistentDataType.FLOAT) :
                    0;
            float defence = entity.getPersistentDataContainer().has(Stats.DEFENCE.getKey()) ?
                    entity.getPersistentDataContainer().get(Stats.DEFENCE.getKey(), PersistentDataType.FLOAT) :
                    0;
            result.setDefence((int) defence);
            result.setTrue_defence((int) true_defence);
            result.recalculate();
            e.setDamage(result.getFinal_damage());
            DamageEvent damageEvent = new DamageEvent(result, entity);
            if(e.isCancelled()) damageEvent.setCancelled(true);
            Bukkit.getPluginManager().callEvent(damageEvent);
        }

        @EventHandler
        public void onHurtByNonEntity(EntityDamageEvent e){
            if((e.getDamageSource().getCausingEntity() instanceof Player)) return;
            if(!(e.getEntity() instanceof LivingEntity entity)) return;
            DamageResult result = null;
            AttributeInstance maxHealthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
                double max_health = maxHealthAttribute !=null ? maxHealthAttribute.getBaseValue() : 100;
                result = DamageResult.builder()
                        .type(DamageType.FIRE)
                        .damage((int) (max_health/25))
                        .build();
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)){
                double max_health = maxHealthAttribute !=null ? maxHealthAttribute.getBaseValue() : 100;
                result = DamageResult.builder()
                        .type(DamageType.FIRE)
                        .damage((int) (max_health/10))
                        .build();
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)){
                double max_health = maxHealthAttribute !=null ? maxHealthAttribute.getBaseValue() : 100;
                result = DamageResult.builder()
                        .type(DamageType.FIRE)
                        .damage((int) (max_health/5))
                        .build();
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                double max_health = maxHealthAttribute.getBaseValue();
                float fallDistance = Math.min(entity.getFallDistance(), 100);
                int damage = (int) (max_health * (fallDistance/100));
                result = DamageResult.builder()
                        .type(DamageType.ABSTRACT)
                        .damage(damage)
                        .trueDamage(true)
                        .build();
            }
            if(result==null) result = DamageResult.builder()
                    .type(DamageType.MAGIC)
                    .damage((int) e.getDamage())
                    .build();

            float true_defence = entity.getPersistentDataContainer().has(Stats.TRUE_DEFENCE.getKey()) ?
                    entity.getPersistentDataContainer().get(Stats.TRUE_DEFENCE.getKey(), PersistentDataType.FLOAT) :
                    0;
            float defence = entity.getPersistentDataContainer().has(Stats.DEFENCE.getKey()) ?
                    entity.getPersistentDataContainer().get(Stats.DEFENCE.getKey(), PersistentDataType.FLOAT) :
                    0;
            result.setDefence((int) defence);
            result.setTrue_defence((int) true_defence);
            result.recalculate();
            e.setDamage(result.getFinal_damage());
            DamageEvent damageEvent = new DamageEvent(result, entity);
            if(e.isCancelled()) damageEvent.setCancelled(true);
            Bukkit.getPluginManager().callEvent(damageEvent);
        }

        @EventHandler
        public void onDeath(EntityDeathEvent e){
            LivingEntity entity = e.getEntity();
            if (entity.getPersistentDataContainer().has(JKey.MOBATRIBUTE_DISPLAY)){
                String s = entity.getPersistentDataContainer().get(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING);
                Bukkit.getEntity(UUID.fromString(s)).remove();
            }
        }
    }

    public static class DamageEvent extends Event implements Listener, Cancellable {
        private final static HandlerList handle = new HandlerList();
        @Getter private DamageResult result;
        @Getter private LivingEntity entity;
        private boolean cancelled;

        public DamageEvent(DamageResult result, LivingEntity entity){
            this.result = result;
            this.entity = entity;
        }

        public DamageEvent() {

        }

        @EventHandler
        public void onDamage(DamageEvent e){
            if(e.isCancelled()) return;
            Random random = new Random();
            Location location = e.getEntity().getEyeLocation().clone();
            location.add(
                    random.nextFloat(-1f, 1f),
                    random.nextFloat(-1f, 1f),
                    random.nextFloat(-1f, 1f)
            );
            TextDisplay damage_display = location.getWorld().spawn(location, TextDisplay.class);
            updateDisplay(e.getEntity());
            damage_display.text(e.getResult().getDisplay());
            damage_display.setBillboard(Display.Billboard.CENTER);
            Bukkit.getScheduler().runTaskLater(JasperProject.getPlugin(), damage_display::remove, 30L);
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            return handle;
        }
        public static @NotNull HandlerList getHandlerList() {
            return handle;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean b) {
            cancelled = b;
        }
    }
}
