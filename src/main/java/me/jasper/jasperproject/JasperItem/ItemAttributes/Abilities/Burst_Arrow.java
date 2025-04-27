package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Burst_Arrow extends ItemAbility {
    private static Burst_Arrow instance;
    @Getter private Arrow arrow;
    @Getter private float force;

    public static Burst_Arrow getInstance(){
        if(instance == null) instance = new Burst_Arrow();
        return instance;
    }
    public Burst_Arrow(){}

    public Burst_Arrow(float cooldown,int count){
        this.setCooldown(cooldown);
        this.setRange(count);

    }
    public Burst_Arrow(int count, float cooldown, Player p, Arrow ar,float force){
        this.setCooldown(cooldown);
        this.setPlayer(p);
        this.setRange(count);
        this.arrow = ar;
        this.force = force;
    }
    @EventHandler
    public void BurstListener(Burst_Arrow e){
        applyCooldown(e,false);
        if(e.isCancelled()) {
            e.getPlayer().sendActionBar(MiniMessage.miniMessage().deserialize("<red><b>COOLDOWN!</b> "+getCdLeft(e,0)+" seconds!"));
            return;
        }

        byte time = (byte) (30/e.getRange()); //1.5 second duration
        new BukkitRunnable() {
            private byte total=0;
            final Player pleryer = e.getPlayer();
            @Override
            public void run() {
                if(!this.pleryer.isOnline()
                        ||!Util.hasAbility(Bukkit.getPlayer(this.pleryer.getUniqueId()).getInventory().getItemInMainHand(), e.getKey())
                        ||this.total >= e.getRange()-1) cancel();
                Arrow panah = this.pleryer.launchProjectile(Arrow.class);
                this.pleryer.getWorld().playSound(this.pleryer.getLocation(), Sound.ENTITY_ARROW_SHOOT,SoundCategory.PLAYERS,1f,1.125f);
                panah.getPersistentDataContainer().set(JKey.removeWhenHit, PersistentDataType.BOOLEAN, true);
                panah.setVelocity(this.pleryer.getLocation().getDirection().multiply(e.getForce()));
                panah.setCritical(true);
                panah.setFireTicks(e.getArrow().getFireTicks());
                panah.setShooter(this.pleryer);
                panah.setTicksLived(200);
                panah.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
//                panah.setDamage();
                this.total++;
            }
        }.runTaskTimer(JasperProject.getPlugin(),time,time);

    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e){
        if(!Util.hasAbility(e.getBow(), this.getKey())) return;
        if(!(e.getProjectile() instanceof Arrow ar)) return;
        if(!(e.getEntity() instanceof Player pl)) return;
        if(!ar.isCritical()) return;


        PersistentDataContainer itemData = Util.getAbilityComp(e.getBow(), this.getKey());
        Bukkit.getPluginManager().callEvent(
            new Burst_Arrow(
                    itemData.get(JKey.key_range, PersistentDataType.INTEGER),
                    itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                    pl, ar, e.getForce()
            )
        );
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                MiniMessage.miniMessage().deserialize("<!i><gold>Ability: <color:#eeff00>Burst Arrow <b><yellow>(ON FULL DRAW)")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Shoot <color:#ba9e9e>"+range+"</color> arrows in rapid succession")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>when shoot at full power in 1.5 second")
        );
    }
}
