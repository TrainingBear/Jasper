package me.jasper.jasperproject.Util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;

public class TookTimer {
    public static void run(String message, Runnable runnable){
        long startTime = System.nanoTime();
        runnable.run();
        long endTime = System.nanoTime();
        String time = String.format("%.2f", (endTime - startTime) / 1_000_000.0);
        Bukkit.broadcast(Util.deserialize(message+" took <v> ms", Placeholder.parsed("v", time)).color(NamedTextColor.GREEN));
    }
}
