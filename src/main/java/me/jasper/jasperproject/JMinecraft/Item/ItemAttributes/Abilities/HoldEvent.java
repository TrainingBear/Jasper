package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HoldEvent extends Event implements Listener, Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList(); public HoldEvent() { } public static HandlerList getHandlerList() {return HANDLER_LIST;}@Override public @NotNull HandlerList getHandlers() {return HANDLER_LIST;}
    @Getter private static final Map<UUID, Long> lastClick = new HashMap<>();
    @Getter private static final Map<UUID, BukkitRunnable> task = new HashMap<>();
    @Getter private UUID player;
    @Getter private onTicking onTicking;
    @Getter private Runnable onRelease;
    private boolean cancelled;

    public HoldEvent(Player player, onTicking onTicking, Runnable onRelease) {
        this.player = player.getUniqueId();
        this.onRelease = onRelease;
        this.onTicking = onTicking;
    }

    @EventHandler
    public void onHold(HoldEvent e){
        if(e.isCancelled()) return;
        UUID uuid = e.getPlayer();
        lastClick.putIfAbsent(uuid, System.currentTimeMillis());

        long elapsed = (System.currentTimeMillis() - lastClick.put(uuid, System.currentTimeMillis()));
        if(elapsed <= 100) return;
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
            }
        };
        task.putIfAbsent(uuid, bukkitRunnable);
        bukkitRunnable.runTask(JasperProject.getPlugin());
        task.get(uuid).cancel();
        BukkitRunnable task_ = new BukkitRunnable() {
            @Override
            public void run() {
                e.getOnRelease().run();
                lastClick.remove(uuid);
            }
        };
        task.put(uuid, task_);
        if(e.getOnTicking().accept(elapsed, task_)) return;
        task_.runTaskLater(JasperProject.getPlugin(), 12L);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled=b;
    }

    @FunctionalInterface
    public interface onTicking{
        boolean accept(Long elapse, BukkitRunnable onRelease);
    }
}
