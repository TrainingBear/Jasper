package me.jasper.jasperproject.JMinecraft.Item.Util;

import lombok.Getter;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Util that useful for holding mechanic
 * <hr>
 * How to use: Bukkit.getPluginManager().callEvent(new {@link Charge}(player,byte maxtime, ChargeAction));
 * @Override the {@link ChargAction}
 */
public class Charge extends Event implements Listener, Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    public Charge() { }

    @Override public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    private boolean cancelled;
    @Getter private static Map<UUID, chargeLogic> charge = new HashMap<>();
    @Getter private static Map<UUID, Long> antiSPAM = new HashMap<>(); // bisa aja guna/kaga
    @Getter private static Map<UUID, BukkitRunnable> cancelTask = new HashMap<>();
    @Getter private Player player;

    /** Use this method when something iterate useful for logic that uses hold mechanic
     * <hr>
     *  since Bukkit API/listener doesn't provide to check player is currently holding or not.
     *  Instead, when player hold it trigger in a frequency
     * @param p Player who holding
     * @param maxTimeInSec Max duration of player allow to hold <b>Disclaimer: when the hold duration exceed
     *                     the maxTime, will immediately run the action, still charging when it still triggers/calls</b>
     * @param action what actions within holding
     */
    public Charge(Player p,int maxTimeInSec, ChargAction action){
        this.player = p;
        charge.putIfAbsent(p.getUniqueId(), new chargeLogic((byte) maxTimeInSec, action));
        antiSPAM.putIfAbsent(p.getUniqueId(), System.currentTimeMillis()-(CLICK_GAP+1));
    }
    private final short CLICK_GAP = 240;
    @EventHandler
    public void run(Charge e){
        if(System.currentTimeMillis() - antiSPAM.get(e.getPlayer().getUniqueId()) <= CLICK_GAP) {
            removeMap(e.getPlayer().getUniqueId());
            return;
        }

        UUID uuid = e.getPlayer().getUniqueId();
        chargeLogic logic = charge.get(uuid);
        float elapsed = (System.currentTimeMillis() - logic.start) / 1000f;
        BukkitRunnable runnable = cancelTask.get(uuid);
        if(runnable != null) runnable.cancel();

        BukkitRunnable task = new BukkitRunnable(){
            @Override
            public void run() {
                logic.action.doAction(e.getPlayer(),elapsed);
                removeMap(uuid);
                this.cancel();
            }
        };
        cancelTask.put(uuid, task);
        if(elapsed >= logic.maxTime){
            logic.action.doAction(e.getPlayer(),elapsed);
            removeMap(uuid);
            return;
        }
        logic.action.whileHold(e.getPlayer(),elapsed);
        cancelTask.get(uuid).runTaskLater(JasperProject.getPlugin(), 12);
    }
    public static void removeMap(UUID uuid){
        cancelTask.remove(uuid);
        antiSPAM.remove(uuid);
        charge.remove(uuid);
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
     * {@link #doAction(Player, float)} action when released
     * {@link #whileHold(Player, float)} action when hold
     */
    public interface ChargAction {
        /**
         * Action when charging released
         *
         * @param player Player that trigger
         * @param power  The duration in second of the hold
         */
        void doAction(Player player, float power);

        /** Action when charging,
         * <hr>
         *it recommended using something looping
         * @param p The player who holding
         * @param power The duration in second current holding
         */
        default void whileHold(Player p, float power){}
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
