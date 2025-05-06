package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public Burst_Arrow(float cooldown, int count){
        this.setCooldown(cooldown);
        this.setRange(count);

    }
    public Burst_Arrow(int count, float cooldown, Player p, @Nullable Arrow ar,float force){
        this.setCooldown(cooldown);
        this.player = (p);
        this.setRange(count);
        this.arrow = ar;
        this.force = force;
    }
    @EventHandler
    public void BurstListener(Burst_Arrow e){
        if(e.isCancelled()) return;
        Player player = e.getPlayer();
        if(hasCooldown(e)) {
            player.sendActionBar(Util.deserialize("<red><b>COOLDOWN!</b> "+getCdLeft(e,0)+" seconds!"));
            return;
        }

        byte time = (byte) (30/e.getRange()); //1.5 second duration
        new BukkitRunnable() {
            private byte total=0;
            @Override public void run() {
                if(!player.isOnline()
                        ||!Util.hasAbility(player.getInventory().getItemInMainHand(), e.getKey())
                        ||this.total >= e.getRange()-1) cancel();

                ItemStack arrow = getArrow(player);
                if(arrow==null) {
                    player.sendMessage("out of arrow!");
                    this.cancel();
                    return;
                }
                EntityShootBowEvent shootEvent = new EntityShootBowEvent(player, player.getActiveItem(), arrow, e.getArrow(), e.getForce());
                Bukkit.getPluginManager().callEvent(shootEvent);
                this.total++;
            }
        }.runTaskTimer(JasperProject.getPlugin(),time,time);
//        applyCooldown(e);
    }

    @EventHandler
    public void onShoot(PlayerInteractEvent e){
        if(!TRIGGER.Interact.SNEAK_LEFT_CLICK(e)) return;
        ItemStack activeItem = e.getPlayer().getActiveItem();
        if(!Util.hasAbility(activeItem, this.getKey())) return;

        PersistentDataContainer itemData = Util.getAbilityComp(activeItem, this.getKey());
        Bukkit.getPluginManager().callEvent(
            new Burst_Arrow(
                    itemData.get(JKey.key_range, PersistentDataType.INTEGER),
                    itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                    e.getPlayer(), null, 3f
            )
        );
    }

    private @Nullable ItemStack getArrow(@NotNull Player player){
        PlayerInventory inventory = player.getInventory();
        ItemStack offHand = inventory.getItemInOffHand();
        if(isArrow(offHand)) return offHand;
        ItemStack mainHand = inventory.getItemInMainHand();
        if(isArrow(mainHand)) return mainHand;

        for (@Nullable ItemStack item : inventory.getContents()) {
            if(item==null) continue;
            Bukkit.broadcastMessage(item.getType().name());
            if(isArrow(item)){
                return item;
            }
        }
        return null;
    }

    private boolean isArrow(ItemStack itemStack){
        return
        itemStack.getType().equals(Material.ARROW)||
        itemStack.getType().equals(Material.SPECTRAL_ARROW) ||
        itemStack.getType().equals(Material.TIPPED_ARROW);
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
