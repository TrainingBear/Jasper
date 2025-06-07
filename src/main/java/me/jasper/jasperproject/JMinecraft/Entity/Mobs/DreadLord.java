package me.jasper.jasperproject.JMinecraft.Entity.Mobs;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.jasper.jasperproject.JMinecraft.Entity.MobFactory;
import me.jasper.jasperproject.JMinecraft.Entity.MobRegistry;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.AgressiveTrait;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.HPTrait;
import me.jasper.jasperproject.Util.Util;
import net.citizensnpcs.api.ai.AttackStrategy;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

public class DreadLord extends MobFactory {
    public DreadLord() {
        super("Dread Lord");
    }

    @Override
    protected NPC create(MobRegistry registry) {
        NPC npc = registry.createNPC(EntityType.PLAYER, name);
        HPTrait dreadLord = new HPTrait("Dread Lord", lvl);
        LookClose lookClose = new LookClose();
        BiConsumer<NPC, LivingEntity> while_targeting = (n, target) -> {
//            n.getNavigator().getLocalParameters().speedModifier()
        };
        class ref{
            private int deg = 0;
            public int getAndIncrease(int n){
                int t = Math.min(Math.abs(deg+n), 360);
                deg = t==360? 0 : t;
                return ThreadLocalRandom.current().nextBoolean() ? deg : deg-(2*deg);
            }
        }
        ref var = new ref();
        AttackStrategy strategy = (a, t)->{
            if(!MobRegistry.getInstance().isNPC(a)) return false;
            Location center = t.getLocation();
            double x = center.getX() + 3 * Math.cos(Math.toRadians(var.getAndIncrease(25)));
            double y = center.getZ() + 3 * Math.sin(Math.toRadians(var.getAndIncrease(25)));
            boolean occur = ThreadLocalRandom.current().nextBoolean();
            Location location = new Location(t.getWorld(), x, a.getLocation().getY(), y);
            if(occur){
                if(a instanceof Player playera) {
                    ServerPlayer player = ((CraftPlayer) playera).getHandle();
                    byte flag = 0x02;
                    player.setShiftKeyDown(true);
                    SynchedEntityData.DataValue<Byte> data = new SynchedEntityData.DataValue<>(0, EntityDataSerializers.BYTE, flag);
                    ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(player.getId(), List.of(data));
                    if(t instanceof Player target){
                        ((CraftPlayer) target).getHandle().connection.send(packet);
                    }
                    a.setJumping(true);
                    a.attack(t);
                    a.swingOffHand();
                    if(isSafe(location)) a.setVelocity(location.toVector().subtract(a.getLocation().toVector()).multiply(0.11f));
                    a.getWorld().spawnParticle(Particle.FLAME, location, 10, 0, 0, 0, 0);
                }
            }
            else {
                ((CraftPlayer) a).getHandle().setShiftKeyDown(false);
                a.setJumping(true);
                a.attack(t);
                a.swingOffHand();
                if(isSafe(location)) a.setVelocity(location.toVector().subtract(a.getLocation().toVector()).multiply(0.11f));
                a.getWorld().spawnParticle(Particle.FLAME, location, 10, 0, 0, 0, 0);
                if(a instanceof Player bukit) {
                    ((CraftPlayer) bukit).getHandle().setShiftKeyDown(false);
                }
            }
            return true;
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

    public void forceSneaking(Player target, boolean sneaking) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, target.getEntityId());

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);

        byte sneakingBitMask = 0x02; // bit 1 = sneaking
        byte flags = 0;

        if (sneaking) flags |= sneakingBitMask;

        watcher.setObject(0, byteSerializer, flags);
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        try {
            for (Player observer : Bukkit.getOnlinePlayers()) {
                if (!observer.equals(target)) {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(observer, packet);
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private boolean isSafe(Location location){
        boolean safe;
        Material material = location.getBlock().getBlockData().getMaterial();
        safe = !material.equals(Material.LAVA) && !material.equals(Material.WATER);
        if(!safe) return safe;
        Location clone = location.clone();
        clone.add(0, -1, 0);
        material = clone.getBlock().getBlockData().getMaterial();
        safe = !material.equals(Material.LAVA) && !material.equals(Material.WATER) && !material.equals(Material.AIR);
        return safe;
    }
}
