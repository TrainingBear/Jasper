package me.jasper.jasperproject.JMinecraft.Entity.Mobs;

import me.jasper.jasperproject.JMinecraft.Entity.MobPlayer.AggresivePlayer;
import me.jasper.jasperproject.JMinecraft.Entity.MobPlayer.PlayerEntity;
import net.minecraft.world.level.Level;
import org.bukkit.World;

public class Johny extends PlayerEntity implements AggresivePlayer {
    public Johny(World world) {
        super(world);
    }

    public Johny(Level world) {
        super(world);
    }
}
