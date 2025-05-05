package me.jasper.jasperproject.JMinecraft.Item.Util;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.JasperEvent;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Util that useful for holding mechanic
 * <hr>
 * How to use: Bukkit.getPluginManager().callEvent(new {@link Charge}(player,byte maxtime, ChargeAction));
 * @Override the {@link ChargAction}
 */
public class Charge extends JasperEvent {
    public Charge() { }

    private boolean cancelled;
    @Getter private static Map<UUID, chargeLogic> charge = new HashMap<>();
    @Getter private static Map<UUID, Long> lastClick = new HashMap<>();
    @Getter private static Map<UUID, BukkitRunnable> tasks = new HashMap<>();
    @Getter private UUID player;

    public static void clear(Player player){
        clear(player.getUniqueId());
    }
    public static void clear(UUID uuid){
        charge.remove(uuid);
        lastClick.remove(uuid);
        tasks.remove(uuid);
    }

    /** Use this method when something iterate useful for logic that uses hold mechanic
     * <hr>
     *  since Bukkit API/listener doesn't provide to check player is currently holding or not.
     *  Instead, when player hold it trigger in a frequency
     * @param p Player who holding
     * @param maxTimeInSec Max duration of player allow to hold <b>Disclaimer: when the hold duration exceed
     *                     the maxTime, will immediately run the action, still charging when it still triggers/calls</b>
     * @param action what actions within holding
     */
    public Charge(@NotNull Player p, int maxTimeInSec, ChargAction action){
        this.player = p.getUniqueId();
        charge.putIfAbsent(p.getUniqueId(), new chargeLogic((byte) maxTimeInSec, action));
    }
    @EventHandler
    public void run(@NotNull Charge e){
        if(e.isCancelled()) return;
        UUID uuid = e.getPlayer();
        long last = lastClick.getOrDefault(uuid, lastClick.putIfAbsent(uuid, System.currentTimeMillis() - 100));
        long current = System.currentTimeMillis();
        long freq = current-last;
        if(freq < 100) {
            return;
        }
        Player player = Bukkit.getPlayer(uuid);
        chargeLogic logic = charge.get(uuid);
        float elapsed = (System.currentTimeMillis() - logic.start) / 1000f;
        BukkitRunnable runnable = tasks.get(uuid);
        if(runnable != null) runnable.cancel();
        BukkitRunnable task = new BukkitRunnable(){
            @Override
            public void run() {
                logic.action.onRelease(player, elapsed);
                tasks.remove(uuid);
                charge.remove(uuid);
                this.cancel();
            }
        };
        tasks.put(uuid, task);
        if(elapsed >= logic.maxTime){
            task.runTask(JasperProject.getPlugin());
            return;
        }
        logic.action.onTicking(player,elapsed);
        task.runTaskLater(JasperProject.getPlugin(), 12);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    /** contain methodL:
     * {@link #onRelease(Player, float)} action when released
     * {@link #onTicking(Player, float)} action when hold
     */
    public interface ChargAction {
        /**
         * Action when charging released
         *
         * @param player Player that trigger
         * @param power  The duration in second of the hold
         */
        void onRelease(@Nullable Player player, float power);

        /** Action when charging,
         * <hr>
         *it recommended using something looping
         * @param p The player who holding
         * @param power The duration in second current holding
         */
        void onTicking(@Nullable Player p, float power);
    }

    private static class chargeLogic {
        private final long start = System.currentTimeMillis();
        private final byte maxTime;
        private final ChargAction action;

        chargeLogic(byte maxTime, ChargAction action) {
            this.maxTime = maxTime;
            this.action = action;
        }
    }
}
