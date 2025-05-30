package me.jasper.jasperproject.JMinecraft.Entity.MobPlayer;

import me.jasper.jasperproject.Util.Util;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.goals.MoveToGoal;
import net.citizensnpcs.api.ai.tree.Sequence;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerEntity implements EPlayer {
    private final static List<NPC> createdNPC = new ArrayList<>();
    public static void test(Location location, Player player){
        String name = "First NPC";
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        createdNPC.add(npc);
        npc.getDefaultGoalController().addGoal(new MoveToGoal(npc, player.getLocation()), 1);
        npc.setProtected(false);
        npc.spawn(location);
        npc.destroy();
        ((LivingEntity) npc.getEntity()).setMaxHealth(20);
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
                    if(npc!=null) npc.getEntity().remove();
                    Util.debug("Killed "+var.getName());
                }
            }
        }
    }
}
