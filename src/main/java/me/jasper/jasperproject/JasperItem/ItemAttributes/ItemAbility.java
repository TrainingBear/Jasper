package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JasperItem.Util.ItemManager;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public abstract class ItemAbility extends Event implements Cancellable, Listener {
    @Getter public final Map<UUID, Long> cooldownMap = new HashMap<>();;
    @Getter public final NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), this.getClass().getSimpleName());;

    private static final HandlerList HANDLER_LIST = new HandlerList();public static HandlerList getHandlerList() {return HANDLER_LIST;}@Override public @NotNull HandlerList getHandlers() {return HANDLER_LIST;}

    protected boolean cancelled = false;
    @Getter protected boolean showCooldown = true;
    @Setter @Getter protected Player player;

    @Setter @Getter protected int range;
    @Setter @Getter protected int damage;
    @Setter @Getter protected int abilityCost;
    @Setter @Getter protected float cooldown;
    @Getter
    protected List<Component> lore = new ArrayList<>();

    protected void addLore(List<Component> E){
        lore.addAll(E);
        if(showCooldown){
            lore.add(MiniMessage.miniMessage().deserialize("<!italic><dark_gray>Cooldown: <green>"+cooldown+" seconds"));
        }
    }

    public void setShowCooldown(boolean showCooldown) {
        this.showCooldown = showCooldown;
        if(this.showCooldown) lore.add(MiniMessage.miniMessage().deserialize("<!italic><dark_gray>Cooldown: <green>"+cooldown+" seconds"));
        else lore.remove(MiniMessage.miniMessage().deserialize("<!italic><dark_gray>Cooldown: <green>"+cooldown+" seconds"));
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public PersistentDataContainer getStatsContainer(PersistentDataContainer TagContainer){
        PersistentDataContainer stats = TagContainer.getAdapterContext().newPersistentDataContainer();
            stats.set(JKey.key_range, PersistentDataType.INTEGER, range);
            stats.set(JKey.key_damage, PersistentDataType.INTEGER, damage);
            stats.set(JKey.key_cooldown, PersistentDataType.FLOAT, cooldown);
            stats.set(JKey.key_abilityCost, PersistentDataType.INTEGER, abilityCost);
        return stats;
    }

    protected <T extends ItemAbility> boolean hasCooldown(T e){
        float cooldown = e.getCooldown();
        float current = ItemAbility.this.cooldownMap.get(player.getUniqueId()) != null ?
                (System.currentTimeMillis() - ItemAbility.this.cooldownMap.get(player.getUniqueId()) ) / 1000.0f : cooldown+1;

        if(current > cooldown) return false;
        if(!isShowCooldown()) return true;
        Player player = e.getPlayer();
        player.sendMessage(
                MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait "+round((cooldown - current),1)+" seconds!</red>")
        );
        return true;
    }
    protected <T extends ItemAbility> void applyCooldown(T e,boolean showCD) {
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

            e.setCancelled(true);
            if(!showCD) return;
            player.sendMessage(
                    MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait "+round((cooldown - current),1)+" seconds!</red>")
            );

            return;
        }
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());

    }

    public static float round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value).setScale(places,RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static List<ItemAbility> convertFrom(ItemStack item){
        List<ItemAbility> abilities = new ArrayList<>();
        if(!ItemUtils.hasAbility(item)) return abilities;
        for (ItemAbility ability : ItemManager.getInstance().getAbilities()) {
            if(!ItemUtils.hasAbility(item, ability.getKey())){
                abilities.add(ability);
            }
        }
        return abilities;
    }

}
