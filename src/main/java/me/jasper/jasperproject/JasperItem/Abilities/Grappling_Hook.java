package me.jasper.jasperproject.JasperItem.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAbility;
import me.jasper.jasperproject.JasperItem.ItemUtils;
import me.jasper.jasperproject.JasperItem.JKey;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class Grappling_Hook extends ItemAbility implements Listener {
    @Getter private final static HandlerList handlerList = new HandlerList();

    NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), "Grapling");
    @Getter private float cooldown;
    @Getter private List<String> lore;
    @Getter private PlayerFishEvent fish;
    private boolean cancelled = false;


    public Grappling_Hook(float cooldown) {
        this.cooldown = cooldown;
        lore = List.of(
                ChatColor.translateAlternateColorCodes('&',"&6Ability: &l&x&e&0&f&f&e&5Grappling &e(RIGHT CLICK)&r"),
                ChatColor.translateAlternateColorCodes('&',"&7Lorem"),
                ChatColor.DARK_GRAY +"cooldown: "+ChatColor.GREEN+cooldown+" seconds"
        );
    }

    public Grappling_Hook(float cooldown, PlayerFishEvent fish){
        this.cooldown = cooldown;
        this.fish = fish;
    }

    public Grappling_Hook(){

    }

    @EventHandler
    public void HookEvent(Grappling_Hook e){
        if (e.getFish().getState() == PlayerFishEvent.State.IN_GROUND||isNearSolidBlock(e.getFish().getHook().getLocation())) {
            applyCooldown(e.getFish().getPlayer(), e.getCooldown(), e);
            if(e.isCancelled()) {
                ItemUtils.playPSound(e.getFish().getPlayer(), Sound.ENTITY_ITEM_BREAK, 1f, 1.7f);
                return;
            }
            Vector velocity = e.getFish().getHook().getLocation().toVector()
                .subtract(e.getFish().getPlayer().getLocation().toVector()) // .normalize() cmn kek nyimpen arahnya doang, bukan kekuatanny
                .setY(
                    (e.getFish().getHook().getLocation().getY()+0.2)
                    - e.getFish().getPlayer().getLocation().getY()
                );
            e.getFish().getPlayer().setVelocity(velocity);
            ItemUtils.playPSound(e.getFish().getPlayer(), Sound.ENTITY_ARROW_SHOOT, 1f, 1.55f);
        }
    }

    @EventHandler
    public void onHook(PlayerFishEvent e) {
        if (
                !e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
                !e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(JKey.Ability) &&
                !e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER).has(this.key)
        )  return;
        if (e.getState() == PlayerFishEvent.State.REEL_IN||e.getState() == PlayerFishEvent.State.IN_GROUND){
            PersistentDataContainer itemData = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(JKey.Ability, PersistentDataType.TAG_CONTAINER)
                    .get(key, PersistentDataType.TAG_CONTAINER);
            Bukkit.getPluginManager().callEvent(new Grappling_Hook(
                    itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                    e
            ));
        }

    }
    private boolean isNearSolidBlock(Location loc) {
        if (loc.getBlock().getType().isSolid()) return true;
        for (BlockFace face : new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}) {
            Location adjacent = loc.clone().add(face.getModX(), face.getModY(), face.getModZ());
            if (adjacent.getBlock().getType().isSolid()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PersistentDataContainer getStatsContainer(PersistentDataContainer TagContainer){

        PersistentDataContainer stats = TagContainer.getAdapterContext().newPersistentDataContainer();
        stats.set(JKey.key_cooldown, PersistentDataType.FLOAT, this.cooldown);

        return stats;
    }


    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public float getCooldown() {
        return this.cooldown;
    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    public List<String> getDescription() {
        return this.lore;
    }

    @Override
    public int getAbilityCost() {
        return 0;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
