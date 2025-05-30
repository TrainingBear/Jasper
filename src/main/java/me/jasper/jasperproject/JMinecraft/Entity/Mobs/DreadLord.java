package me.jasper.jasperproject.JMinecraft.Entity.Mobs;

import me.jasper.jasperproject.JMinecraft.Entity.MobFactory;
import me.jasper.jasperproject.JMinecraft.Entity.MobNameDisplay;
import me.jasper.jasperproject.JMinecraft.Entity.Traits.HPTrait;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.HologramTrait;
import org.bukkit.entity.EntityType;

public class DreadLord extends MobFactory {
    @Override
    protected NPC create(NPCRegistry registry) {
        NPC npc = registry.createNPC(EntityType.PLAYER, this.name);
        new HPTrait("Dread Lord", lvl);
        npc.addTrait(.class);
        return npc;
    }
}
