package event;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerEvent;

public class PlayerToggleSprintEvent extends PlayerEvent implements Cancellable{

    public PlayerToggleSprintEvent(Player who) {
        super(who);
        who.setHealth(0.0);
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}