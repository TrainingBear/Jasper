package me.jasper.jasperproject.JMinecraft.Entity.MobPlayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;

public class PlayerEntity extends Player implements EPlayer {
    public PlayerEntity(World world) {
        this(((CraftWorld) world).getHandle());
    }
    public PlayerEntity(Level world) {
        super(world, null, 0f, null);

    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    @Override
    public void tick() {
        onTick();
        super.tick();
    }
}
