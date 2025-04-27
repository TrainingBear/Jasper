package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BackStab extends ItemAbility {
    private static BackStab instance;
    @Getter private EquipmentSlot hand;
    public BackStab(){}
    public static BackStab getInstance(){
        if(instance == null) instance = new BackStab();
        return instance;
    }
    public BackStab(float cooldown,int distance){
        this.setCooldown(cooldown);
        this.setRange(distance);

    }
    public BackStab(float cooldown, int range, Player player, EquipmentSlot equipmentSlot){
        this.setCooldown(cooldown);
        this.setPlayer(player);
        this.setRange(range);
        this.hand = equipmentSlot;
    }
    @EventHandler
    public void trigger(PlayerInteractEvent e){
        if(!Util.hasAbility(e.getItem(), this.getKey())) return;
        if(TRIGGER.Interact.SHIFT_RIGHT_CLICK(e)){
            PersistentDataContainer itemData = Util.getAbilityComp(e.getItem(), this.getKey());
            Bukkit.getPluginManager().callEvent(
                    new BackStab(
                            itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                            itemData.get(JKey.key_range, PersistentDataType.INTEGER),
                            e.getPlayer(),e.getHand()
                    )
            );
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void action(BackStab e){
        Entity entity = e.getPlayer().getTargetEntity(e.getRange(),false);
        if(entity==null){
            e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red><b>INVALID!</b> There's no entity on sight"));
            return;
        }
        Location entityLoc = entity.getLocation().clone();
        entityLoc.setPitch(0);
        Location lokasiTujuan = entityLoc.clone().add(entityLoc.getDirection().normalize().multiply(-1));
        if(lokasiTujuan.toBlockLocation().getBlock().isSolid() || lokasiTujuan.clone().toBlockLocation().add(0,1,0).getBlock().isSolid()){
            e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red><b>INVALID!</b> Location is obstructed"));
            return;
        }
        applyCooldown(e,true);
        if(e.isCancelled()) return;
        Player player= e.getPlayer();

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE,0.75f,1.5f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL,0.75f,1.5f);
        player.getWorld().spawnParticle(
                Particle.DUST, player.getLocation().add(0,1,0), 60
                ,.3f,.4f,.3f,0
                ,new Particle.DustOptions(Color.fromRGB(60,60,60),2f), false);

        player.setFallDistance(0);
        Util.teleportPlayer(player,lokasiTujuan,false);
        player.attack(entity);
        player.swingHand(e.getHand());

        if(entity instanceof Player targetPlayer) targetPlayer.playSound(entity.getLocation(), Sound.ENTITY_GHAST_HURT,1f,0.85f);
        player.getWorld().spawnParticle(
                Particle.DUST, player.getLocation().add(0,1,0), 60
                ,.3f,.4f,.3f,0
                ,new Particle.DustOptions(Color.fromRGB(60,60,60),2f), false);
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                MiniMessage.miniMessage().deserialize("<!i><gold>Ability: <red>BackStab <b><yellow>(SNEAK+RIGHT CLICK)")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Teleport to behind looked Entity in")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>range of "+this.range+" blocks and <color:#aa7a7f>BackStab</color>'em")
        );
    }
}
