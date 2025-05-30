package me.jasper.jasperproject.JMinecraft.Entity;

import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;


public abstract class MobFactory {
    public final String name = this.getClass().getSimpleName();
    @Setter protected int lvl = 1;
    protected abstract NPC create(MobRegistry registry);
    public void spawn(Location location){
        NPC npc = create(MobRegistry.getInstance());
        npc.spawn(location);
    }
}
