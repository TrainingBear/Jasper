package me.jasper.jasperproject.JMinecraft.Entity;

import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;

@Getter
public abstract class MobFactory {
    protected final String name;
    @Setter private MobRegistry registry;
    public MobFactory(String name){
        this(MobRegistry.getInstance(), name);
    }
    public MobFactory(MobRegistry registry, String name){
        this.registry = registry;
        this.name = name;
    }
    @Setter protected int lvl = 1;
    protected abstract NPC create(MobRegistry registry);
    public void spawn(Location location){
        NPC npc = create(registry);
        npc.data().setPersistent(NPC.Metadata.DEFAULT_PROTECTED, false);
        npc.data().setPersistent(NPC.Metadata.FLUID_PUSHABLE, true);
        npc.data().setPersistent("pushable", true);
        npc.spawn(location);
    }
}
