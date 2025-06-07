package me.jasper.jasperproject.JMinecraft.Entity.Mobs;

import com.mojang.datafixers.types.Func;
import me.jasper.jasperproject.JMinecraft.Entity.MobFactory;
import me.jasper.jasperproject.JMinecraft.Entity.MobRegistry;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.AgressiveTrait;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.HPTrait;
import net.citizensnpcs.api.ai.AttackStrategy;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.RotationTrait;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DreadLord extends MobFactory {
    public DreadLord() {
        super("Dread Lord");
    }

    @Override
    protected NPC create(MobRegistry registry) {
        NPC npc = registry.createNPC(EntityType.PLAYER, name);
        HPTrait dreadLord = new HPTrait("Dread Lord", lvl);
        LookClose lookClose = new LookClose();
        BiConsumer<LivingEntity, LivingEntity> while_targeting = (e, t) -> {
        };
        int degree = 0;
        AttackStrategy strategy = (a, t)->{
            if(npc.getNavigator().isPaused()) {
//                a.setJumping(true);
                a.attack(t);
                a.swingOffHand();
                Location center = t.getLocation();
                double x = center.getX() + 3 * Math.cos(Math.toRadians(degree));
                double y = center.getZ() + 3 * Math.sin(Math.toRadians(degree));
                npc.getNavigator().setTarget(new Location(t.getWorld(), x, npc.getEntity().getLocation().getY(), y));
                npc.getNavigator().setPaused(false);
                // degree = degree.get() >= 360 ? 0 : degree.addAndGet(10);
                return true;
            }
            return false;
        };
        Goal wannderGoal = WanderGoal.builder(npc).xrange(5).yrange(5).delay(160).build();
        AgressiveTrait aggresive = AgressiveTrait.builder()
                .aggressive(true)
                .speed(2f)
                .follow_range(15)
                .while_navigating(while_targeting)
                .strategy(strategy)
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
