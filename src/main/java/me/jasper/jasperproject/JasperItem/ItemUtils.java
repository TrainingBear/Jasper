package me.jasper.jasperproject.JasperItem;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ItemUtils {
    public static void playPSound(Player player, Sound sound, float volume, float pitch){
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
