package me.jasper.jasperproject.JMinecraft.Entity;

import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public abstract class MobFactory {
    public final String name = this.getClass().getSimpleName();
    @Setter protected int lvl = 1;
    protected abstract NPC create(NPCRegistry registry);
}
