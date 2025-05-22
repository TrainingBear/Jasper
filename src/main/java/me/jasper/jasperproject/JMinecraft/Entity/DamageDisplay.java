package me.jasper.jasperproject.JMinecraft.Entity;

import net.minecraft.world.level.Level;
import org.bukkit.World;

public class DamageDisplay extends MobNameDisplay{
    public DamageDisplay(World world) {
        super(world);
    }

    public DamageDisplay(Level world) {
        super(world);
    }

    @Override
    public void tick(){
        if (this.tickCount >= 30) {
            this.remove(RemovalReason.KILLED);
            return;
        }
        super.tick();
    }
}
