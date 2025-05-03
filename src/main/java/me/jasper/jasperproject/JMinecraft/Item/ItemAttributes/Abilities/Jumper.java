package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Jumper extends ItemAbility {
    private static Jumper instance;
    public Jumper(){}
    public static Jumper getInstance(){
        if(instance==null) instance = new Jumper();
        return instance;
    }
    public Jumper(int blockHigh, float cooldown){
        this.setRange(blockHigh);
        this.setCooldown(cooldown);

    }
    @EventHandler
    public void Trigger(PlayerInteractEvent e){
        if(!Util.hasAbility(e.getItem(), this.getKey())) return;
        if(TRIGGER.Interact.RIGHT_CLICK(e)){
            PersistentDataContainer itemData = Util.getAbilityComp(e.getItem(), this.getKey());
            Bukkit.getPluginManager().callEvent(
                    new Jumper(
                            itemData.get(JKey.key_range, PersistentDataType.INTEGER),
                            itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                            e.getPlayer()
                    )
            );
            e.setCancelled(true);
        }
    }
    public Jumper(int blockHigh, float cooldown, Player player){
        this.setRange(blockHigh);
        this.setCooldown(cooldown);
        this.setPlayer(player);
    }
    @EventHandler
    public void action(Jumper e){
        applyCooldown(e,true);
        if(e.isCancelled()) return;
        Location playerLoc = e.getPlayer().getLocation();

        e.getPlayer().setVelocity(e.getPlayer().getVelocity().setY(Math.sqrt(0.16 * e.getRange()+.35f)));

        e.getPlayer().getWorld().playSound(
                e.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH
                , SoundCategory.PLAYERS,0.85f,1.425f
        );

        e.getPlayer().getWorld().spawnParticle(
                Particle.FIREWORK,e.getPlayer().getLocation().add(0,.3f,0)
                ,50,.3f,.3f,.3f,0
        );

        byte point = 10;
        for(short i = 0;i < point ; i++){
            float angle = (float) (2 * Math.PI * i / point);
            e.getPlayer().getWorld().spawnParticle(
                    Particle.FIREWORK
                    ,playerLoc.clone().add((Math.cos(angle) * .5f),.25f,(Math.sin(angle) * .5f))
                    ,0 ,0,1,0, ThreadLocalRandom.current().nextFloat(.125f,.36f)
            );
        }
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                MiniMessage.miniMessage().deserialize("<!i><gold>Ability: <color:#bad2ff>Jumper <b><yellow>(RIGHT CLICK)")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Launching you up "+range+" blocks")
        );
    }
}
