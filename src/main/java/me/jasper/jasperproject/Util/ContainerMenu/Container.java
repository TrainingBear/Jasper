package me.jasper.jasperproject.Util.ContainerMenu;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class Container {
    @Getter private final Inventory container;
    private final List<Content> contents = new ArrayList<>();
    private int[] layout = null;

    public Container(Player player, int[] layout){
        this(player, MiniMessage.miniMessage().deserialize("Unknown"), layout);
    }
    public Container(Player player, Component name, int[] layout){
        this.layout = layout;
        this.container = Bukkit.createInventory(player, Math.min(54, layout.length), name);
    }

    public Container(Player player, Component name, int size){
        this.container = Bukkit.createInventory(player, Math.min(54, size), name);
    }

    public void addContent(Content e){
        contents.add(e);
    }public void addContent(List<Content> e){
        contents.addAll(e);
    }

    public void load(){
        load(null);
    }
    public void load(@Nullable Consumer<Content> consumer){
        if (layout==null) return;
        Set<Integer> ID = new HashSet<>();
        Map<Integer, ItemStack> pallet = new HashMap<>();
        for (Content content : contents) {
            int id = content.getID();
            ID.add(id);
            pallet.put(id, content.getItem());
        }

        for (int i = 0; i < layout.length; i++) {
            int id = layout[i];
            if (ID.contains(id)) {
                container.setItem(i, pallet.get(id));
            }
        }
    }
}
