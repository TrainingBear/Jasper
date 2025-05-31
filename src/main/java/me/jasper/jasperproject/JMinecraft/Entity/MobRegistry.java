package me.jasper.jasperproject.JMinecraft.Entity;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.HPTrait;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.NPCCreateEvent;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.npc.CitizensNPC;
import net.citizensnpcs.npc.CitizensNPCRegistry;
import net.citizensnpcs.npc.EntityControllers;
import net.citizensnpcs.trait.ArmorStandTrait;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class MobRegistry extends CitizensNPCRegistry {
    private static MobRegistry instance;
    public synchronized static MobRegistry getInstance(){
        if(instance==null){
            instance = new MobRegistry();
        }
        return instance;
    }
    private final MemoryNPCDataStore data = new MemoryNPCDataStore();
    @Getter private final Map<String, List<NPC>> mobs = new HashMap<>();
    private MobRegistry() {
        super(null);
    }

    public List<NPC> getMob(String name){
        return mobs.get(name);
    }

    public List<NPC> getMob(Class<? extends MobFactory> clazz){
        return mobs.get(clazz.getSimpleName());
    }
    @Override
    public NPC createNPCUsingItem(EntityType type, String name, ItemStack item) {
        NPC npc = createNPC(type, name);
        if (!type.name().equals("OMINOUS_ITEM_SPAWNER") && !type.name().equals("DROPPED_ITEM") && !type.name().equals("ITEM") && type != EntityType.FALLING_BLOCK && type != EntityType.ITEM_FRAME && !type.name().equals("GLOW_ITEM_FRAME") && !type.name().equals("ITEM_DISPLAY") && !type.name().equals("BLOCK_DISPLAY")) {
            throw new UnsupportedOperationException("Not an item entity type");
        } else {
            npc.data().set(NPC.Metadata.ITEM_AMOUNT, item.getAmount());
            npc.data().set(NPC.Metadata.ITEM_ID, item.getType().name());
            npc.setItemProvider(() -> item.clone());
            return npc;
        }
    }
    @Override
    public NPC createNPC(EntityType type, String name) {
        return this.createNPC(type, UUID.randomUUID(), 0, name);
    }
    @Override
    public NPC createNPC(EntityType type, UUID uuid, int id, String name) {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(type, "type cannot be null");
        CitizensNPC npc = new CitizensNPC(uuid, id, name, EntityControllers.createForType(type), this);
        npc.getOrAddTrait(MobType.class).setType(type);
        mobs.computeIfAbsent(name, k->new ArrayList<>()).add(npc);
        Bukkit.getPluginManager().callEvent(new NPCCreateEvent(npc));
        if (type == EntityType.ARMOR_STAND && !npc.hasTrait(ArmorStandTrait.class)) {
            npc.addTrait(ArmorStandTrait.class);
        }
        npc.addTrait(LookClose.class);
        return npc;
    }

    @Override
    public void deregister(NPC npc) {
        npc.despawn(DespawnReason.REMOVAL);
        String name = npc.getOrAddTrait(HPTrait.class).getName();
        this.mobs.get(name).remove(npc);
    }
    @Override
    public void deregisterAll(){
        for (List<NPC> value : mobs.values()) {
            for (NPC npc : value) {
                npc.destroy();
            }
        }
    }
}
