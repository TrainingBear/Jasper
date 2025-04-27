package me.jasper.jasperproject.JMinecraft.Entity.MobEvent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class JasperMobDeathEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final LivingEntity fullMob;

    public JasperMobDeathEvent(LivingEntity fullMob) {
        this.fullMob = fullMob;
    }

    public JasperMobDeathEvent(boolean isAsync, LivingEntity fullMob) {
        super(isAsync);
        this.fullMob = fullMob;
    }


    public static HandlerList getHandlerList() {
        return handlerList;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public Entity getEntity() {
        return fullMob;
    }
}
