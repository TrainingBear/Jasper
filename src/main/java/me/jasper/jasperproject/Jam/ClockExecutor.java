package me.jasper.jasperproject.Jam;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.scheduler.BukkitRunnable;

public class ClockExecutor extends BukkitRunnable {
    static JasperProject plugin;

     public ClockExecutor(JasperProject plugin) {
        ClockExecutor.plugin = plugin;
    }

    @Override
    public void run() {
         new ClockUtility(plugin).jarumMenit();
         new ClockUtility(plugin).jarumJam();
         new ClockUtility(plugin).perhitunganWaktu();
    }

    public static class Detak extends BukkitRunnable {
        @Override
        public void run() {
            new ClockUtility(plugin).detakJam();
        }
    }

}
