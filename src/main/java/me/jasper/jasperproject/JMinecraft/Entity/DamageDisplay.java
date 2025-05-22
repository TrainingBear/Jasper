package me.jasper.jasperproject.JMinecraft.Entity;

import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class DamageDisplay extends Display.TextDisplay {
    public DamageDisplay(World world) {
        this(((CraftWorld) world).getHandle());
    }

    public DamageDisplay(Level world) {
        super(EntityType.TEXT_DISPLAY, world);
    }

    public void spawn(Location location){
        spawn(location, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public void spawn(Location location, CreatureSpawnEvent.SpawnReason rason){
        this.setPos(location.x(), location.y(), location.z());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this, rason);
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
