package me.jasper.jasperproject.JMinecraft.Entity.MobPlayer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Vindicator;

public interface AggresivePlayer extends EPlayer {
    @Override
    default void onTick() {
        Vindicator vindicator = new Vindicator(EntityType.VINDICATOR, null);
        vindicator.tick();
        EPlayer.super.onTick();
    }
}
