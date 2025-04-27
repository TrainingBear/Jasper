package me.jasper.jasperproject.JMinecraft.Player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class JPlayer implements Listener {
    Map<Type, ItemStack> lastItems = new HashMap<>(Type.values().length);
    private UUID UUID;

    public JPlayer(){

    }
    public JPlayer(Player player){
        this.UUID = player.getUniqueId();
    }

    /**
     *
     * @return total damage amount
     */
    public int attack(){
        return 0;
    }

    public void setLastItems(Type type, ItemStack item){
        lastItems.put(type, item);
    }

    public @Nullable ItemStack getLastItems(Type type){
        return lastItems.get(type);
    }

//    @EventHandler
//    public void onSwapHotbar(PlayerSwapHandItemsEvent e){
//        Player player = e.getPlayer();
//        ItemStack mainHandItem = e.getMainHandItem();
//        ItemStack offHandItem = e.getOffHandItem();
//        Stats.apply(player, mainHandItem, Type.MAIN_HAND);
//        Stats.apply(player, offHandItem, Type.OFF_HAND);
//
//    }

    public enum AttackType{
        MELEE,
        ARROW,
        MAGIC,
        PROJECTILE
    }

    public enum Type{
        MAIN_HAND,
        OFF_HAND,
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }
}
