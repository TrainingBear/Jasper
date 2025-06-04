package me.jasper.jasperproject.JMinecraft.Player;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Player.Ability.Mage;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorEquipEvent;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorType;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageResult;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.Util.JKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public class JPlayer implements Listener {
    @Getter private static final Map<UUID, JPlayer> onlinePlayers = new HashMap<>();
    Map<ArmorType, ItemStack> lastItems = new HashMap<>(ArmorType.values().length);
    @Setter private String lastInstance;
    private UUID UUID;

    public JPlayer(){ }

    public JPlayer(Player player){
        this.UUID = player.getUniqueId();
    }

    public static JPlayer register(Player player){
        if(!onlinePlayers.containsKey(player.getUniqueId())){
            onlinePlayers.put(player.getUniqueId(), new JPlayer(player));
        }
        return onlinePlayers.get(player.getUniqueId());
    }
    public static JPlayer unregister(Player player){
        return onlinePlayers.remove(player.getUniqueId());
    }
    public static JPlayer getJPlayer(Player player){
        return register(player);
    }
    public static Player getBukkitPlayer(JPlayer player){
        return Bukkit.getPlayer(player.getUUID());
    }

    public PlayerGroup createGroup(JPlayer... member){
        PlayerGroup playerGroup = new PlayerGroup(this);
        playerGroup.addMember(List.of(member));
        return playerGroup;
    }

    public DamageResult shoot(@Nullable LivingEntity target, ItemStack weapon, boolean critical, float modifier, float force, float arrow_damage){
        Player bukkitPlayer = getBukkitPlayer();
        Map<Stats, Float> player_stats;
        boolean valid = weapon.hasItemMeta() && weapon.getItemMeta().getPersistentDataContainer().has(JKey.Category) && ItemType.isMelee(weapon.getItemMeta().getPersistentDataContainer().get(JKey.Category, PersistentDataType.STRING));
        player_stats = Stats.getCombatStats(bukkitPlayer, valid ? weapon : null);
        player_stats.put(Stats.DAMAGE, arrow_damage);
        DamageResult result = DamageResult.builder(player_stats)
                .critical(critical)
                .type(DamageType.PROJECTILE)
                .force(force)
                .build();
        result.setFinal_damage(result.getFinal_damage()*modifier);
        if(target!=null)target.damage(result.getFinal_damage(), DamageSource.builder(org.bukkit.damage.DamageType.FALLING_STALACTITE) .withCausingEntity(bukkitPlayer) .withDirectEntity(bukkitPlayer) .build() );
        return result;
    }
    public DamageResult attack(@Nullable LivingEntity target, ItemStack weapon, boolean critical){
        return attack(target, weapon, critical, 1f);
    }
    public DamageResult attack(@Nullable LivingEntity target, ItemStack weapon, boolean critical, float modifier){
        Player bukkitPlayer = getBukkitPlayer();
        Map<Stats, Float> player_stats;
        boolean valid = weapon.hasItemMeta() && weapon.getItemMeta().getPersistentDataContainer().has(JKey.Category) && ItemType.isMelee(weapon.getItemMeta().getPersistentDataContainer().get(JKey.Category, PersistentDataType.STRING));
        player_stats = Stats.getCombatStats(bukkitPlayer, valid ? weapon : null);
        boolean isCritical = bukkitPlayer.isSneaking()&& critical&& ( !bukkitPlayer.hasPotionEffect(PotionEffectType.BLINDNESS) || !bukkitPlayer.hasPotionEffect(PotionEffectType.NAUSEA) || !bukkitPlayer.hasPotionEffect(PotionEffectType.POISON) || !bukkitPlayer.hasPotionEffect(PotionEffectType.WITHER) || !bukkitPlayer.isFrozen());
        DamageResult result = DamageResult.builder(player_stats)
                .critical(isCritical)
                .type(DamageType.MELEE)
                .build();
        if(target!=null)target.damage(result.getFinal_damage()*modifier, DamageSource.builder(org.bukkit.damage.DamageType.FALLING_BLOCK) .withCausingEntity(bukkitPlayer) .withDirectEntity(bukkitPlayer) .build() );
        return result;
    }

    public Player getBukkitPlayer(){
        return Bukkit.getPlayer(this.UUID);
    }


    public void setLastItems(ArmorType type, ItemStack item){
        lastItems.put(type, item);
    }

    public @Nullable ItemStack getLastItems(ArmorType type){ return lastItems.get(type); }

    @EventHandler
    public void onEquip(ArmorEquipEvent e){
        if(e.isCancelled()) return;
        Player player = e.getPlayer();
        Stats.apply(player, e.getNewArmorPiece(), e.getType());
    }

    @EventHandler
    public void onSwapHotbar(PlayerItemHeldEvent e){
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e){
        if(!(e.getEntity() instanceof Player bukkitPlayer)) return;
        Map<Stats, Float> player_stats = Stats.getCombatStats(bukkitPlayer, e.getBow());
        boolean critical = !bukkitPlayer.hasPotionEffect(PotionEffectType.BLINDNESS) ||
                !bukkitPlayer.hasPotionEffect(PotionEffectType.NAUSEA) ||
                !bukkitPlayer.hasPotionEffect(PotionEffectType.POISON) ||
                !bukkitPlayer.hasPotionEffect(PotionEffectType.WITHER) ||
                !bukkitPlayer.isFrozen();
        PersistentDataContainer pdc = bukkitPlayer.getPersistentDataContainer();
        if (pdc.has(JKey.Ability)
                && pdc.get(JKey.Ability, PersistentDataType.TAG_CONTAINER).has(Mage.key)
                && pdc.get(JKey.Ability, PersistentDataType.TAG_CONTAINER).get(Mage.key, PersistentDataType.TAG_CONTAINER).has(Mage.Shoot.key)){
            Level nmsWorld = ((CraftWorld) bukkitPlayer.getWorld()).getHandle();
            Mage.Shoot shoot = new Mage.Shoot(bukkitPlayer,
                    new WitherSkull(EntityType.WITHER_SKULL, nmsWorld),
                    player_stats
            );
            shoot.setCritical(critical);
            Bukkit.getPluginManager().callEvent(shoot);
            e.getProjectile().remove();
            return;
        }
        DamageResult result = DamageResult.builder(player_stats)
                .type(DamageType.PROJECTILE)
                .build();
        if(e.getProjectile() instanceof Arrow arrow){
            boolean crit = player_stats.get(Stats.CRIT_CHANCE) >= new Random().nextInt(100) + 1;
            arrow.setCritical(critical && crit);
            arrow.setDamage(player_stats.get(Stats.DAMAGE));
            Vector velocity = arrow.getVelocity().multiply(Math.max(1, result.getForce()));
            arrow.setVelocity(velocity);
        }
    }
}
