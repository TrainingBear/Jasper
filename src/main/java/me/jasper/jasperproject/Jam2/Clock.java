package me.jasper.jasperproject.Jam2;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Clock {
    // ini list variabel yg bakal di taro di yml
    static Location loc; // XYZ sama world
    static UUID uidArmorStand,
            uidDetik,
            uidJam;
    // UUID bisa string, serah

    private static BlockDisplay jarumDetik, jarumJam;
    BukkitRunnable task = new BukkitRunnable() {
        @Override
        public void run() {

        }
    };

    public static boolean startClock(Player pler) {

        return false;
    }

    public static void setup(Player pler) {
        if (uidArmorStand == null || loc == null) {
            // ========================= urusan armorstandnya ==========================
            ArmorStand armorstand = pler.getWorld().spawn(pler.getLocation(), ArmorStand.class);

            Location locArmorStand = armorstand.getLocation();
            locArmorStand.setYaw(roundYaw(locArmorStand.getYaw()));
            locArmorStand.setPitch(0);
            armorstand.teleport(locArmorStand);

            loc = locArmorStand;
            uidArmorStand = armorstand.getUniqueId();
            // ========================= urusan jarumnya ==========================
            if (uidDetik == null) {
                BlockDisplay detik = armorstand.getWorld().spawn(locArmorStand, BlockDisplay.class);
                detik.setBlock(Material.REDSTONE_BLOCK.createBlockData());
                
                jarumDetik = detik;
            }
        }
    }

    private static float roundYaw(float yaw) {
        return (float) Math.round(yaw / 90) * 90;
    }
}
