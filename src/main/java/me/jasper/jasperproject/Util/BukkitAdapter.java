package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JMinecraft.Entity.JMob;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BukkitAdapter {
    public static JMob adapt(LivingEntity entity){
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if(!pdc.has(JKey.MOBATRIBUTE_DISPLAY)) return null;
        JMob jMob = new JMob(((CraftLivingEntity) entity).getHandle());
        String name = pdc.get(JKey.MOBATRIBUTE_NAME, PersistentDataType.STRING);
        short level = pdc.get(JKey.MOBATRIBUTE_LEVEL, PersistentDataType.SHORT);
        jMob.setName(name);
        jMob.setLevel(level);
        return jMob;
    }
}
