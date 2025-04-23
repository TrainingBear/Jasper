package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.TRIGGER;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        addLore(List.of(
                MiniMessage.miniMessage().deserialize("<!i><gold>Ability: <red>BackStab <b><yellow>(SNEAK+RIGHT CLICK)")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Teleport to behind looked Entity in")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>range of "+range+" blocks and <color:#aa7a7f>BackStab</color>'em")
        ));
    }
    public BackStab(float cooldown, int range, Player player, EquipmentSlot equipmentSlot){
        this.setCooldown(cooldown);
        this.setPlayer(player);
        this.setRange(range);
        this.hand = equipmentSlot;
    }
    @EventHandler
    public void trigger(PlayerInteractEvent e){
        if(!ItemUtils.hasAbility(e.getItem(), this.getKey())) return;
        if(TRIGGER.Interact.SHIFT_RIGHT_CLICK(e)){
            PersistentDataContainer itemData = ItemUtils.getAbilityComp(e.getItem(), this.getKey());
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
        if(lokasiTujuan.getBlock().isSolid() || lokasiTujuan.clone().add(0,1,0).getBlock().isSolid()){
            e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red><b>INVALID!</b> Location is obstructed"));
            return;
        }
        e.getPlayer().teleport(lokasiTujuan);
        e.getPlayer().attack(entity);
        e.getPlayer().swingHand(e.getHand());

    }
}
