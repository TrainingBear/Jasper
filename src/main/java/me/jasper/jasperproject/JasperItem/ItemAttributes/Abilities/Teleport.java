package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.TRIGGER;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockIterator;

import java.util.List;

public class Teleport extends ItemAbility{
    private static Teleport instance;

    Teleport() {
    }

    public static Teleport getInstance(){
        if(instance == null) instance = new Teleport();
        return instance;
    }

    public Teleport(int range, float cooldown){
       this.setRange(range);
       this.setCooldown(cooldown);
       this.addLore(List.of(
               MiniMessage.miniMessage().deserialize("<!i><gold>Ability: <color:#7e10e0><b>Teleport <yellow>(RIGHT CLICK)")
               ,MiniMessage.miniMessage().deserialize("<!i><gray>Teleporting you to the")
               ,MiniMessage.miniMessage().deserialize("<!i><gray>unobstructed location with")
               ,MiniMessage.miniMessage().deserialize("<!i><gray>range of "+range)

       ));

    }

    public Teleport(int range, float cooldown, Player player){
        this.setRange(range);
        this.setCooldown(cooldown);
        this.setPlayer(player);
    }



    @EventHandler
    public void action(Teleport e) {
        if(e.getPlayer().isSneaking() && ItemUtils.hasAbility(
                e.getPlayer().getInventory().getItemInMainHand(), Warper.getInstance().getKey())
        ) return;
        applyCooldown(e);
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
    }


    //This gonna be my Event trigger
    @EventHandler
    public void Trigger(PlayerInteractEvent e){
        if(!ItemUtils.hasAbility(e.getItem(), this.getKey())) return;
        if(TRIGGER.Interact.RIGHT_CLICK(e)){

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
