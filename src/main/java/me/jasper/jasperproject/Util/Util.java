package me.jasper.jasperproject.Util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;
import java.util.function.Consumer;

public final class Util {
    public static void teleportPlayer(Player p, Location loc,boolean invulWhenTP){
        p.teleport(loc);
        if(!invulWhenTP) p.setNoDamageTicks(0);
    }
    public static void teleportPlayer(Player p, Location loc, PlayerTeleportEvent.TeleportCause cause, boolean invulWhenTP){
        p.teleport(loc,cause);
        if(!invulWhenTP) p.setNoDamageTicks(0);
    }

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

    public static String escapeRegex(String name){
        return name.replaceAll("([\\\\\\[\\]{}()*+?^$|])", "");
    }
    public static String toRoman(int numbers){
        return numberToRoman(numbers, 0, new Stack<>());
    }

    private static final String[] romanUnique = {"I","V","X","L","C","D","M","V̅","X̅"};
    private static String numberToRoman(int numbers, int digit, Stack<String> memory){

        if(numbers<=0){
            StringBuilder builder = new StringBuilder();
            while (!memory.isEmpty()){
                builder.append(memory.pop());
            }
            return builder.toString();
        }

        int last_digit = numbers % 10;
        StringBuilder roman = new StringBuilder();
        if(last_digit==9){
            roman.append(romanUnique[digit]);
            roman.append(romanUnique[digit+2]);
            memory.add(roman.toString());
            return numberToRoman(numbers/10, digit+2, memory);
        }
        if(last_digit>=5){
            roman.append(romanUnique[digit+1]);
            roman.append(romanUnique[digit].repeat(last_digit - 5));
            memory.add(roman.toString());
            return numberToRoman(numbers/10, digit+2, memory);
        }
        if(last_digit==4){
            roman.append(romanUnique[digit]);
            roman.append(romanUnique[digit+1]);
            memory.add(roman.toString());
            return numberToRoman(numbers/10, digit+2, memory);
        }
        if(last_digit==0){
            return numberToRoman(numbers/10, digit+2, memory);
        }
        roman.append(romanUnique[digit].repeat(last_digit));
        memory.add(roman.toString());
        return numberToRoman(numbers/10, digit+2, memory);
    }

    public static boolean hasAbility(ItemStack item, @Nullable NamespacedKey key){
        if(key==null) return  item != null &&
                item.hasItemMeta() &&
                item.getItemMeta().getPersistentDataContainer().has(JKey.Ability);
        return  item != null &&
                item.hasItemMeta() &&
                item.getItemMeta().getPersistentDataContainer().has(JKey.Ability) &&
                item.getItemMeta().getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER).has(key);
    }

    public static void playPSound(Player player, Sound sound, float volume, float pitch){
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static PersistentDataContainer getAbilityComp(ItemStack item, NamespacedKey ItemAbility){
        return item.getItemMeta().getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER)
                .get(ItemAbility, PersistentDataType.TAG_CONTAINER);
    }
}
