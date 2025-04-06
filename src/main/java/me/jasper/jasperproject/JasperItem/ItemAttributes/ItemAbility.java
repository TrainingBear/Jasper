package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JasperItem.Util.ItemHandler;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.JKey;
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class ItemAbility extends Event implements Cancellable, Listener {
    //ini key nya gw buat berdasarkan nama class ability yang dibuat, jadi jangan di ubah2.
    // yaudah tinggal sebut nama classnya trs get keynya
    @Getter protected final NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), this.getClass().getSimpleName());

    protected final HashMap<UUID, Long> cooldowns = new HashMap<>();
    @Getter protected final static HandlerList handlerList = new HandlerList();
    protected boolean cancelled = false;
    @Getter protected boolean showCooldown = true;
    @Setter @Getter protected Player player;

    @Setter @Getter protected int range;
    @Setter @Getter protected int damage;
    @Setter @Getter protected int abilityCost;
    @Setter @Getter protected float cooldown;
    @Getter
    protected List<String> lore = new ArrayList<>();

    protected void addLore(List<String> E){
        lore.addAll(E);
        if(showCooldown){
            lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + cooldown + " seconds");
        }
    }

    public ItemAbility setShowCooldown(boolean showCooldown) {
        this.showCooldown = showCooldown;
        if(this.showCooldown) {
            lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + cooldown + " seconds");
        } else {
            lore.remove(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + cooldown + " seconds");
        }
        return this;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public void register() {
        ItemHandler.getAbilities().add(this);
    }

    public PersistentDataContainer getStatsContainer(PersistentDataContainer TagContainer){
        PersistentDataContainer stats = TagContainer.getAdapterContext().newPersistentDataContainer();
        if(range > 0){
            stats.set(JKey.key_range, PersistentDataType.INTEGER, range);
        }if(damage > 0){
            stats.set(JKey.key_damage, PersistentDataType.INTEGER, damage);
        }if(cooldown > 0){
            stats.set(JKey.key_cooldown, PersistentDataType.FLOAT, cooldown);
        }if(abilityCost > 0){
            stats.set(JKey.key_abilityCost, PersistentDataType.INTEGER, abilityCost);
        }
        return stats;
    }
    protected <T extends ItemAbility> boolean hasCooldown(T e, boolean showcooldown){
        Player player = e.getPlayer();
        float cooldown = e.getCooldown();

        float current = cooldowns.get(player.getUniqueId()) != null ?
                (System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) ) / 1000.0f : cooldown+1;

        if(current > cooldown) return false;
        if(!showcooldown) return true;
        player.sendMessage(
                MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait "+round((cooldown - current),1)+" seconds!</red>")
        );
        return true;
    }
    protected <T extends ItemAbility> void applyCooldown(T e, boolean showCooldown) {//SEMENTARA parameter boolean
        float cooldown = e.getCooldown();
        if (cooldown <= 0) return;
        Player player = e.getPlayer();
        this.showCooldown = showCooldown;

        if (cooldowns.containsKey(player.getUniqueId())) {
            float current = (System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) ) / 1000.0f;
            if(current >= cooldown){
                cooldowns.remove(player.getUniqueId());
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                return;
            }

            e.setCancelled(true);
            if(!showCooldown) return;
            player.sendMessage(
                    MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> Please wait "+round((cooldown - current),1)+" seconds!</red>")
            );

            return;
        }

        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

    }

    public static float round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value).setScale(places,RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static List<ItemAbility> convertFrom(ItemStack item){
        List<ItemAbility> abilities = new ArrayList<>();
        if(!ItemUtils.hasAbility(item)) return abilities;
        for (ItemAbility ability : ItemHandler.getAbilities()) {
            if(!ItemUtils.hasAbility(item, ability.getKey())){
                abilities.add(ability);
            }
        }
        return abilities;
    }

}
