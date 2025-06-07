package me.jasper.jasperproject.JMinecraft.Entity.Traits;

import io.papermc.paper.entity.LookAnchor;
import lombok.Builder;
import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Entity.MobRegistry;
import net.citizensnpcs.api.ai.AttackStrategy;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Builder
public class AgressiveTrait extends Trait {
    private LivingEntity target;
    private Function<LivingEntity, Boolean> filter;
    private Iterator<LivingEntity> victims;
    private int instinct_range = 8;
    private int follow_range = instinct_range + 2;
    private int attack_range = 3;
    private int delay = 20;
    private float speed = 1.5f;
    private boolean aggressive;
    private AttackStrategy strategy;
    private BiConsumer<NPC, LivingEntity> while_navigating;

    public AgressiveTrait(LivingEntity target, Function<LivingEntity, Boolean> filter, Iterator<LivingEntity> victims, int instinct_range, int follow_range, int attack_range, int delay, float speed, boolean aggressive, AttackStrategy strategy, BiConsumer<NPC, LivingEntity> while_navigating) {
        super("Aggressive");
        this.target = target;
        this.filter = filter;
        this.victims = victims;
        this.instinct_range = instinct_range == 0? this.instinct_range : instinct_range;
        this.follow_range = follow_range == 0? this.follow_range+this.instinct_range : follow_range+instinct_range;
        this.attack_range = attack_range == 0? this.attack_range : attack_range;
        this.delay = delay == 0? this.delay : delay;
        this.speed = speed==0?this.speed:speed;
        this.aggressive = aggressive;
        this.strategy=strategy;
        this.while_navigating = while_navigating;
    }

    @Override
    public void run() {
        if(!npc.isSpawned()) return;
        if(delay-- <= 0){
            delay = 10;
            if(target==null){
                target = getVictim();
            }
            else {
                Location location = target.getEyeLocation();
                double distance = npc.getEntity().getLocation().distance(location);
                if(target.isDead() || distance > follow_range ) {
                    target = null;
                    if(npc.getNavigator().isNavigating()) npc.getNavigator().setTarget(target, aggressive);
                    return;
                }
                if(npc.getNavigator().isNavigating() && while_navigating !=null && distance > attack_range){
                    while_navigating.accept(npc, target);
                }
                npc.getNavigator().setTarget(target, aggressive);
                npc.getEntity().lookAt(location.getX(), location.getY(), location.getZ(), LookAnchor.EYES);
                npc.getNavigator().getLocalParameters().speedModifier(speed);
                npc.getNavigator().getLocalParameters().attackRange(attack_range);
                npc.getNavigator().getLocalParameters().attackStrategy(strategy == null? (livingEntity, livingEntity1) -> {
                    livingEntity.attack(livingEntity1);
                    livingEntity.swingOffHand();
                    return true;
                } : strategy);
            }
        }
    }

    public LivingEntity getVictim(){
        if(victims==null || !this.victims.hasNext()) findNewVictim();
        while (victims.hasNext()){
            LivingEntity next = victims.next();
            if(next==null) continue;
            double distance = next.getLocation().distance(npc.getEntity().getLocation());
            if(distance > instinct_range) continue;
            if(!next.isDead()) return next;
        }
        return null;
    }

    public void findNewVictim(){
        Entity entity = npc.getEntity();
        List<LivingEntity> victims = new ArrayList<>();
        for (Entity nearbyEntity : entity.getNearbyEntities(instinct_range, instinct_range/2, instinct_range))
            if(nearbyEntity instanceof LivingEntity livingEntity)
                if(filter!=null && filter.apply(livingEntity))
                    victims.add(livingEntity);
                else{
                    if (!MobRegistry.getInstance().isNPC(nearbyEntity)) {
                        victims.add(livingEntity);
                    }
                }
        this.victims = victims.iterator();
    }
}
