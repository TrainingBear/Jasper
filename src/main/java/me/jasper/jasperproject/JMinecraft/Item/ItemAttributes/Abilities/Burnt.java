package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Burnt extends ItemAbility {
    private static Burnt instance;
    @Getter
    private Location locat;

    public static Burnt getInstance() {
        if (instance == null)
            instance = new Burnt();
        return instance;
    }

    public Burnt() {
    }

    public Burnt(float cooldown) {
        this.setCooldown(cooldown);
    }

    public Burnt(float cooldown, Player p, Location loc) {
        this.setCooldown(cooldown);
        this.player = (p);
        loc.setYaw(this.getPlayer().getYaw());
        this.locat = loc;
    }

    @EventHandler
    public void action(Burnt e) {
        applyCooldown(e, false);
        if (e.isCancelled()) {
            e.getPlayer().sendActionBar(Util.deserialize("<red><b>COOLDOWN!</b> " + getCdLeft(e, 0) + " seconds!"));
            return;
        }
        byte[][] direction = { // X,Z
                { 0, 1 },
                { -1, 0 },
                { 0, -1 },
                { 1, 0 }
        };
        e.getPlayer().getWorld().playSound(
                e.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, SoundCategory.PLAYERS, 0.8f, 1.56f);
        e.getPlayer().getWorld().playSound(
                e.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.PLAYERS, 0.8f, 1.85f);
        new BukkitRunnable() {
            private final Player p = e.getPlayer();
            private final Location clickLoc = e.getLocat();
            private final byte[] dir = direction[Math.floorMod(Math.round(clickLoc.getYaw() / 90f), 4)];
            private byte frame = 0;

            @Override
            public void run() {
                if (!p.isOnline() || !Util.hasAbility(p.getInventory().getItemInMainHand(), e.getKey())
                        || this.frame >= 3) { // frame >= #-1, # is length
                    cancel();
                }

                byte step = (byte) (Math.abs(this.frame) - 1);
                byte dx = this.dir[0] != 0 ? (byte) (this.dir[0] + (this.dir[0] > 0 ? step : -step)) : 0;
                byte dz = this.dir[1] != 0 ? (byte) (this.dir[1] + (this.dir[1] > 0 ? step : -step)) : 0;

                byte fail = 0;
                for (int i = -1; i <= 1; i++) {
                    if (!createFire(this.clickLoc.clone()
                            .add((this.dir[0] != 0 ? dx : i), 0, (dir[1] != 0 ? dz : i))) && ++fail > 1)
                        cancel();
                }

                if (this.frame > 0)
                    this.clickLoc.getWorld().playSound(this.clickLoc.clone().add(dx, 0, dz), Sound.ENTITY_BLAZE_SHOOT,
                            0.5f, 1.68f);

                this.frame++;
            }
        }.runTaskTimer(JasperProject.getPlugin(), 3, 3);
    }

    @EventHandler
    public void trigger(PlayerInteractEvent e) {
        if (!Util.hasAbility(e.getItem(), this.getKey()))
            return;
        if (TRIGGER.Interact.SNEAK_RIGHT_CLICK(e)) {
            if (e.getClickedBlock() == null)
                return;

            Bukkit.getPluginManager().callEvent(
                    new Burnt(
                            Util.getAbilityComp(e.getItem(), this.getKey()).get(JKey.key_cooldown,
                                    PersistentDataType.FLOAT),
                            e.getPlayer(), e.getClickedBlock().getLocation()));
        }
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                Util.deserialize("<!i><gold>Ability: <red>Burnt <b><yellow>(SNEAK RIGHT CLICK)"),
                Util.deserialize("<!i><gray>Ignite <color:#aa7070>fire</color> trail 3×4 where you ignite"),
                Util.deserialize("<!i><gray>based on your direction"));
    }

    private boolean createFire(Location loc) {
        Block diAtas = loc.clone().add(0, 1, 0).getBlock();
        Block diAtas2 = loc.clone().add(0, 2, 0).getBlock();

        if (loc.getBlock().getType().isSolid()) {
            if (isEmptyOrFire(diAtas)) {
                diAtas.setType(Material.FIRE);
                return diAtas.getType() == Material.FIRE;
            }
            if (isEmptyOrFire(diAtas2)) {
                diAtas2.setType(Material.FIRE);
                return diAtas2.getType() == Material.FIRE;
            }
        } else if (diAtas.getType().isSolid() && isEmptyOrFire(diAtas2)) {
            diAtas2.setType(Material.FIRE);
            return diAtas2.getType() == Material.FIRE;
        } else if (loc.clone().add(0, -1, 0).getBlock().getType().isSolid() && isEmptyOrFire(loc.getBlock())) {
            loc.getBlock().setType(Material.FIRE);
            return loc.getBlock().getType() == Material.FIRE;

        } else if (diAtas.getType().isSolid() && isEmptyOrFire(diAtas2)) {
            diAtas2.setType(Material.FIRE);
            return diAtas2.getType() == Material.FIRE;
        }

        return false;
    }

    private boolean isEmptyOrFire(Block block) {
        return block.getType().isEmpty() || block.getType() == Material.FIRE;
    }
}
