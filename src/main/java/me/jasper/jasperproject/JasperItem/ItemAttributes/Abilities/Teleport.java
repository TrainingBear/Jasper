package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.JKey;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockIterator;

import java.util.List;

public class Teleport extends ItemAbility implements Listener {

    public Teleport(){register();}
    public Teleport(int range, float cooldown){
       this.setRange(range);
       this.setCooldown(cooldown);
       this.addLore(List.of(
                ChatColor.translateAlternateColorCodes('&',"&6Ability: &l&x&7&E&1&0&E&0Teleport &e(RIGHT CLICK)&r"),
                ChatColor.translateAlternateColorCodes('&',"&7Blablabla, this gona teleport"),
                ChatColor.translateAlternateColorCodes('&',"&7you to the direction that u"),
                ChatColor.translateAlternateColorCodes('&',"&7looking for up to &a" + range),
                ChatColor.translateAlternateColorCodes('&',"&ablocks&c!&r")

       ));

    }

    public Teleport(int range, float cooldown, Player player){
        this.setRange(range);
        this.setCooldown(cooldown);
        this.setPlayer(player);
    }

    //This Event Listener
    @EventHandler
    public void onTeleport(Teleport e){
        if(e.getPlayer().isSneaking() && ItemUtils.hasAbility(
                e.getPlayer().getInventory().getItemInMainHand(), Warper.getInstance().getKey())
        ) return;
        e.applyCooldown();
        if(e.isCancelled()) return;
        Player player = e.getPlayer();

        Location beforeTP = e.getPlayer().getLocation();
        Location target = getTargetBlock(player, e.getRange()).getLocation().add(0.5, 0, 0.5);
        target.setYaw(player.getLocation().getYaw());
        target.setPitch(player.getLocation().getPitch());

        Location afterTP = target.add(0 ,player.getLocation().getPitch() < 0 ? -1:0, 0);
        player.teleport(afterTP);
        player.setFallDistance(0);
        ItemUtils.playPSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f
                , (float) Math.min(1.7, 0.5f + (beforeTP.distance(afterTP) * 0.05f)));
        e.setCancelled(true);
    }


    //This gonna be my Event trigger
    @EventHandler
    public void Trigger(PlayerInteractEvent e){
        if((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) &&
                ItemUtils.hasAbility(e.getItem(), this.getKey())){

            PersistentDataContainer itemData = ItemUtils.getAbilityComp(e.getItem(), this.getKey());

            Bukkit.getPluginManager().callEvent(
                    new Teleport(
                            itemData.get(JKey.key_range, PersistentDataType.INTEGER),
                            itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                            e.getPlayer()
                    )
            );
            e.setCancelled(true);
        }
    }

    private Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block block = iter.next();
        Block lastBlock = block;
        while (iter.hasNext()) {
            block = iter.next();
            if (block.getType() == Material.AIR) lastBlock = block;
            else return lastBlock;
        }
        return lastBlock;
    }

}
