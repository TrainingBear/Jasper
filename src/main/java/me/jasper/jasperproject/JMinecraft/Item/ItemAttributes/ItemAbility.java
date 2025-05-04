package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Item.Util.ItemManager;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class ItemAbility extends Event implements Cancellable, Listener, Cloneable, Cooldown {
    @Getter public final NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), this.getClass().getSimpleName());;
    private static final HandlerList HANDLER_LIST = new HandlerList();public static HandlerList getHandlerList() {return HANDLER_LIST;}@Override public @NotNull HandlerList getHandlers() {return HANDLER_LIST;}

    protected boolean cancelled = false;
    @Setter @Getter protected int range;
    @Setter @Getter protected int damage;
    @Setter @Getter protected int abilityCost;
    @Setter private float cooldown;
    private boolean visible;
    protected Player player;

    protected abstract List<Component> createLore();
    public List<Component> getLore(){
        List<Component> lore = new ArrayList<>(createLore());
        if(cooldown > 0) lore.add(MiniMessage.miniMessage().deserialize("<!italic><dark_gray>Cooldown: <green>"+Util.round(cooldown,1)+" seconds"));
        return lore;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public Object clone() {
        try{
            return super.clone();
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
        for (ItemAbility ability : ItemManager.getAbilities()) {
            if(pdc.has(ability.getKey())){
                PersistentDataContainer container = pdc.get(ability.getKey(), PersistentDataType.TAG_CONTAINER);
                ItemAbility clone = (ItemAbility) ability.clone();
                clone.range = container.get(JKey.key_range, PersistentDataType.INTEGER);
                clone.damage = container.get(JKey.key_damage, PersistentDataType.INTEGER);
                clone.cooldown = container.get(JKey.key_cooldown, PersistentDataType.FLOAT);
                clone.abilityCost = container.get(JKey.key_abilityCost, PersistentDataType.INTEGER);
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

    public static List<Component> toLore(List<ItemAbility> abilities){
        List<Component> lore = new ArrayList<>();
        for (ItemAbility ability : abilities) {
            lore.add(Util.deserialize("<reset>"));
            lore.addAll(ability.getLore());
        }
        return lore;
    }

    public static @NotNull PersistentDataContainer toPDC(@NotNull PersistentDataAdapterContext context, @NotNull List<ItemAbility> abilities){
        PersistentDataContainer abilities_name = context.newPersistentDataContainer();
        for (ItemAbility ability : abilities) {
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

    @Override
    public float getCooldown() {
        return cooldown;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
