package me.jasper.jasperproject.JasperItem.Util;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
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
    public static final class Fishing{
        public static boolean FISHING_THROW(PlayerFishEvent e){
            return e.getState() == PlayerFishEvent.State.FISHING;
        }
        public static boolean BOBBER_ON_GROUND(PlayerFishEvent e){
            return e.getState() == PlayerFishEvent.State.IN_GROUND;
        }
        public static boolean CAUGHT_FISH(PlayerFishEvent e){
            return e.getState() == PlayerFishEvent.State.CAUGHT_FISH;
        }
        public static boolean CAUGHT_ENTITY(PlayerFishEvent e){
            return e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY;
        }
        public static boolean REEL_WITHOUT_CATCHING(PlayerFishEvent e){
            return e.getState() == PlayerFishEvent.State.REEL_IN;
        }
        public static boolean FAILED_CATCH(PlayerFishEvent e){
            return e.getState() == PlayerFishEvent.State.FAILED_ATTEMPT;
        }
        public static boolean BOOBER_LURING_FISH(PlayerFishEvent e){
            return e.getState() == PlayerFishEvent.State.LURED;
        }
    }
}






