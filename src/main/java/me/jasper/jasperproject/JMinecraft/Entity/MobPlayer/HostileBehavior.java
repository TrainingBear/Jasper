package me.jasper.jasperproject.JMinecraft.Entity.MobPlayer;

import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;

import java.util.List;

public class HostileBehavior extends BehaviorGoalAdapter {
    private final NPC npc;

    public HostileBehavior(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void reset() {

    }

    @Override
    public BehaviorStatus run() {
        List<Entity> nearbyEntities = this.npc.getEntity().getNearbyEntities(10.0, 10.0, 10.0);
        if(!nearbyEntities.isEmpty()){
            new FollowBehavior(npc, nearbyEntities.getFirst(), true);
            return BehaviorStatus.RUNNING;
        }
        return BehaviorStatus.FAILURE;
    }

    @Override
    public boolean shouldExecute() {
        return false;
    }
}
