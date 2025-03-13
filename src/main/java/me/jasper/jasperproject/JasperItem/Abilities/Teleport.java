package me.jasper.jasperproject.JasperItem.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAbility;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Teleport extends ItemAbility implements Listener, Keyed {
    private final static HandlerList handlerList = new HandlerList();


    //The Event
    @Getter private int range;
    @Getter private Player player;
    private final NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), "Teleport");
    public Teleport(int range, Player player) {
        this.range = range;
        this.player = player;
    }

    //This Event Listener
    @EventHandler
    public void onTeleport(Teleport e){
        e.getPlayer().teleport(e.getPlayer().getTargetBlockExact(e.getRange()).getLocation());
    }


    //This gonna be my Event trigger
    @EventHandler
    public void Trigger(PlayerInteractEvent e){
        if((e.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) &&
                e.getItem().getItemMeta().getPersistentDataContainer().get(
                        new NamespacedKey(JasperProject.getPlugin(),"item_ability"), PersistentDataType.TAG_CONTAINER
                ).has(this.key)
                ){

        }
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }
}
