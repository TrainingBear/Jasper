package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JasperItem.Util.ItemManager;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public abstract class ItemAbility extends Event implements Cancellable, Listener, Cloneable {
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



    protected <T extends ItemAbility> boolean hasCooldown(T e,boolean sendmessage){
        float cooldown = e.getCooldown();
        float current = ItemAbility.this.cooldownMap.get(player.getUniqueId()) != null ?
                (System.currentTimeMillis() - ItemAbility.this.cooldownMap.get(player.getUniqueId()) ) / 1000.0f : cooldown+1;

        if(current > cooldown) return false;
        if(sendmessage) {
            if (!isShowCooldown()) return true;
            e.getPlayer().sendMessage(
                    MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait " + Util.round((cooldown - current), 1) + " seconds!</red>")
            );
        }
        return true;
    }
    protected <T extends ItemAbility> float getCdLeft(T e,int defaultIfNull){
        return cooldownMap.containsKey(e.getPlayer().getUniqueId())
                ? Util.round(e.getCooldown() - ((System.currentTimeMillis() - cooldownMap.get(e.getPlayer().getUniqueId()) ) / 1000.0f),1)
                : defaultIfNull;
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
                    MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait "+ Util.round((cooldown - current),1)+" seconds!</red>")
            );

            return;
        }
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());

    }

    public Object clone() {
       try{
           ItemAbility clone = (ItemAbility) super.clone();
           clone.lore = List.copyOf(this.lore);
           return clone;
       } catch (CloneNotSupportedException e) {
           throw new RuntimeException(e);
       }
    }

    public static @NotNull List<ItemAbility> convertFrom(@NotNull ItemStack item){
        List<ItemAbility> abilities = new ArrayList<>();
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if(!pdc.has(JKey.Ability)) {
            return abilities;
        }
        pdc = pdc.get(JKey.Ability, PersistentDataType.TAG_CONTAINER);
        for (ItemAbility ability : ItemManager.getInstance().getAbilities()) {
            if(pdc.has(ability.getKey())){
                PersistentDataContainer container = pdc.get(ability.getKey(), PersistentDataType.TAG_CONTAINER);
                ItemAbility clone = (ItemAbility) ability.clone();
                clone.range = container.get(JKey.key_range, PersistentDataType.INTEGER);
                clone.damage = container.get(JKey.key_damage, PersistentDataType.INTEGER);
                clone.cooldown = container.get(JKey.key_cooldown, PersistentDataType.FLOAT);
                clone.abilityCost = container.get(JKey.key_abilityCost, PersistentDataType.INTEGER);
                clone.lore = ability.lore;
                abilities.add(clone);
            }
        }
        return abilities;
    }
    public PersistentDataContainer toPDC(@NotNull PersistentDataAdapterContext context){
        PersistentDataContainer stats = context.newPersistentDataContainer();
        stats.set(JKey.key_range, PersistentDataType.INTEGER, range);
        stats.set(JKey.key_damage, PersistentDataType.INTEGER, damage);
        stats.set(JKey.key_cooldown, PersistentDataType.FLOAT, cooldown);
        stats.set(JKey.key_abilityCost, PersistentDataType.INTEGER, abilityCost);
        return stats;
    }

    public static @NotNull PersistentDataContainer toPDC(@NotNull PersistentDataAdapterContext context, @NotNull List<ItemAbility> abilities, @Nullable List<Component> lore){
        PersistentDataContainer abilities_name = context.newPersistentDataContainer();
        for (ItemAbility ability : abilities) {
            if(lore!=null) lore.addAll(ability.getLore());
            PersistentDataContainer stats = abilities_name.getAdapterContext().newPersistentDataContainer();
            stats.set(JKey.key_range, PersistentDataType.INTEGER, ability.range);
            stats.set(JKey.key_damage, PersistentDataType.INTEGER, ability.damage);
            stats.set(JKey.key_cooldown, PersistentDataType.FLOAT, ability.cooldown);
            stats.set(JKey.key_abilityCost, PersistentDataType.INTEGER, ability.abilityCost);
            abilities_name.set(
                    ability.getKey(),
                    PersistentDataType.TAG_CONTAINER, stats
            );
        }
        return abilities_name;
    }

}
