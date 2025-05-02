package me.jasper.jasperproject.JMinecraft.Item.Util;

import lombok.Getter;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Util that useful for holding mechanic
 * <br>
 * {@link #charging(Player player,byte maxTimeInSec,ChargAction action)} method to use
 * <br>
 * <b>Use #getInstance() first when using #charging</b>
 */
public class Charg {
    private static Charg instance;

    public static Charg getInstance() {
        if (instance == null) {
            instance = new Charg();
        }
        return instance;
    }

    @Getter private static Map<UUID, chargeLogic> charge = new HashMap<>();

    /** Use this method when something iterate useful for logic that uses hold mechanic
     * <hr>
     *  since Bukkit API/listener doesn't provide to check player is currently holding or not.
     *  Instead, when player hold it trigger in a frequency
     * @param player Player who holding
     * @param maxTimeInSec Max duration of player allow to hold <b>Disclaimer: when the hold duration exceed
     *                     the maxTime, will immediately run the action, still charging when it still triggers/calls</b>
     * @param action what actions within holding
     */
    public void charging(Player player,byte maxTimeInSec,ChargAction action) {
        chargeLogic chargeLogic = charge.get(player.getUniqueId())

        ;if (chargeLogic == null) {
            charge.put(player.getUniqueId(), new chargeLogic(System.currentTimeMillis(), maxTimeInSec,(byte) 1, action));
            charge.get(player.getUniqueId()).startTask(player);
        }
        else chargeLogic.addClick();

    }

    public interface ChargAction {
        /** Action when charging released
         *
         * @param player Player that trigger
         * @param power The duration in second of the hold
         */
        void doAction(Player player, float power);

        /** Action when charging,
         * <hr>
         *it recommended using something looping
         * @param p The player who holding
         * @param power The duration in second current holding
         */
        default void whileCharg(Player p,float power){}
    }

    private static class chargeLogic {
        private final long start;
        private final byte maxTime;
        private byte click;
        private final ChargAction action;
        private final byte trigger;

        chargeLogic(long timeStart,byte maxTime ,byte trigger, ChargAction action) {
            this.maxTime = maxTime;
            this.start = timeStart;
            this.trigger = trigger;
            this.action = action;
            this.click = 0;
        }

        public void startTask(Player player) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!player.isOnline()){
                        Charg.getCharge().remove(player.getUniqueId());
                        this.cancel();
                    }
                    if (click < trigger) {
                        chargeLogic mechanic = Charg.getCharge().remove(player.getUniqueId());
                        if(mechanic != null) mechanic.doTheAction(player,countToMS());
                        this.cancel();
                    }
                    else {
                        click = 0;
                        if(countToMS() >= maxTime) {
                            chargeLogic mechanic = Charg.getCharge().remove(player.getUniqueId());
                            if(mechanic != null) mechanic.doTheAction(player,countToMS());
                            this.cancel();
                        }
                        if(action != null) action.whileCharg(player,countToMS());
                    }

                }
            }.runTaskTimer(JasperProject.getPlugin(), 6, 6);
        }

        public float countToMS() {
            return ((System.currentTimeMillis() - start) / 1000f);
        }

        public void addClick() {
            this.click += 1;
        }
        public void doTheAction(Player p , float count){
            if(this.action != null) this.action.doAction(p,count);
        }

    }
}
