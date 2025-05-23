package me.jasper.jasperproject.JMinecraft.Entity.MobPlayer;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.entity.EntityType;

public class PlayerEntity implements EPlayer {
    public PlayerEntity(World world) {
        this(((CraftWorld) world).getHandle());
    }
    public PlayerEntity(String name) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        npc.spawn()
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
        this.goalSelector.addGoal(1, new Goal() {
            @Override
            public boolean canUse() {
                return false;
            }
        });
    }
}
