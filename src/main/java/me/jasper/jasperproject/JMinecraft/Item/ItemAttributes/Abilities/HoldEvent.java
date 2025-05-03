package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities;

import lombok.Getter;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class HoldEvent extends Event implements Listener, Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();public static HandlerList getHandlerList() {return HANDLER_LIST;}@Override public @NotNull HandlerList getHandlers() {return HANDLER_LIST;}
    @Getter private static final Map<UUID, Long> lastClick = new HashMap<>();
    @Getter private static final Map<UUID, BukkitRunnable> task = new HashMap<>();
    @Getter private UUID player;
    @Getter private BiConsumer<Long, BukkitRunnable> onTicking;
    @Getter private Runnable onRelease;
    private boolean cancelled;

    public HoldEvent(Player player, BiConsumer<Long, BukkitRunnable> onTicking, Runnable onRelease) {
        this.player = player.getUniqueId();
        this.onRelease = onRelease;
        this.onTicking = onTicking;
    }

    @EventHandler
    public void onHold(HoldEvent e){
        if(e.isCancelled()) return;
        UUID uuid = e.getPlayer();
        lastClick.putIfAbsent(uuid, System.currentTimeMillis());
        long last = lastClick.put(uuid, System.currentTimeMillis());
        long current = System.currentTimeMillis();
        long elapsed = (current - last);
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
        e.getOnTicking().accept(elapsed, task_);
        task.put(uuid, task_);
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
}
