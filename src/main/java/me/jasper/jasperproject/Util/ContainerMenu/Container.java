package me.jasper.jasperproject.Util.ContainerMenu;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Container {
    @Getter private final Inventory container;
    private final List<Content> contents = new ArrayList<>();
    private int[][] layout;

    public Container(Player player, int[][] layout){
        this(player, MiniMessage.miniMessage().deserialize("Unknown"), layout);
    }
    public Container(Player player, Component name, int[][] layout){
        this.layout = layout;
        this.container = Bukkit.createInventory(player, layout.length*layout[0].length, name);
    }

    public void addContent(Content e){
        contents.add(e);
    }public void addContent(List<Content> e){
        contents.addAll(e);
    }

    public void load(){
        Set<Integer> ID = new HashSet<>();
        Map<Integer, ItemStack> pallet = new HashMap<>();
        for (Content content : contents) {
            int id = content.getID();
            ID.add(id);
            pallet.put(id, content.getItem());
        }

        int index = 0;
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                int id = layout[i][j];
                if(ID.contains(id)){
                    container.setItem(index, pallet.get(id));
                }
                index++;
            }
        }
    }
}
