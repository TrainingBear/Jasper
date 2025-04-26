package me.jasper.jasperproject.Util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

public final class Util {

    public static Component deserialize(String message, TagResolver... tags){
        return MiniMessage.miniMessage().deserialize(message, tags);
    }

    public static void scanInventory(Player player, Consumer<ItemStack> consumer){
        for (ItemStack content : player.getInventory().getStorageContents()) {
            consumer.accept(content);
        }
    }
    public static float round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
