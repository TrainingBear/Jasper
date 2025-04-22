package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.TRIGGER;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Heal extends ItemAbility {
    private static Heal instance;
    public Heal(){}

    public static Heal getInstance(){
        if (instance == null) instance = new Heal();
        return instance;
    }

    public Heal(int howMuchHealth, float cooldown){
        this.setRange(howMuchHealth);
        this.setCooldown(cooldown);
        this.addLore(List.of(
                MiniMessage.miniMessage().deserialize("<!i><gold>Ability: <green>Heal <b><yellow>(RIGHT CLICK)")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Healing you <color:#749e70>"+range+"</color> healths")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>for 7 seconds duration")
        ));
    }

    @EventHandler
    public void Trigger(PlayerInteractEvent e){
        if(!ItemUtils.hasAbility(e.getItem(), this.getKey())) return;
        if(TRIGGER.Interact.RIGHT_CLICK(e)){

            PersistentDataContainer itemData = ItemUtils.getAbilityComp(e.getItem(), this.getKey());

            Bukkit.getPluginManager().callEvent(
                    new Heal(
                            itemData.get(JKey.key_range, PersistentDataType.INTEGER),
                            itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                            e.getPlayer()
                    )
            );
            e.setCancelled(true);
        }
    }
    public Heal(int range, float cooldown, Player player){
        this.setRange(range);
        this.setCooldown(cooldown);
        this.setPlayer(player);
    }
    @EventHandler
    public void action(Heal e){
        applyCooldown(e,true);
        if(e.isCancelled()) return;

        new BukkitRunnable(){
            byte duration = (byte) (e.getCooldown()-1f);//<-- downtime
            @Override
            public void run() {
                if(duration <= 0 ) cancel();
                e.getPlayer().setHealth(Math.min(
                        e.getPlayer().getHealth()+e.getRange()
                        , e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()
                ));
                e.getPlayer().sendActionBar(MiniMessage.miniMessage().deserialize("<green>Heal "+"▍".repeat(duration)));
                e.getPlayer().getWorld().spawnParticle(
                        Particle.HEART , e.getPlayer().getLocation().add(0,0.8f,0) ,2,0.26f,0.35f,0.26f
                );
                duration--;
            }
        }.runTaskTimer(JasperProject.getPlugin(),0,20);
    }
}
