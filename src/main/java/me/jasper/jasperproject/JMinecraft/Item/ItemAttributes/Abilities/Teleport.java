package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
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
    }

    public Teleport(int range, float cooldown, Player player){
        this.setRange(range);
        this.setCooldown(cooldown);
        this.setPlayer(player);
    }



    @EventHandler
    public void action(Teleport e) {
        if(e.getPlayer().isSneaking() && Util.hasAbility(
                e.getPlayer().getInventory().getItemInMainHand(), Warper.getInstance().getKey())
        ) return;
        applyCooldown(e,false);
        if(e.isCancelled()) return;

        Player player = e.getPlayer();

        Location beforeTP = e.getPlayer().getLocation();
        Location target = getTargetBlock(player, e.getRange()).getLocation().add(0.5, 0, 0.5);
        target.setYaw(player.getLocation().getYaw());
        target.setPitch(player.getLocation().getPitch());

        Location afterTP = target.add(0 ,player.getLocation().getPitch() < 0 ? -1:0, 0);
        Util.teleportPlayer(player,afterTP,false);
        player.setFallDistance(0);
        Util.playPSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f
                , (float) Math.min(1.7, 0.5f + (beforeTP.distance(afterTP) * 0.05f)));
    }


    //This gonna be my Event trigger
    @EventHandler
    public void Trigger(PlayerInteractEvent e){
        if(!Util.hasAbility(e.getItem(), this.getKey())) return;
        if(TRIGGER.Interact.RIGHT_CLICK(e)){

            PersistentDataContainer itemData = Util.getAbilityComp(e.getItem(), this.getKey());

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


    @Override
    protected List<Component> createLore() {
        return new ArrayList<>(List.of(
                Util.deserialize("<!i><gold>Ability: <color:#7e10e0>Teleport <b><yellow>(RIGHT CLICK)"),
                Util.deserialize("<!i><gray>Teleporting you to the"),
                Util.deserialize("<!i><gray>unobstructed location with"),
                Util.deserialize("<!i><gray>range of " + this.range)

        ));
    }
}
