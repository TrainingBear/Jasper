package me.jasper.jasperproject.JMinecraft.Entity.MobPlayer;

import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;

public class PlayerBehavior extends BehaviorGoalAdapter {
    @Override
    public void reset() {
    }

    @Override
    public BehaviorStatus run() {
        return BehaviorStatus.FAILURE;
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }
}
