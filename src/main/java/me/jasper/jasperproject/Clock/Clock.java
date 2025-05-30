package me.jasper.jasperproject.Clock;

import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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
    static UUID uidArmorStand,
            uidMenit,
            uidJam;
    private static boolean isRunning = false;
    private static BukkitTask task;

    /**
     * Starting the clock to rotate based on world time
     * <p>
     * if the clock is already running, it'll do nothing
     * <p>
     * <hr>
     * Note : {@link #setup} first before calling this method
     */

    public static void start() {
        if (uidArmorStand == null || uidMenit == null || uidJam == null) {
            Util.debug("the variable is null, try remove then setup");
            return;
        } // NOTE BEYOND THIS TERM KANAN AND LEFT IS FROM ARMORSTAND VIEW
        if (isRunning) {
            Util.debug("the clock is already running");
            return;
        }
        final byte[][] xy = { // X, Y dari atas trs ke kanan trs ke bawah
                { 0, 3 }, { 1, 3 }, { 2, 2 },
                { 3, 1 }, { 2, 0 }, { 1, -1 },
                { 0, -1 }
        };
        final byte[][] mapping = { // ini dari arah trs ngadep kekanan
                { -1, 0 },
                { 0, -1 },
                { 1, 0 },
                { 0, 1 }
        };
        Location temp = Bukkit.getEntity(uidArmorStand).getLocation().add(0, .5f, 0).toBlockLocation();
        task = new BukkitRunnable() {
            final Location loc = temp.clone().add(temp.getDirection().normalize().multiply(-1));
            final byte[] xz = mapping[Math.floorMod(Math.round(loc.getYaw() / 90f), 4)];
            final World wrld = loc.getWorld();
            final Entity jrmJam = Bukkit.getEntity(uidJam);
            final Entity jrmMenit = Bukkit.getEntity(uidMenit);
            boolean flipflop = false;
            byte curIndex;
            Block blockBefore;
            Material bloBefMat;
            BlockData bloBefDat;

            @Override
            public void run() {

                final long time = wrld.getTime();

                // Hour hand: full cycle = 12000 ticks
                long tHour = time % 12000;
                float yawH, pitchH;
                byte indx = (byte) (time % 6000 * 0.001d);
                Location blokLocs = loc.clone();
                if (tHour <= 6000) {// kanan
                    yawH = loc.getYaw() + 90f;
                    pitchH = 90f - (tHour / 6000f) * 180f;
                    indx = (byte) (-indx + 6);
                    blokLocs = loc.clone().add(
                            (xz[0] > 0 ? xy[indx][0] : xz[0] < 0 ? -xy[indx][0] : 0),
                            xy[indx][1],
                            (xz[1] > 0 ? xy[indx][0] : xz[1] < 0 ? -xy[indx][0] : 0)//
                    );
                } else { // kanan
                    yawH = loc.getYaw() - 90f;
                    pitchH = -90f + ((tHour - 6000) / 6000f) * 180f;

                    blokLocs = loc.clone().add(
                            (xz[0] > 0 ? -xy[indx][0] : xz[0] < 0 ? xy[indx][0] : 0),
                            xy[indx][1],
                            (xz[1] > 0 ? -xy[indx][0] : xz[1] < 0 ? xy[indx][0] : 0)//
                    );
                }
                if (curIndex != indx) {
                    if (blockBefore != null) {
                        blockBefore.setType(bloBefMat);
                        blockBefore.setBlockData(bloBefDat);
                    }
                    blockBefore = blokLocs.getBlock();
                    bloBefMat = blockBefore.getType();
                    bloBefDat = blockBefore.getBlockData();

                    blockBefore.setType(Material.REDSTONE_LAMP);
                    BlockData data = blockBefore.getBlockData();
                    if (data instanceof Lightable light) {
                        light.setLit(true);
                        blockBefore.setBlockData(light);
                    }

                    curIndex = indx;
                }

                jrmJam.setRotation(yawH, pitchH);
                // =============================================================================
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
    }

    /**
     * Stopping the clock
     * <hr>
     * Note : {@link #start} first before calling this method otherwise it'll do
     * nothing
     */

    public static void stop() {
        if (isRunning) {
            task.cancel();
            isRunning = false;
        } else
            Util.debug("the clock is not running");
    }

    /**
     * Moving the clock to the player location
     * <p>
     * the yaw of clock will anchored/rounded to 4 direction (0, 90, 180, 270)
     * <p>
     * 
     * @param pler the {@link Player} who called this method
     *             <p>
     *             <hr>
     *             Note : {@link #setup} first before calling this method otherwise
     *             it'll do nothing</h>
     */

    public static void move(Player pler) {
        if (uidArmorStand == null || uidMenit == null || uidJam == null) {
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

            arStand.setInvisible(true);
            arStand.setMarker(true);
            arStand.setInvulnerable(true);

            Bukkit.getEntity(uidMenit).teleport(newloc);
            Bukkit.getEntity(uidJam).teleport(newloc);
        } catch (NullPointerException e) {
            pler.sendMessage("the entity may not exist, try remove then setup");
        }
    }

    /**
     * Spawning the clock to the player location
     * <p>
     * the yaw of clock will anchored/rounded to 4 direction (0, 90, 180, 270)
     * <p>
     * 
     * @param pler the {@link Player} who called this method
     *             <p>
     *             <hr>
     *             Note : if the previous clock already there, it'll do nothing</h>
     */
    public static void setup(Player pler) {
        if (uidArmorStand != null || uidJam != null || uidMenit != null) {
            pler.sendMessage("the clock is already there");
            return;
        }
        // ========================= urusan armorstandnya ==========================
        ArmorStand armorstand = pler.getWorld().spawn(pler.getLocation(), ArmorStand.class);
        armorstand.getEquipment().setHelmet(Util.getCustomSkull(
                "http://textures.minecraft.net/texture/88988523f2631de5cb01fdea38705b64eb906667d8d99bcb859a0a16db591a78"));
        // .....................redstone block ^
        Location locArmorStand = armorstand.getLocation();
        locArmorStand.setYaw(Util.roundYaw(locArmorStand.getYaw(), (byte) 4));
        locArmorStand.setPitch(0);
        armorstand.teleport(locArmorStand);
        locArmorStand.add(0, armorstand.getEyeHeight(), 0);

        uidArmorStand = armorstand.getUniqueId();
        JasperProject.getClockConfig().edit("Clock",
                c -> c.set("UUID_Base", uidArmorStand.toString()));
        // ========================= urusan jarumnya ==========================

        if (uidMenit == null) {
            BlockDisplay menit = armorstand.getWorld().spawn(locArmorStand, BlockDisplay.class);
            Lightable rdstoneTorch = (Lightable) Material.REDSTONE_TORCH.createBlockData();
            rdstoneTorch.setLit(false);
            menit.setBlock(rdstoneTorch);
            final Transformation transformasi = new Transformation(
                    new Vector3f(-.625f, -.625f, 0f),
                    new Quaternionf(.5f, .5f, .5f, .5f),
                    new Vector3f(1.25f, 2.15f, 1.25f), // scale, panjangny yg y
                    new Quaternionf(0f, 0f, 0f, 1f));
            menit.setTransformation(transformasi);

            JasperProject.getClockConfig().edit("Clock",
                    c -> c.set("UUID_Jarum_Menit", menit.getUniqueId().toString()));
            uidMenit = menit.getUniqueId();
        }
        if (uidJam == null) {
            BlockDisplay jam = armorstand.getWorld().spawn(locArmorStand, BlockDisplay.class);
            jam.setBlock(Material.REDSTONE_TORCH.createBlockData());
            final Transformation transformasi = new Transformation(
                    new Vector3f(-.625f, -.625f, 0f),
                    new Quaternionf(.5f, .5f, .5f, .5f),
                    new Vector3f(1.25f, 2.5f, 1.25f), // scale, panjangny yg y
                    new Quaternionf(0f, 0f, 0f, 1f));
            jam.setTransformation(transformasi);

            JasperProject.getClockConfig().edit("Clock",
                    c -> c.set("UUID_Jarum_Jam", jam.getUniqueId().toString()));
            uidJam = jam.getUniqueId();
        }
        armorstand.setInvisible(true);
        armorstand.setMarker(true);
        armorstand.setInvulnerable(true);
        return;
    }

    /**
     * Removing the clock
     * <p>
     */
    public static void remove() {
        String msgDebug = "Removing: ";
        if (isRunning)
            stop();
        if (uidArmorStand != null) {
            try {
                Bukkit.getEntity(uidArmorStand).remove();
            } catch (NullPointerException ignored) {
            } finally {
                uidArmorStand = null;
                JasperProject.getClockConfig().edit("Clock",
                        c -> c.set("UUID_Base", ""));
                msgDebug += "Armorstand ";
            }
        }
        if (uidJam != null) {
            try {
                Bukkit.getEntity(uidJam).remove();
            } catch (NullPointerException ignored) {
            } finally {
                uidJam = null;
                JasperProject.getClockConfig().edit("Clock",
                        c -> c.set("UUID_Jarum_Jam", ""));
                msgDebug += "jarum_Jam ";
            }
        }
        if (uidMenit != null) {
            try {
                Bukkit.getEntity(uidMenit).remove();
            } catch (NullPointerException ignored) {
            } finally {
                uidMenit = null;
                JasperProject.getClockConfig().edit("Clock",
                        c -> c.set("UUID_Jarum_Menit", ""));
                msgDebug += "jarum_Menit ";
            }
        }
        Util.debug(msgDebug);
    }

    /**
     * Assign needed variable {@link UUID} for the {@link Clock} using the .yml
     * before
     * <p>
     * recommended call only onEnable
     */
    public static void initialize() {
        String msgDebug = "Assigning clock: ";
        String uuidBase = JasperProject.getClockConfig().getConfig("Clock").getString("UUID_Base");
        if (uuidBase != null && !uuidBase.isEmpty()) {
            uidArmorStand = UUID.fromString(uuidBase);
            msgDebug += "Armorstand ";
        } else
            JasperProject.getClockConfig().forceEdit("Clock", c -> c.set("UUID_Base", ""));

        String uuidJam = JasperProject.getClockConfig().getConfig("Clock").getString("UUID_Jarum_Jam");
        if (uuidJam != null && !uuidJam.isEmpty()) {
            uidJam = UUID.fromString(uuidJam);
            msgDebug += "jarum_Jam ";
        } else
            JasperProject.getClockConfig().forceEdit("Clock", c -> c.set("UUID_Jarum_jam", ""));

        String uuidMenit = JasperProject.getClockConfig().getConfig("Clock").getString("UUID_Jarum_Menit");
        if (uuidMenit != null && !uuidMenit.isEmpty()) {
            uidMenit = UUID.fromString(uuidMenit);
            msgDebug += "jarum_Menit";
        } else
            JasperProject.getClockConfig().forceEdit("Clock", c -> c.set("UUID_Jarum_Menit", ""));

        Bukkit.getLogger().info(msgDebug);
    }
}
