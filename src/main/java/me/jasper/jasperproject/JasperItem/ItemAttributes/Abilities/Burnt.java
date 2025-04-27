package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.TRIGGER;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Burnt extends ItemAbility {
    private static Burnt instance;
    public static Burnt getInstance(){
        if(instance == null) instance = new Burnt();
        return instance;
    }
    public Burnt(){}
    public Burnt(float cooldown){
        this.setCooldown(cooldown);
    }
    public Burnt(float cooldown, Player p){
        this.setCooldown(cooldown);
        this.setPlayer(p);
    }
    @EventHandler
    public void action(Burnt e){
        byte[][] direction = {//X,Z
                {1,0},
                {0,1},
                {-1,0},
                {0,-1}
        };
        new BukkitRunnable(){
            final Player p = e.getPlayer();
            final Location loc = p.getLocation();
            final byte[] dir = direction[(int) ((loc.getYaw() % 360 + 360) % 360)];
            byte frame = 0;
            @Override
            public void run(){
                if(!this.p.isOnline()
                        ||!ItemUtils.hasAbility(Bukkit.getPlayer(this.p.getUniqueId()).getInventory().getItemInMainHand(), e.getKey())
                        ||this.frame >= 4) cancel();
                if(dir[0]!=0) for(byte i = -1;i<=1;i++) loc.add(dir[0]+frame,0,i);
                else for(byte i = -1;i<=1;i++) loc.add(i,0,dir[0]+frame);
                frame++;
            }
        }.runTaskTimer(JasperProject.getPlugin(),0,10);
    }
    @EventHandler
    public void trigger(PlayerInteractEvent e){
        if(!ItemUtils.hasAbility(e.getItem(), this.getKey())) return;
        if(TRIGGER.Interact.RIGHT_CLICK(e)) {

            Bukkit.getPluginManager().callEvent(
                    new Burnt(
                            ItemUtils.getAbilityComp(e.getItem(), this.getKey()).get(JKey.key_cooldown, PersistentDataType.FLOAT)
                            ,e.getPlayer()
                    )
            );
        }
    }
    @Override
    protected List<Component> createLore() {
        return List.of();
    }
}
