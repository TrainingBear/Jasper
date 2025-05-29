package me.jasper.jasperproject.JMinecraft.Entity.MobPlayer;

import me.jasper.jasperproject.Util.Util;
import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class FollowBehavior extends BehaviorGoalAdapter {
    private final NPC npc;
    private final Entity target;
    private boolean aggressive;

    public FollowBehavior(NPC npc, Entity target) {
        this(npc, target, false);
    }
    public FollowBehavior(NPC npc, Entity target, boolean aggressive) {
        this.npc = npc;
        this.target = target;
        this.aggressive = aggressive;
    }

    @Override
    public void reset() {
    }

    @Override
    public BehaviorStatus run() {
        return BehaviorStatus.RUNNING;
    }

    @Override
    public boolean shouldExecute() {
        this.npc.getNavigator().setTarget(target, aggressive);
        Util.debug(npc.getEntity().getName() +" is now targeting "+target.getName());
        if(npc.getEntity().getNearbyEntities(1, 1, 1).contains(target)){
            ((LivingEntity) npc.getEntity()).setHealth(0);
            Util.debug(npc.getName() +" death");
            return true;
        }
        if(npc.getEntity().getNearbyEntities(2, 2, 2).contains(target)){
            Util.debug(npc.getName() +" is reached the target!");
            return true;
        }
        return false;
    }
}
