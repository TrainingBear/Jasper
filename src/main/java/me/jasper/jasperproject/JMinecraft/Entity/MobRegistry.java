package me.jasper.jasperproject.JMinecraft.Entity;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MobRegistry {
    private static final Map<String, NPC> mobs = new HashMap<>();
    public static Optional<NPC> getMob(String name){
        return Optional.of(mobs.get(name));
    }
    public static Optional<NPC> getMob(Class<? extends MobFactory> clazz){
        return Optional.of(mobs.get(clazz.getSimpleName()));
    }
    public static void register(MobFactory... npc){
        for (MobFactory factory : npc) {
            mobs.put(factory.name, factory.create(CitizensAPI.getNPCRegistry()));
        }
    }
    public static void unregister(){
        for (NPC npc : mobs.values()) {
            npc.destroy();
        }
    }
}
