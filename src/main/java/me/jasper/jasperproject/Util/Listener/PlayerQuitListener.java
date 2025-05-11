package me.jasper.jasperproject.Util.Listener;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.*;
import me.jasper.jasperproject.JMinecraft.Item.Util.Charge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        Heal.getHealDuration().remove(uuid);
        Animator.getFirstPos().remove(uuid);
        Animator.getRegions().remove(uuid);
        Animator.getSecondPost().remove(uuid);
        Warper.getTarget().remove(uuid);
        Bash.getPowers().remove(uuid);

        Charge.clear(uuid);
        HoldEvent.getTask().remove(uuid);
        HoldEvent.getLastClick().remove(uuid);
    }
}
