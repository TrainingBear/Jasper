package me.jasper.jasperproject.JMinecraft.Entity;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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

public class JMob implements Listener {
    @Getter private CraftLivingEntity mob;

    public JMob(net.minecraft.world.entity.LivingEntity entityLiving){
        this.mob = (CraftLivingEntity) entityLiving.getBukkitEntity();
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
        int level = (int) pdc.get(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT);
        display.text(Util.deserialize(level +" | "+name+" | "+ getHealthDisplay(mob.getHealth())));
    }

    public static void updateDisplay(LivingEntity entity){
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        String string_uuid = pdc.get(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING);
        TextDisplay display = (TextDisplay) Bukkit.getEntity(UUID.fromString(string_uuid));
        String name = pdc.get(JKey.MOBATRIBUTE_NAME, PersistentDataType.STRING);
        int level = (int) pdc.get(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT);
        display.text(Util.deserialize(level +" | "+name+" | "+ getHealthDisplay(entity.getHealth())));
    }

    public void spawn(Location location){
        TextDisplay display= location.getWorld().spawn(location, TextDisplay.class);
        display.setVisualFire(false);
        display.setBillboard(Display.Billboard.CENTER);
        mob.getPersistentDataContainer().set(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING, display.getUniqueId().toString());
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

    public static String getHealthDisplay(double health){
        if(health >= 1_000_000_000f) return Util.round (health/1_000_000_000 ,1) + "B ❤ "; //milyar/billion
        else if(health >= 1_000_000) return Util.round ( health/1_000_000_000 ,1) +"M ❤ ";//juta
        else if(health >= 1_000) return Util.round ( health/1_000,1)+"k ❤ "; //seribu
        else if(health < 1_000) return Util.round ( health,1)+" ❤ ";
        else return health +" ❤ ";
    }

    /**
     * spawn some text above the entity display if it has and disappear based on param <b>howLong</b>
     * @param entitas the entity
     * @param text the text to display
     * @param howLong how long the display appear in Ticks
     */
    public static void say(LivingEntity entitas, Component text, long howLong){
        PersistentDataContainer pdc = entitas.getPersistentDataContainer();
        if(pdc.has(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING)){
            TextDisplay txtdsply = entitas.getWorld().spawn(entitas.getLocation(), TextDisplay.class);
            txtdsply.setVisualFire(false);
            txtdsply.text(text);
            txtdsply.setBillboard(Display.Billboard.CENTER);
            Bukkit.getEntity(UUID.fromString(pdc.get(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING))).addPassenger(txtdsply);

            Bukkit.getScheduler().runTaskLater(JasperProject.getPlugin(), txtdsply::remove, howLong);
        }
    }

    public static class MobListener implements Listener {
        @EventHandler
        public void onHurt(EntityDamageByEntityEvent e){
            if(!(e.getEntity() instanceof LivingEntity entity)) return;
            DamageResult result = null;
            if((e.getDamager() instanceof Player player)){
                entity.setMaximumNoDamageTicks(0);
                JPlayer jPlayer = PlayerManager.getJPlayer(player);
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)) {
                    result = DamageResult.patch((float) e.getDamage(), entity, DamageType.MELEE, player.getAttackCooldown());
                }
                 else {
                    float attackCooldown = player.getAttackCooldown();
                    result = jPlayer.attack(null, player.getInventory().getItemInMainHand(), e.isCritical(), attackCooldown);
                    Bukkit.broadcastMessage("before = "+result.getFinal_damage());
                    float final_damage = result.getFinal_damage() * attackCooldown;
                    result.setFinal_damage(final_damage);
                    Bukkit.broadcastMessage(final_damage +" | after = "+result.getFinal_damage());
                    result = DamageResult.patch(result.getFinal_damage(), entity, DamageType.MELEE);
                }
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                if(e.getDamager() instanceof Projectile projectile){
                    if(projectile.getShooter() instanceof Player player){
                        if(projectile instanceof Arrow arrow){
                            entity.setMaximumNoDamageTicks(0);
                            result = PlayerManager.getJPlayer(player).shoot(null, arrow.getWeapon(),
                                    arrow.isCritical(), 1F, (float) arrow.getVelocity().length(),
                                    (float) arrow.getDamage());
                        } else {
                            PersistentDataContainer pdc = projectile.getPersistentDataContainer();
                        }
                    }
                }
            }
            if(result==null) {
                Bukkit.broadcastMessage("Invoked null");
                if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                    result = DamageResult.patch((float) e.getDamage(), entity, DamageType.PROJECTILE);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                    result = DamageResult.patch((float) e.getDamage(), entity, DamageType.MELEE);
                } else{
                    result = DamageResult.patch((float) e.getDamage(), entity, DamageType.ABSTRACT);
                }
            }
            e.setDamage(result.getFinal_damage());
            DamageEvent damageEvent = new DamageEvent(result, entity);
            if(e.isCancelled()) damageEvent.setCancelled(true);
            Bukkit.getPluginManager().callEvent(damageEvent);
        }

        @EventHandler
        public void onHurtByNonEntity(EntityDamageEvent e){
            if(e.getDamageSource().getCausingEntity() != null) return;
            if(!(e.getEntity() instanceof LivingEntity entity)) return;
            DamageResult result = null;
            AttributeInstance maxHealthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            float max_health = maxHealthAttribute !=null ? (float) maxHealthAttribute.getBaseValue() : 100;
            if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
                result = DamageResult.patch(max_health/25f, entity, DamageType.FIRE);
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)){
                entity.setFireTicks(100);
                entity.setMaximumNoDamageTicks(10);
                result = DamageResult.patch(max_health/10f, entity, DamageType.FIRE);
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)){
                entity.setFireTicks(100);
                entity.setMaximumNoDamageTicks(10);
                result = DamageResult.patch(max_health/5f, entity, DamageType.FIRE);
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                float fallDistance = Math.min(entity.getFallDistance(), 100);
                int damage = (int) (max_health * (fallDistance/100));
                result = DamageResult.patch(damage, entity, DamageType.ABSTRACT, true, 1f);
            }
            if(result==null) result = DamageResult.patch((float) e.getDamage(), entity, DamageType.MAGIC);

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

        @EventHandler
        public void onDespawn(EntityRemoveFromWorldEvent e){
            if(!(e.getEntity() instanceof LivingEntity entity)) return;
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
            Random rdm = new Random();
            double width = e.getEntity().getWidth();
            Location location = e.getEntity().getEyeLocation().clone();
            location.add(
                    rdm.nextDouble(-width, width),
                    rdm.nextFloat(-1f, 1f),
                    rdm.nextDouble(-width, width)
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
