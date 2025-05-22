package me.jasper.jasperproject.JMinecraft.Entity;

import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobNameDisplay extends Display.TextDisplay {
    public MobNameDisplay(World world) {
        this(((CraftWorld) world).getHandle());
    }

    public MobNameDisplay(Level world) {
        super(EntityType.TEXT_DISPLAY, world);
    }

    public void spawn(Location location){
        spawn(location, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
    public void spawn(Location location, CreatureSpawnEvent.SpawnReason rason){
        this.setPos(location.x(), location.y(), location.z());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this, rason);
        Bukkit.broadcastMessage("spawned at "+this.position().toString());
    }

    @Override
    public void tick(){
        Entity var0 = this.getVehicle();
        if (var0==null) this.remove(RemovalReason.KILLED);
        if (var0 != null && var0.isRemoved()) this.remove(RemovalReason.KILLED);
        super.tick();
    }
}
