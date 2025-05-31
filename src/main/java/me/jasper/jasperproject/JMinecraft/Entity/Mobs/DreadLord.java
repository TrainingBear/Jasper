package me.jasper.jasperproject.JMinecraft.Entity.Mobs;

import me.jasper.jasperproject.JMinecraft.Entity.MobFactory;
import me.jasper.jasperproject.JMinecraft.Entity.MobRegistry;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.AgressiveTrait;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.HPTrait;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.RotationTrait;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DreadLord extends MobFactory {
    @Override
    protected NPC create(MobRegistry registry) {
        NPC npc = registry.createNPC(EntityType.PLAYER, this.name);
        HPTrait dreadLord = new HPTrait("Dread Lord", lvl);
        LookClose lookClose = new LookClose();
        RotationTrait rot = (RotationTrait) npc.getOrAddTrait(RotationTrait.class);
        rot.getGlobalParameters().immediate(false);
        Goal wannderGoal = WanderGoal.builder(npc).xrange(5).yrange(5).delay(160).build();
        AgressiveTrait aggresive = AgressiveTrait.builder()
                .aggressive(true)
                .speed(2f)
                .build();
        SkinTrait skin = new SkinTrait();
        lookClose.setRandomLook(true);
        lookClose.setDisableWhileNavigating(true);
        lookClose.setRange(5);
        npc.addTrait(dreadLord);
        npc.getDefaultGoalController().addGoal(wannderGoal,10);
        npc.setProtected(false);

        npc.addTrait(lookClose);
        npc.addTrait(skin);
        npc.addTrait(aggresive);
        skin.setSkinName("DreadLord");
        return npc;
    }
}
