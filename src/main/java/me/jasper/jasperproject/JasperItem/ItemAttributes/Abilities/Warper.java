package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.JKey;
import me.jasper.jasperproject.Util.SignGUI;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.IntStream;

public class Warper extends ItemAbility{
    @Getter private Action ActionPlayer;
    HashMap<UUID, int[]> target = new HashMap<>();

    public Warper(){
        register();
    }
    public Warper(float cooldown){
        setCooldown(cooldown);
        addLore(List.of(
                ChatColor.translateAlternateColorCodes('&',"&6Ability: ")
                ,ChatColor.translateAlternateColorCodes('&',"")
        ));

    }
    public Warper(float cooldown, PlayerInteractEvent e){
        setCooldown(cooldown);
        setPlayer(e.getPlayer());
        this.ActionPlayer = e.getAction();
    }

    @EventHandler
    public void WarperListener(Warper e){
        if((e.getActionPlayer().equals(Action.LEFT_CLICK_AIR)||e.getActionPlayer().equals(Action.LEFT_CLICK_BLOCK))&&e.getPlayer().isSneaking()){
//            e.getPlayer().sendBlockChange(e.getPlayer().getEyeLocation().getBlock().getLocation(), e.getPlayer().getEyeLocation().getBlock().getBlockData());
            String[] builtInText= {
                    ""
                    ,"^^^^^^^^^^^^"
                    ,"Input coordinates"
                    ,"x y z / x, y, z"
            };
            SignGUI.open(
                    e.getPlayer(), builtInText, Material.WARPED_SIGN, (p, lines, signLoc, previousBlockData) -> {
                        p.sendBlockChange(signLoc, previousBlockData);//turn back to normal
                        if(!IntStream.rangeClosed(1, 3).allMatch(i -> lines[i].equals(builtInText[i]))){
                            p.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"ABILITY"+ChatColor.RED+" edit di baris pertama saja");
                            return;
                        }
                        String[] parts = lines[0].trim().split("\\s*,\\s*|\\s+");
                        if(parts.length==1 && parts[0].isEmpty()) return;
                        else if (parts.length != 3) {
                            e.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"ABILITY"+ChatColor.RED+" koordinat tidak valid!");
                            return;
                        }
                        try {
                            int[] result = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();

                            for (int num : result) {
                                if (num > 12) { //limit
                                    e.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"ABILITY"+ChatColor.RED+" angka melebihi limit dari 12!");
                                    return;
                                }
                            }
                            target.put(p.getUniqueId(), result.clone());

                        } catch (NumberFormatException exc) {
                            e.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"ABILITY"+ChatColor.RED+" koordinat tidak valid!");
                        }
                    });
            e.setCancelled(true);
        }
        else if((e.getActionPlayer().equals(Action.RIGHT_CLICK_BLOCK)||e.getActionPlayer().equals(Action.RIGHT_CLICK_AIR))&&e.getPlayer().isSneaking()){
            if(!target.containsKey(e.getPlayer().getUniqueId())){
                e.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"ABILITY"+ChatColor.RED+" masukkan koordinat (klik kiri + sneak)");
                ItemUtils.playPSound(e.getPlayer(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE,1,0.5f);
                return;
            }

            applyCooldown(e, true);
            if(e.isCancelled()) return;
            int[] targetlocation = target.get(e.getPlayer().getUniqueId());

            e.getPlayer().teleport(e.getPlayer().getLocation().add(targetlocation[0],targetlocation[1],targetlocation[2]));
            ItemUtils.playPSound(e.getPlayer(), Sound.ENTITY_ENDER_DRAGON_HURT,1,0.5f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void trigger(PlayerInteractEvent e){
        if (!ItemUtils.hasAbility(e.getPlayer().getInventory().getItemInMainHand(), this.getKey()))  return;

        if(((e.getAction().equals(Action.LEFT_CLICK_AIR)||e.getAction().equals(Action.LEFT_CLICK_BLOCK))&&e.getPlayer().isSneaking())
            ||
            ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) ||e.getAction().equals(Action.RIGHT_CLICK_AIR)) && e.getPlayer().isSneaking())){

            PersistentDataContainer itemData = ItemUtils.getAbilityComp(e.getPlayer().getInventory().getItemInMainHand(), this.getKey());

            Bukkit.getPluginManager().callEvent(new Warper(
                    itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                    e
            ));
        }

    }
}
