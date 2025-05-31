package me.jasper.jasperproject.JMinecraft.Entity;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.google.common.base.Optional;
import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.HPTrait;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.BukkitAdapter;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftTextDisplay;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;


public class JMob implements Listener {
    @Getter private final LivingEntity mob;
    @Setter public String name = "Jasper Entity";
    @Setter public short level = 1;

    public JMob(net.minecraft.world.entity.LivingEntity entityLiving){
        this.mob = (CraftLivingEntity) entityLiving.getBukkitEntity();
    }

    public JMob setArmor(float defence){ return modifyBaseAttribute(Attributes.ARMOR, defence); }
    public JMob setArmorToughness(float toughness){ return modifyBaseAttribute(Attributes.ARMOR_TOUGHNESS, toughness); }
    public JMob addBaseAttribute(Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute, double value){ return modifyBaseAttribute(attribute, ((CraftLivingEntity) this.mob).getHandle().getAttribute(attribute).getBaseValue()+value); }
    public JMob setEntityScale(float scale){ return modifyBaseAttribute(Attributes.SCALE, scale); }
    public JMob setDamage(int damage){ return modifyBaseAttribute(Attributes.ATTACK_DAMAGE, damage); }
    public JMob modifyBaseAttribute(Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute, double value){
        Objects.requireNonNull(((CraftLivingEntity) mob).getHandle().getAttribute(attribute)).setBaseValue(value);
        return this;
    }
    public JMob setMovementSpeed(float d){
        if(d>0.5f) return this;
        return modifyBaseAttribute(Attributes.MOVEMENT_SPEED, d);
    }
    public JMob setMaxHealth(float d){
        JMob mob = modifyBaseAttribute(Attributes.MAX_HEALTH, d);
        this.mob.setHealth(d);
        return mob;
    }

    private void updateDisplay(){
        updateDisplay(mob);
    }

    public static void updateDisplay(LivingEntity mob){
        PersistentDataContainer pdc = mob.getPersistentDataContainer();
        if(!pdc.has(JKey.MOBATRIBUTE_DISPLAY)) return;
        String string_uuid = pdc.get(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING);
        String name = pdc.get(JKey.MOBATRIBUTE_NAME, PersistentDataType.STRING);
        short level = pdc.get(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT);
        TextDisplay display = (TextDisplay) Bukkit.getEntity(UUID.fromString(string_uuid));
        display.text(Util.deserialize(level +" | "+ name+" | "+ getHealthDisplay(mob.getHealth())));
    }

    public void spawn(Location location){
        MobNameDisplay dis = new MobNameDisplay(location.getWorld());
        dis.spawn(location);

        TextDisplay display = ((CraftTextDisplay) dis.getBukkitEntity());
        display.setVisualFire(false);
        display.setBillboard(Display.Billboard.CENTER);
        PersistentDataContainer pdc = mob.getPersistentDataContainer();
        pdc.set(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING, display.getUniqueId().toString());
        pdc.set(JKey.MOBATRIBUTE_NAME, PersistentDataType.STRING, name);
        pdc.set(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT, level);
        mob.addPassenger(display);
        mob.spawnAt(location);
        updateDisplay();
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
            if(MobRegistry.getInstance().isNPC(entity)) return;
            DamageResult result = null;
            if((e.getDamager() instanceof Player player)){
                JPlayer jPlayer = JPlayer.getJPlayer(player);
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)) {
                    result = DamageResult.patch((float) e.getDamage(), entity, DamageType.MELEE, player.getAttackCooldown());
                }
                 else {
                    float attackCooldown = player.getAttackCooldown();
                    result = jPlayer.attack(null, player.getInventory().getItemInMainHand(), e.isCritical(), attackCooldown);
                    float final_damage = result.getFinal_damage() * attackCooldown;
                    result.setFinal_damage(final_damage);
                    result = DamageResult.patch(result.getFinal_damage(), entity, DamageType.MELEE);
                }
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                if(e.getDamager() instanceof Projectile projectile){
                    if(projectile.getShooter() instanceof Player player){
                        if(projectile instanceof Arrow arrow){
                            entity.setMaximumNoDamageTicks(0);
                            result = JPlayer.getJPlayer(player).shoot(null, arrow.getWeapon(),
                                    arrow.isCritical(), 1F, (float) arrow.getVelocity().length(),
                                    (float) arrow.getDamage());
                        } else {
                            PersistentDataContainer pdc = projectile.getPersistentDataContainer();
                            int damage = pdc.get(JKey.DAMAGE, PersistentDataType.INTEGER);
                            result = DamageResult.patch(damage, entity, DamageType.MAGIC);
                        }
                    }
                }
            }
            if(result==null) {
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
            JMob adapt = BukkitAdapter.adapt(entity);
            if(adapt==null) return;

            DamageResult result = null;
            AttributeInstance maxHealthAttribute = entity.getAttribute(Attribute.MAX_HEALTH);
            float max_health = maxHealthAttribute !=null ? (float) maxHealthAttribute.getBaseValue() : 100;
            if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
                e.setDamage((int) (max_health/25f));
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)){
                entity.setMaximumNoDamageTicks(10);
                e.setDamage((int) (max_health/10f));
            }
            else if(e.getDamageSource().isIndirect()){
                entity.setMaximumNoDamageTicks(10);
                e.setDamage((int) (max_health/5f));
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                float fallDistance = Math.min(entity.getFallDistance(), 100);
                int damage = (int) (max_health * (fallDistance/100));
                e.setDamage(damage);
            } else if (e.getDamageSource().getDamageType().equals(org.bukkit.damage.DamageType.BAD_RESPAWN_POINT)) {
                result = DamageResult.patch((float) e.getDamage(), entity, DamageType.ABSTRACT, true, 1f);
            }
            if(result==null){
                e.setDamage((int) e.getDamage());
                result = DamageResult.patch((float) e.getDamage(), entity, DamageType.MAGIC);
            }
//            e.setCancelled(true);
            e.setDamage(result.getFinal_damage());
            DamageEvent damageEvent = new DamageEvent(result, entity);
            Bukkit.getPluginManager().callEvent(damageEvent);
        }

        @EventHandler
        public void onDeath(EntityDeathEvent e){
//            LivingEntity entity = e.getEntity();
//            if (entity.getPersistentDataContainer().has(JKey.MOBATRIBUTE_DISPLAY)){
//                String s = entity.getPersistentDataContainer().get(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING);
//
//                Bukkit.getEntity(UUID.fromString(s)).remove();
//            }
        }

        @EventHandler
        public void onDespawn(EntityRemoveFromWorldEvent e){
            if(!(e.getEntity() instanceof LivingEntity entity)) return;
            if (entity.getPersistentDataContainer().has(JKey.MOBATRIBUTE_DISPLAY)){
                String s = entity.getPersistentDataContainer().get(JKey.MOBATRIBUTE_DISPLAY, PersistentDataType.STRING);
                if(s!=null){
                    Entity entity1 = Bukkit.getEntity(UUID.fromString(s));
                    if(entity1!=null) entity1.remove();
                }
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
            LivingEntity entity = e.getEntity();
            double width = entity.getWidth();
            Location location = entity.getEyeLocation().clone();
            location.add(
                    rdm.nextDouble(-width, width),
                    rdm.nextFloat(-1f, 1f),
                    rdm.nextDouble(-width, width)
            );
            DamageDisplay damageDisplay = new DamageDisplay(location.getWorld());
            damageDisplay.spawn(location);

            TextDisplay damage_display = ((CraftTextDisplay) damageDisplay.getBukkitEntity());
            damage_display.text(e.getResult().getDisplay());
            damage_display.setBillboard(Display.Billboard.CENTER);

            if(CitizensAPI.getNPCRegistry().isNPC(entity)){
                NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                Optional<HPTrait> hp = npc.getTraitOptional(HPTrait.class);
                if(hp.isPresent()){
                    npc.setName( ChatColor.GRAY+"[Lv."+hp.get().getLvl()+"] "
                            +ChatColor.BLACK+hp.get().getName2()+ChatColor.GRAY+" | "+ChatColor.RED
                            +Util.satuan(entity.getHealth()-e.getResult().getFinal_damage()) +"/"+Util.satuan(entity.getMaxHealth())+" ❤ ");
                }
            }
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
