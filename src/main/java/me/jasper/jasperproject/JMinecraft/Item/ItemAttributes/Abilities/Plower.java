package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;

public class Plower extends ItemAbility {
    private static Plower instance;
    @Getter
    private Location locat;

    public static Plower getInstance() {
        if (instance == null)
            instance = new Plower();
        return instance;
    }

    public Plower() {
    }

    public Plower(float cooldown) {
        this.setCooldown(cooldown);
    }

    public Plower(float cooldown, Player p, Location locat) {
        this.setCooldown(cooldown);
        this.player = p;
        locat.setYaw(this.getPlayer().getYaw());
        this.locat = locat;
    }

    @EventHandler
    public void trigger(Plower e) {
        Material type = e.getLocat().getBlock().getType();
        if (!(type == Material.DIRT || type == Material.GRASS_BLOCK
                || type == Material.DIRT_PATH || type == Material.PODZOL))
            return;
        applyCooldown(e, false);
        if (e.isCancelled()) {
            e.getPlayer().sendActionBar(Component.text("COOLDOWN! " + getCdLeft(e, 0) + " seconds!"));
            return;
        }

        byte[][] direction = { // X,Z
                { 0, 1 },
                { -1, 0 },
                { 0, -1 },
                { 1, 0 }
        };
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
                    if (!createTile(this.clickLoc.clone()
                            .add((this.dir[0] != 0 ? dx : i), 0, (dir[1] != 0 ? dz : i))) && ++fail > 1)
                        cancel();
                }

                if (this.frame > 0)
                    this.clickLoc.getWorld().playSound(this.clickLoc.clone().add(dx, 0, dz), Sound.ITEM_HOE_TILL,
                            0.5f, 1.15f);

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
            Bukkit.getPluginManager().callEvent(new Plower(
                    Util.getAbilityComp(e.getItem(), this.getKey()).get(JKey.key_cooldown, PersistentDataType.FLOAT),
                    e.getPlayer(), e.getClickedBlock().getLocation()));
        }
    }

    @Override
    protected List<Component> createLore() {
        return List.of();
    }

    private boolean createTile(Location loc) {
        Block diatas = loc.clone().add(0, 1, 0).getBlock();
        Block exact = loc.getBlock();
        World world = loc.getWorld();

        if (exact.getType().isSolid()) {
            if (isHoeAble(exact)) {
                if (diatas.getType().isEmpty()) {
                    exact.setType(Material.FARMLAND);
                    world.spawnParticle(
                            Particle.HAPPY_VILLAGER, exact.getLocation().add(.5f, 1f , .5f), 4, .3f, 0f, .3f);
                    return exact.getType() == Material.FARMLAND;
                } else if (isHoeAble(diatas)) {
                    if (loc.clone().add(0, 2, 0).getBlock().getType().isEmpty()) {
                        diatas.setType(Material.FARMLAND);
                        world.spawnParticle(
                                Particle.HAPPY_VILLAGER, diatas.getLocation().add(.5f, 1f, .5f), 4, .3f, 0f, .3f);
                        return diatas.getType() == Material.FARMLAND;
                    }
                }
            }
        } else {
            if (isHoeAble(diatas)) {
                if (loc.clone().add(0, 2, 0).getBlock().getType().isEmpty()) {
                    diatas.setType(Material.FARMLAND);
                    world.spawnParticle(
                            Particle.HAPPY_VILLAGER, diatas.getLocation().add(.5f, 1f, .5f), 4, .3f, 0f, .3f);
                    return diatas.getType() == Material.FARMLAND;
                }
            }
            Block dibawah = loc.clone().add(0, -1, 0).getBlock();
            if (isHoeAble(dibawah)) {
                dibawah.setType(Material.FARMLAND);
                world.spawnParticle(
                        Particle.HAPPY_VILLAGER, dibawah.getLocation().add(.5f, 1f, .5f), 4, .3f, 0f, .3f);
                return dibawah.getType() == Material.FARMLAND;
            }
        }
        return false;
    }

    private boolean isHoeAble(Block block) {
        Material type = block.getType();
        if (type == Material.DIRT || type == Material.GRASS_BLOCK || type == Material.FARMLAND
                || type == Material.DIRT_PATH || type == Material.PODZOL)
            return true;
        return false;
    }
}