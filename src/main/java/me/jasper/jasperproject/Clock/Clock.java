package me.jasper.jasperproject.Clock;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.Util;

public class Clock {
    // ini list variabel yg bakal di taro di yml
    static Location loc; // XYZ sama world
    static UUID uidArmorStand,
            uidMenit,
            uidJam;
    // UUID bisa string, serah
    private static boolean isRunning = false;
    private static BukkitTask task;

    public static void start() {
        if (uidArmorStand == null || uidMenit == null || uidJam == null || loc == null) {
            Bukkit.broadcast(Util.deserialize("the variable is null, try remove then setup"));
            return;
        }
        if (!isRunning) {
            task = new BukkitRunnable() {
                World wrld = loc.getWorld();
                boolean flipflop = false;
                Entity jrmJam = Bukkit.getEntity(uidJam);
                Entity jrmMenit = Bukkit.getEntity(uidMenit);

                @Override
                public void run() {
                    long time = wrld.getTime() % 24000;

                    // Hour hand: full cycle = 12000 ticks
                    long tHour = time % 12000;
                    float yawH, pitchH;
                    if (tHour <= 6000) {
                        yawH = loc.getYaw() + 90f;
                        pitchH = 90f - (tHour / 6000f) * 180f;
                    } else {
                        yawH = loc.getYaw() - 90f;
                        pitchH = -90f + ((tHour - 6000) / 6000f) * 180f;
                    }
                    jrmJam.setRotation(yawH, pitchH);

                    // Minute hand: cycle = 1000 ticks
                    long tMin = time % 1000;
                    float yawM, pitchM;
                    if (tMin <= 500) {
                        yawM = loc.getYaw() - 90f;
                        pitchM = -90f + (tMin / 500f) * 180f;
                    } else {
                        yawM = loc.getYaw() + 90f;
                        pitchM = 90f - ((tMin - 500) / 500f) * 180f;
                    }
                    jrmMenit.setRotation(yawM, pitchM);

                    if (flipflop) {
                        wrld.playSound(loc, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON, 4.8f, 2f);
                        flipflop = false;
                    } else {
                        wrld.playSound(loc, Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 4.8f, 2f);
                        flipflop = true;
                    }
                }
            }.runTaskTimer(JasperProject.getPlugin(), 0, 20);
            isRunning = true;
        } else
            Bukkit.broadcast(Util.deserialize("the clock is already running"));
    }

    public static void stop() {
        if (isRunning) {
            task.cancel();
            isRunning = false;
        } else
            Bukkit.broadcast(Util.deserialize("the clock is not running"));
    }

    public static void move(Player pler) {
        if (uidArmorStand == null || uidMenit == null || uidJam == null || loc == null) {
            pler.sendMessage("the variable is null, try remove then setup");
            return;
        }
        Location newloc = pler.getLocation();
        newloc.setYaw(Util.roundYaw(newloc.getYaw(), (byte) 4));
        newloc.setPitch(0);
        try {
            ArmorStand arStand = (ArmorStand) Bukkit.getEntity(uidArmorStand);
            arStand.teleport(newloc);

            arStand.setInvisible(false);
            arStand.setMarker(false);
            arStand.setInvulnerable(false);

            newloc.add(0, arStand.getEyeHeight(), 0);
            loc = newloc.clone();

            arStand.setInvisible(true);
            arStand.setMarker(true);
            arStand.setInvulnerable(true);

            Bukkit.getEntity(uidMenit).teleport(newloc);
            Bukkit.getEntity(uidJam).teleport(newloc);
        } catch (NullPointerException e) {
            pler.sendMessage("the entity may not exist, try remove then setup");
        }
    }

    public static void setup(Player pler) {
        if (uidArmorStand == null || loc == null) {
            // ========================= urusan armorstandnya ==========================
            ArmorStand armorstand = pler.getWorld().spawn(pler.getLocation(), ArmorStand.class);
            armorstand.getEquipment().setHelmet(Util.getCustomSkull(
                    "http://textures.minecraft.net/texture/88988523f2631de5cb01fdea38705b64eb906667d8d99bcb859a0a16db591a78"));

            Location locArmorStand = armorstand.getLocation();
            locArmorStand.setYaw(Util.roundYaw(locArmorStand.getYaw(), (byte) 4));
            locArmorStand.setPitch(0);
            armorstand.teleport(locArmorStand);
            locArmorStand.add(0, armorstand.getEyeHeight(), 0);

            loc = locArmorStand.clone();
            uidArmorStand = armorstand.getUniqueId();
            // ========================= urusan jarumnya ==========================

            if (uidMenit == null) {
                BlockDisplay menit = armorstand.getWorld().spawn(locArmorStand, BlockDisplay.class);
                Lightable rdstoneTorch = (Lightable) Material.REDSTONE_TORCH.createBlockData();
                rdstoneTorch.setLit(false);
                menit.setBlock(rdstoneTorch);
                final Transformation transformasi = new Transformation(
                        new Vector3f(-.625f, -.625f, 0f),
                        new Quaternionf(.5f, .5f, .5f, .5f),
                        new Vector3f(1.25f, 2.15f, 1.25f), // scale
                        new Quaternionf(0f, 0f, 0f, 1f));
                menit.setTransformation(transformasi);
                uidMenit = menit.getUniqueId();
            }
            if (uidJam == null) {
                BlockDisplay jam = armorstand.getWorld().spawn(locArmorStand, BlockDisplay.class);
                jam.setBlock(Material.REDSTONE_TORCH.createBlockData());
                final Transformation transformasi = new Transformation(
                        new Vector3f(-.625f, -.625f, 0f),
                        new Quaternionf(.5f, .5f, .5f, .5f),
                        new Vector3f(1.25f, 2.5f, 1.25f), // scale
                        new Quaternionf(0f, 0f, 0f, 1f));
                jam.setTransformation(transformasi);
                uidJam = jam.getUniqueId();
            }
            armorstand.setInvisible(true);
            armorstand.setMarker(true);
            armorstand.setInvulnerable(true);
            return;
        }
        pler.sendMessage("the clock is already there");
    }

    public static void remove() {
        String msgDebug = "Removing: ";
        if (isRunning)
            stop();
        if (uidArmorStand != null) {
            try {
                Bukkit.getEntity(uidArmorStand).remove();
            } catch (NullPointerException ignored) {
            } finally {
                loc = null;
                uidArmorStand = null;
                msgDebug += "Armorstand ";
            }
        }
        if (uidJam != null) {
            try {
                Bukkit.getEntity(uidJam).remove();
            } catch (NullPointerException ignored) {
            } finally {
                uidJam = null;
                msgDebug += "jarum_Jam ";
            }
        }
        if (uidMenit != null) {
            try {
                Bukkit.getEntity(uidMenit).remove();
            } catch (NullPointerException ignored) {
            } finally {
                uidMenit = null;
                msgDebug += "jarum_Menit ";
            }
        }
        Bukkit.broadcast(Util.deserialize(msgDebug));
    }

}
