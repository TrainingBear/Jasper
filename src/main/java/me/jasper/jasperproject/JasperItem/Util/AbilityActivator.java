package me.jasper.jasperproject.JasperItem.Util;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AbilityActivator implements Listener {
    enum Type{//, serah namanya
        RIGHT_CLICK,
        SHIFT_RIGHT_CLICK,
        RIGHT_CLICK_BLOCK,
        SHIFT_RIGHT_CLICK_BLOCK,
        RIGHT_CLICK_AIR,
        SHIFT_RIGHT_CLICK_AIR,

        LEFT_CLICK,
        SHIFT_LEFT_CLICK,
        LEFT_CLICK_BLOCK,
        SHIFT_LEFT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        SHIFT_LEFT_CLICK_AIR; // done
    }

    public static final Map<Type, List<ItemAbility>> key = new HashMap<>();

    public static <T extends ItemAbility> void addEvent(Type type, T e){// bro copas
        key.computeIfAbsent(type, k -> new ArrayList<>()).add(e); // rada ragu gw soal List<> nya
       }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        ///         RIGHT CLICK
        if((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            for (ItemAbility itemAbility : key.get(Type.RIGHT_CLICK)) {
                if(!ItemUtils.hasAbility(e.getItem(), itemAbility.getKey())) continue;
                Bukkit.getPluginManager().callEvent(itemAbility);
            }
        }
        ///             RIGHT_CLICK_AIR
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){//yddh push ke git
            //cuma tinggal copas, ganti keybind
            for (ItemAbility itemAbility : key.get(Type.RIGHT_CLICK_AIR)) {
                if(!ItemUtils.hasAbility(e.getItem(), itemAbility.getKey())) continue;
                Bukkit.getPluginManager().callEvent(itemAbility);
            }
        }
        ///             RIGHT_CLICK_BLOCK
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            for (ItemAbility itemAbility : key.get(Type.RIGHT_CLICK_BLOCK)) {
                if(!ItemUtils.hasAbility(e.getItem(), itemAbility.getKey())) continue;
                Bukkit.getPluginManager().callEvent(itemAbility);
            }
        }
    }
}






