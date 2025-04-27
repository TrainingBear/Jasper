package me.jasper.jasperproject.JMinecraft.Player;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerManager {
    @Getter private static final Map<UUID, JPlayer> onlinePlayers = new HashMap<>();
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
}
