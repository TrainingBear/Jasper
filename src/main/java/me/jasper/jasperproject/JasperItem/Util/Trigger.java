package me.jasper.jasperproject.JasperItem.Util;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

public final class Trigger implements Listener {
    static final class Interact{
        static boolean RIGHT_CLICK(Action action, Player player){
            return (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK));
        }
        static boolean SHIFT_RIGHT_CLICK(Action action, Player player){
            return (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && player.isSneaking();
        }
        static boolean RIGHT_CLICK_BLOCK(Action action, Player player){
            return action.equals(Action.RIGHT_CLICK_BLOCK);
        }
        static boolean SHIFT_RIGHT_CLICK_BLOCK(Action action, Player player){
            return action.equals(Action.RIGHT_CLICK_BLOCK) && player.isSneaking();
        }
        static boolean RIGHT_CLICK_AIR(Action action, Player player){
            return action.equals(Action.RIGHT_CLICK_AIR);
        }
        static boolean SHIFT_RIGHT_CLICK_AIR(Action action, Player player){
            return action.equals(Action.RIGHT_CLICK_AIR) && player.isSneaking();
        }

        static boolean LEFT_CLICK(Action action, Player player){
            return (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK));
        }
        static boolean SHIFT_LEFT_CLICK(Action action, Player player){
            return (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) && player.isSneaking();
        }
        static boolean LEFT_CLICK_BLOCK(Action action, Player player){
            return action.equals(Action.LEFT_CLICK_BLOCK);
        }
        static boolean SHIFT_LEFT_CLICK_BLOCK(Action action, Player player){
            return action.equals(Action.LEFT_CLICK_BLOCK) && player.isSneaking();
        }
        static boolean LEFT_CLICK_AIR(Action action, Player player){
            return action.equals(Action.LEFT_CLICK_AIR);
        }
        static boolean SHIFT_LEFT_CLICK_AIR(Action action, Player player){
            return action.equals(Action.LEFT_CLICK_AIR) && player.isSneaking();
        }
    }

}






