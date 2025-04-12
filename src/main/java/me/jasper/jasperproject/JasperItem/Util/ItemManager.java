package me.jasper.jasperproject.JasperItem.Util;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Warper;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Product.*;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.HashSet;

@Getter
public final class ItemManager {
    private static ItemManager instance;
    public static ItemManager getInstance() {
        if(instance==null){
            instance=new ItemManager();
        }
        return instance;
    }
    private final PluginManager pluginManager = JasperProject.getPM();
    private final Plugin plugin = JasperProject.getPlugin();

    private final HashSet<ItemAbility> abilities = new HashSet<>();
    private final HashMap<String, Jitem> items = new HashMap<>();
    private final HashSet<ENCHANT> enchants = new HashSet<>();

    private void registerItem(Factory factory){
        Jitem item = factory.create();
        item.update();
        items.put(item.getID(), item);
    }
    private void registerAbility(ItemAbility ability){
        pluginManager.registerEvents(ability, plugin);
        abilities.add(ability);
    }

    public void registerAll(){

        registerAbility(Teleport.getInstance());
        registerAbility(Grappling_Hook.getInstance());
        registerAbility(Warper.getInstance());
        registerAbility(Animator.getInstance());

        registerItem(new Blender());
        registerItem(new EndGateway());
        registerItem(new WarpGateway());
        registerItem(new GraplingHook());
        registerItem(new TestItem());
    }
}
