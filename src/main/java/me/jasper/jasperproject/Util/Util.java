package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.bukkit.Bukkit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Stack;
import java.util.UUID;
import java.util.function.Consumer;

public final class Util {
    public static void debug(Object... message){
        StringBuilder messages = new StringBuilder();
        for (Object o : message) {
            messages.append(o.toString());
        }
        String format = "[JDebug] ";
        Bukkit.broadcast(deserialize(format).color(NamedTextColor.BLUE).append(deserialize(messages.toString()).color(NamedTextColor.GOLD)));
//        JasperProject.getPlugin().getLogger().info(format+message);
    }

    public static String satuan(double health){
        if(health >= 1_000_000_000f) return Util.round (health/1_000_000_000 ,1) + "B"; //milyar/billion
        else if(health >= 1_000_000) return Util.round ( health/1_000_000_000 ,1) +"M";//juta
        else if(health >= 1_000) return Util.round ( health/1_000,1)+"k"; //seribu
        else return Util.round(health, 0)+"";
    }

    public static void teleportEntity(LivingEntity e, Location loc, boolean invulWhenTP) {
        e.teleport(loc);
        if(!invulWhenTP) e.setNoDamageTicks(0);
    }
    public static void teleportEntity(LivingEntity e, Location loc, PlayerTeleportEvent.TeleportCause cause, boolean invulWhenTP){
        e.teleport(loc,cause);
        if(!invulWhenTP) e.setNoDamageTicks(0);
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

    public static String timer(long time_milisecond) {
        long second = time_milisecond / 1000;
        long minute = second / 60;
        second = second - (minute) * 60;
        long hour = minute / 60;
        minute = minute - (hour) * 60;
        return hour > 0 ? hour + ":" + minute + ":" + second
                : minute > 0 ? minute + ":" + second : String.valueOf(second);
    }

    /**
     * get {@link ItemStack} {@link Material#PLAYER_HEAD} head with custom skin
     *
     * @param skinLink URL of the skin, using {@link String} of
     *                 http://textures.minecraft.net/texture/.....
     * @return {@link ItemStack} player head with custom skin
     *
     */
    public static ItemStack getCustomSkull(String skinLink) {
        com.destroystokyo.paper.profile.PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        try {
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(URI.create(skinLink).toURL());
            profile.setTextures(textures);
        } catch (MalformedURLException ignored) {
            return null;
        }
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setPlayerProfile(profile);

        head.setItemMeta(meta);
        return head;
    }
    /** rounding yaw entity of 360 degree to the nearest facing/point
     *
     * @param yaw yaw of entity
     * @param amount amount of facing/point
     * @return nearest facing/point
     */
    public static float roundYaw(float yaw, byte amount) {
        float anchor = 360f / amount;
        return (float) (Math.round(yaw / anchor) * anchor);
    }
    /**
     * get target {@link LivingEntity} from range inputed entity sight
     *
     * @param suatuEntitas the entity to get target
     * @param range the range
     * @param ignoreBlock ignore block?
     * @return {@link LivingEntity} target {@link LivingEntity} or null if not found or <b>not</b> a {@link LivingEntity}
     */
    public static LivingEntity getTargetEntity(LivingEntity suatuEntitas, int range, boolean ignoreBlock) {
        if (suatuEntitas.getTargetEntity(range, ignoreBlock) instanceof LivingEntity entity) {
            return entity;
        }
        return null;
    }
}
