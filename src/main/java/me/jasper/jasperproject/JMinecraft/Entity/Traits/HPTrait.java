package me.jasper.jasperproject.JMinecraft.Entity.Traits;

import com.google.common.base.Optional;
import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.Util.Util;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;

@Getter
@Setter
public class HPTrait extends Trait {
    private int lvl;
    private String name2;
    public HPTrait(String name, int lvl) {
        super("DreadLord");
        this.name2 = name;
        this.lvl=lvl;
    }
    @Override
    public void run() {
    }

    @Override
    public void onSpawn() {
            LivingEntity entity = (LivingEntity) npc.getEntity();
            npc.setName( ChatColor.GRAY+"[Lv."+lvl+"] "
                    +ChatColor.GREEN+name2+ChatColor.GRAY+" | "+ChatColor.RED
                    + Util.satuan(entity.getHealth()) +"/"+Util.satuan(entity.getMaxHealth())+" ❤ ");
    }
}
