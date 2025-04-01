package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.JKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class EndGateway extends ItemAbility {
    @Getter private Action ActionPlayer;
    int[] target = new int[3];

    public EndGateway(){
        register();
    }
    public EndGateway(float cooldown){
        setCooldown(cooldown);
        addLore(List.of(
                ChatColor.translateAlternateColorCodes('&',"&6Ability: ")
                ,ChatColor.translateAlternateColorCodes('&',"")
        ));

    }
    public EndGateway(float cooldown,PlayerInteractEvent e){
        setCooldown(cooldown);
        setPlayer(e.getPlayer());
        this.ActionPlayer = e.getAction();
    }

    @EventHandler
    public void EndGatewayListener(EndGateway e){
        if(e.getActionPlayer().equals(Action.LEFT_CLICK_AIR)||e.getActionPlayer().equals(Action.LEFT_CLICK_BLOCK)){
            Sign signGUI = (Sign) new Location(Bukkit.getWorld(e.getPlayer().getWorld().getUID()),-3710, 75, 1667).getBlock();
            if(signGUI == null){
                e.getPlayer().sendMessage(ChatColor.RED+"Failed to find the sign");
                return;
            }

            e.getPlayer().openSign(signGUI);
            e.getPlayer().sendMessage("Opening GUI if could...");

        }
    }

    @EventHandler
    public void trigger(PlayerInteractEvent e){
        if (!ItemUtils.hasAbility(e.getPlayer().getInventory().getItemInMainHand(), this.getKey()))  return;

        if((e.getAction().equals(Action.LEFT_CLICK_AIR)||e.getAction().equals(Action.LEFT_CLICK_BLOCK))
            || ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                ||e.getAction().equals(Action.RIGHT_CLICK_AIR)) && e.getPlayer().isSneaking())){

            PersistentDataContainer itemData = ItemUtils.getAbilityComp(e.getPlayer().getInventory().getItemInMainHand(), this.getKey());

            Bukkit.getPluginManager().callEvent(new EndGateway(
                    itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                    e
            ));
        }

    }
}
