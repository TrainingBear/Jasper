package me.jasper.jasperproject.JasperItem;

import com.google.common.base.Preconditions;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.xml.stream.events.Namespace;
import java.util.List;

enum ENCHANT {
    SharpnesV2;


    String name;
    byte level;
    byte max_level;
    List<String> lore;
    PersistentDataContainer data;


    ENCHANT(){

    }

    ENCHANT(String name, byte max_level, List<String> lore) {
        this.name = name;
        this.max_level = max_level;
        this.lore = lore;
    }

    public void addLevel(byte level){
        if(level > max_level) return;
        this.level+=level;
    }

    public void decreaseLevel(byte level){
        if(level <=0 )return;
        this.level-=level;
    };


}
