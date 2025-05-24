package me.jasper.jasperproject.JMinecraft.Entity.MobPlayer;

import me.jasper.jasperproject.Util.Util;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerEntity implements EPlayer {
    private final static List<NPC> createdNPC = new ArrayList<>();
    public static void test(Location location){
        String name = "First NPC";
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        npc.spawn(location);
        createdNPC.add(npc);
        npc.getDefaultGoalController().addGoal(new HostileBehavior(npc), 0);
        Util.debug("Spawned NPC with name of: "+name);
    }

    public static void killall(){
        for (NPC npc : createdNPC) {
            npc.getEntity().remove();
            Util.debug("Removed "+npc.getName());
        }
    }

    public static void kill(Location location){
        List<Entity> nearbyEntities = (List<Entity>) location.getNearbyEntities(5, 5, 5);
        if(!nearbyEntities.isEmpty()) {
            for (Entity nearbyEntity : nearbyEntities) {
                if(CitizensAPI.getNPCRegistry().isNPC(nearbyEntity)){
                    Entity var = nearbyEntities.removeFirst();
                    NPC npc = CitizensAPI.getNPCRegistry().getNPC(var);
                    npc.getEntity().remove();
                    Util.debug("Killed "+var.getName());
                }
            }
        }
    }
}
