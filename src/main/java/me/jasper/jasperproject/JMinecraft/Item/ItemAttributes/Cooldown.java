package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes;

import lombok.Getter;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface Cooldown {
    Map<UUID, Long> cooldownMap = new HashMap<>();
    float getCooldown();
    boolean isVisible();
    Player getPlayer();

    default  <T extends Cooldown> boolean hasCooldown(T e){
        float cooldown = e.getCooldown();
        float current = cooldownMap.get(e.getPlayer().getUniqueId()) != null
                ? (System.currentTimeMillis() - cooldownMap.get(e.getPlayer().getUniqueId()) ) / 1000.0f
                : cooldown+1;

        if(current > cooldown) return false;
        if(e.isVisible()) {
            if (!isVisible()) return true;
            e.getPlayer().sendMessage(
                    MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait " + Util.round((cooldown - current), 1) + " seconds!</red>")
            );
        }
        return true;
    }
    default  <T extends Cooldown> boolean hasCooldown(T e,float customCD,boolean sendmessage){
        float current = cooldownMap.get(e.getPlayer().getUniqueId()) != null
                ? (System.currentTimeMillis() - cooldownMap.get(e.getPlayer().getUniqueId()) ) / 1000.0f
                : customCD +1;

        if(current > customCD) return false;
        if(sendmessage) {
            if (!e.isVisible()) return true;
            e.getPlayer().sendMessage(
                    MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait " + Util.round((customCD - current), 1) + " seconds!</red>")
            );
        }
        return true;
    }
    default  <T extends Cooldown> float getCdLeft(T e,int defaultIfNull){
        return cooldownMap.containsKey(e.getPlayer().getUniqueId())
                ? Util.round(e.getCooldown() - ((System.currentTimeMillis() - cooldownMap.get(e.getPlayer().getUniqueId()) ) / 1000.0f),1)
                : defaultIfNull;
    }
    default  <T extends Cooldown> void applyCooldown(T e) {
        applyCooldown(e, e.isVisible());
    }
    default  <T extends Cooldown> void applyCooldown(T e,boolean showCD) {
        float cooldown = e.getCooldown();
        if (cooldown <= 0) return;
        Player player = e.getPlayer();


        if (cooldownMap.containsKey(player.getUniqueId())) {
            float current = (System.currentTimeMillis() - cooldownMap.get(player.getUniqueId()) ) / 1000.0f;
            if(current >= cooldown){
                cooldownMap.remove(player.getUniqueId());
                cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
                return;
            }

            if(!showCD) return;
            player.sendMessage(
                    MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait "+ Util.round((cooldown - current),1)+" seconds!</red>")
            );

            return;
        }
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());

    }
    default  <T extends Cooldown> void applyCooldown(T e,float customCD,boolean showCD) {
        if (customCD <= 0) return;
        Player player = e.getPlayer();


        if (cooldownMap.containsKey(player.getUniqueId())) {
            float current = (System.currentTimeMillis() - cooldownMap.get(player.getUniqueId()) ) / 1000.0f;
            if(current >= customCD){
                cooldownMap.remove(player.getUniqueId());
                cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
                return;
            }

            if(!showCD) return;
            player.sendMessage(
                    MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait "+ Util.round((customCD - current),1)+" seconds!</red>")
            );

            return;
        }
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
