package me.jasper.jasperproject.JMinecraft.Entity.Mobs;

import me.jasper.jasperproject.JMinecraft.Entity.MobFactory;
import me.jasper.jasperproject.JMinecraft.Entity.MobRegistry;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.HPTrait;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;

public class DreadLord extends MobFactory {
    @Override
    protected NPC create(MobRegistry registry) {
        NPC npc = registry.createNPC(EntityType.PLAYER, this.name);
        HPTrait dreadLord = new HPTrait("Dread Lord", lvl);
        Goal wannderGoal = WanderGoal.builder(npc).xrange(5).yrange(5).build();
        npc.addTrait(dreadLord.getClass());
        npc.getDefaultGoalController().addGoal(wannderGoal,10);
        return npc;
    }
}
