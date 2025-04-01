package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.JKey;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Grappling_Hook extends ItemAbility implements Listener {
    @Getter private PlayerFishEvent fish;


    public Grappling_Hook(float cooldown) {
        this.setCooldown(cooldown);
        addLore(List.of(
                ChatColor.translateAlternateColorCodes('&',"&6Ability: &l&x&e&0&f&f&e&5Grappling &e(RIGHT CLICK)&r")
                ,ChatColor.translateAlternateColorCodes('&',"&7Pulls the player towards the hook")
                ,ChatColor.translateAlternateColorCodes('&',"&7if there is a block around the hook.")
        ));

    }

    public Grappling_Hook(float cooldown, PlayerFishEvent fish){
        setCooldown(cooldown);
        setPlayer(fish.getPlayer());
        this.fish = fish;
    }

    public Grappling_Hook(){
        register();
    }

    @EventHandler
    public void HookEvent(Grappling_Hook e){
        if(e.getFish().getState() == PlayerFishEvent.State.FISHING) e.getFish().getHook()
                .setVelocity(e.getFish().getPlayer().getLocation().getDirection().multiply(2.75));
        else if (isNearSolidBlock(e.getFish().getHook().getLocation()) ||e.getFish().getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            Player player = e.getFish().getPlayer();
            Location bobber = e.getFish().getHook().getLocation();

            applyCooldown(e,true);
            if(e.isCancelled()) {
                ItemUtils.playPSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1.7f);
                return;
            }
            //per-1 value = 20 blocks/s
            double y = bobber.getY() - player.getLocation().getY();
            double x = multiplierXZ(y);
            Vector velocity = bobber.toVector()
                    .subtract(player.getLocation().toVector())
                    .multiply(new Vector(x,multiplierY(y),x))
                    ;
            // 9 block = 0.16
            // 6 block = 0.2
            // 3 block = 0.3

            player.setVelocity(velocity);
            ItemUtils.playPSound(player, Sound.ENTITY_ARROW_SHOOT, 1f, 1.55f);
        }
    }

    private double multiplierXZ(double Y){
        //Multipliernya bakalan ke ambil 0.1 klo blocknya semakin tinggi
        return Math.max(0.09, -0.0233 * Y + 0.3697
        );
    }
    private double multiplierY(double Y){
        //Multipliernya bakalan ke ambil 0.1 klo blocknya semakin tinggi
        return Math.max(0.12, -0.0233 * Y + 0.365 //normal/default 0.3697
        );
    }

    @EventHandler
    public void onHook(PlayerFishEvent e) {
        if (!ItemUtils.hasAbility(e.getPlayer().getInventory().getItemInMainHand(), this.getKey()))  return;

        if (e.getState() == PlayerFishEvent.State.FISHING || e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY
            ||e.getState() == PlayerFishEvent.State.REEL_IN || e.getState() == PlayerFishEvent.State.IN_GROUND){
            PersistentDataContainer itemData = ItemUtils.getAbilityComp(e.getPlayer().getInventory().getItemInMainHand(), this.getKey());

            Bukkit.getPluginManager().callEvent(new Grappling_Hook(
                    itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                    e
            ));
        }
        else if(e.getState() == PlayerFishEvent.State.CAUGHT_FISH){
            Objects.requireNonNull(e.getCaught()).remove();
            e.setCancelled(true);
        }

    }
    private boolean isNearSolidBlock(Location loc) {
        if (loc.getBlock().getType().isSolid()) return true;
        for (BlockFace face : new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}) {
            if (loc.clone().add(face.getModX(), face.getModY(), face.getModZ())
                    .getBlock().getType().isSolid()) {
                return true;
            }
        }
        return false;
    }
}
