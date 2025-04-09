package me.jasper.jasperproject.JasperItem.Util;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class TRIGGER {
    public static final class Interact{
        public static boolean RIGHT_CLICK(PlayerInteractEvent e){
            return (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK));
        }
        public static boolean SHIFT_RIGHT_CLICK(PlayerInteractEvent e){
            return (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && e.getPlayer().isSneaking();
        }
        public static boolean RIGHT_CLICK_BLOCK(PlayerInteractEvent e){
            return e.getAction().equals(Action.RIGHT_CLICK_BLOCK);
        }
        public static boolean SHIFT_RIGHT_CLICK_BLOCK(PlayerInteractEvent e){
            return e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getPlayer().isSneaking();
        }
        public static boolean RIGHT_CLICK_AIR(PlayerInteractEvent e){
            return e.getAction().equals(Action.RIGHT_CLICK_AIR);
        }
        public static boolean SHIFT_RIGHT_CLICK_AIR(PlayerInteractEvent e){
            return e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().isSneaking();
        }

        public static boolean LEFT_CLICK(PlayerInteractEvent e){
            return (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK));
        }
        public static boolean SHIFT_LEFT_CLICK(PlayerInteractEvent e){
            return (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) && e.getPlayer().isSneaking();
        }
        public static boolean LEFT_CLICK_BLOCK(PlayerInteractEvent e){
            return e.getAction().equals(Action.LEFT_CLICK_BLOCK);
        }
        public static boolean SHIFT_LEFT_CLICK_BLOCK(PlayerInteractEvent e){
            return e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getPlayer().isSneaking();
        }
        public static boolean LEFT_CLICK_AIR(PlayerInteractEvent e){
            return e.getAction().equals(Action.LEFT_CLICK_AIR);
        }
        public static boolean SHIFT_LEFT_CLICK_AIR(PlayerInteractEvent e){
            return e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking();
        }
    }

}






