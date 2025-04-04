package me.jasper.jasperproject.JasperEntity.MobEvent;

import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class JasperMobDamagedEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    @Getter private LivingEntity nama;
    @Getter private Player damager;
    @Getter private double damage;
    public JasperMobDamagedEvent(LivingEntity nama, Player damager, double damage){
        this.nama = nama;
        this.damager = damager;
        this.damage = damage;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public LivingEntity getEntity() {
        return nama;
    }

}
