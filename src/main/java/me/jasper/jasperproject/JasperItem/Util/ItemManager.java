package me.jasper.jasperproject.JasperItem.Util;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.*;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Product.Tools.Blender;
import me.jasper.jasperproject.JasperItem.Product.Tools.GraplingHook;
import me.jasper.jasperproject.JasperItem.Product.Tools.Titanium_Pickaxe;
import me.jasper.jasperproject.JasperItem.Product.Utilities.Feather_Jumper;
import me.jasper.jasperproject.JasperItem.Product.Utilities.Healing_Staff;
import me.jasper.jasperproject.JasperItem.Product.Utilities.Healing_Wand;
import me.jasper.jasperproject.JasperItem.Product.Weapons.*;
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
    private final HashMap<String, JItem> items = new HashMap<>();
    private final HashSet<ENCHANT> enchants = new HashSet<>();

    private void registerItem(Factory factory){
        JItem item = factory.create();
        item.update();
        items.put(item.getID().toUpperCase(), item);
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
        registerAbility(Burst_Arrow.getInstance());
        registerAbility(Heal.getInstance());
        registerAbility(Jumper.getInstance());
        registerAbility(BackStab.getInstance());

        registerItem(new Blender());
        registerItem(new End_Gateway());
        registerItem(new Warp_Gateway());
        registerItem(new GraplingHook());
        registerItem(new TestItem());
        registerItem(new Titanium_Sword());
        registerItem(new Burst_Bow());
        registerItem(new Healing_Wand());
        registerItem(new Healing_Staff());
        registerItem(new Titanium_Pickaxe());
        registerItem(new Feather_Jumper());
        registerItem(new Assassin_Dagger());
    }
}
