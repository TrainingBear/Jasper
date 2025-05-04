package me.jasper.jasperproject.Util.Listener;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Bash;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Heal;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Warper;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JMinecraft.Item.Util.Charge;
import me.jasper.jasperproject.JMinecraft.Item.Util.ItemManager;
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

        Charge.getCharge().remove(uuid);
    }
}
