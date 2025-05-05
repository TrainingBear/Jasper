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
 */
public class Charge extends Event implements Listener, Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList(); public Charge() { }

    public static HandlerList getHandlerList() {return HANDLER_LIST;}@Override public @NotNull HandlerList getHandlers() {return HANDLER_LIST;}
    private boolean cancelled;
    @Getter private static Map<UUID, chargeLogic> charge = new HashMap<>();

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
        charge.putIfAbsent(p.getUniqueId(), new Charge.chargeLogic((byte) maxTimeInSec, action));
    }
    @EventHandler
    public void run(){

    }



    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public interface ChargAction {
        /** Action when charging released
         *
         * @param player Player that trigger
         * @param power The duration in second of the hold
         */
        boolean doAction(Player player, float power);

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

        public void startTask(Player player) {
            new BukkitRunnable() {
                @Override
                public void run() {

                }
            }.runTaskLater(JasperProject.getPlugin(), 12);
        }

        public float countToMS() {
            return ((System.currentTimeMillis() - start) / 1000f);
        }

        public void doTheAction(Player p , float count){
            if(this.action != null) this.action.doAction(p,count);
        }

    }
    @EventHandler
    public void Charge(Charge e){

    }
}
