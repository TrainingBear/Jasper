package me.jasper.jasperproject.JMinecraft.Entity.MobPlayer;

import me.jasper.jasperproject.Util.Util;
import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;

import java.util.List;

public class HostileBehavior extends BehaviorGoalAdapter {
    private final NPC npc;
    private FollowBehavior follow;

    public HostileBehavior(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void reset() {
    }

    @Override
    public BehaviorStatus run() {
        if (follow==null) return BehaviorStatus.SUCCESS;
        return follow.run();
    }

    @Override
    public boolean shouldExecute() {
        Util.debug("Running..");
        List<Entity> nearbyEntities = this.npc.getEntity().getNearbyEntities(10.0, 10.0, 10.0);
        if(!nearbyEntities.isEmpty()){
            follow = new FollowBehavior(npc, nearbyEntities.getFirst(), true);
            return true;
        }
        return false;
    }
}
