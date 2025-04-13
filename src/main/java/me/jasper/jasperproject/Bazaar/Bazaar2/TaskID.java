package me.jasper.jasperproject.Bazaar.Bazaar2;

import jline.internal.Preconditions;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public final class TaskID {
    public static final byte SWAP_CATEGORY = 0;

    public final static Map<Byte, InventoryUpdater> MAP;
    static {
        MAP = new HashMap<>();
        MAP.put(SWAP_CATEGORY,
                (inv , ID) -> {
            Bukkit.broadcastMessage("[TaskID] Running...");
            List<Content> contents = Bazaar.getCategories();
            List<Content> result = new ArrayList<>();
            Content selected_content = null;
            for (Content content : contents) {
                Bukkit.broadcastMessage("[TaskID] Getting "+content.getID());
                if(content.getID()==ID){
                    Bukkit.broadcast(content.getItem().displayName());
                    selected_content = content;
                    int index = contents.indexOf(content);
                    int arange_index = 2-index;
                    int size = contents.size()-1;
                    result = result.subList(Math.abs(arange_index), size);
                    for (int i = 0; i < result.size(); i++) {
                        if((i+2)==6) break;
                        inv.setItem(i+2, content.getItem());
                    }
                    break;
                }
            }
            TaskID.UpdateSubcategory(ID, inv);
            TaskID.UpdateDecoration(selected_content, inv);
        });


    }

    public static final InventoryUpdater SWAP_1 = (inv , ID) -> {
        List<Content> contents = Bazaar.getCategories();
        List<Content> result = new ArrayList<>();
        Content selected_content = null;
        for (Content content : contents) {
            if(content.getID()==ID){
                selected_content = content;
                int index = contents.indexOf(content);
                int arange_index = 2-index;
                int size = contents.size();
                result = result.subList(Math.abs(arange_index), size);
                for (int i = 0; i < result.size(); i++) {
                    if((i+2)==6) break;
                    inv.setItem(i+2, content.getItem());
                }
                break;
            }
        }
        TaskID.UpdateSubcategory(ID, inv);
        TaskID.UpdateDecoration(selected_content, inv);

    };

    public static void UpdateDecoration(Content ID, Inventory inventory){
        int[] indexes = {

                19,                 26,
                28,                 35,
                37,                 44,
                46,  48,  50,  52,  53,
        };
        Border decoration = Bazaar.getDecorationMap().getOrDefault(ID.getID(), Bazaar.getDecorationMap().get(0));
        Border pointer = Bazaar.POINTER;

        ItemStack item = pointer.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ID.getItem().displayName());
        item.setItemMeta(meta);
        inventory.setItem(13, item);

        for (int i : indexes) {
            inventory.setItem(i, decoration.getItem());
        }



    }

    public static void UpdateSubcategory(int ID, Inventory inventory){
        int[] indexes = {
                20, 21, 22, 23, 24, 25,
                29, 30, 31, 32, 33, 34,
                38, 39, 40, 41, 42, 43
        };
        for (int i : indexes) {
            inventory.setItem(i, null);
        }
        List<Content> SubCategory = Bazaar.getSubCategories().getOrDefault(ID, null);
        if(SubCategory==null) return;
        Iterator<Content> iterator = Bazaar.getSubCategories().get(ID).iterator();
        for (int index : indexes) {
            if(!iterator.hasNext()) return;
            inventory.setItem(index, iterator.next().getItem());
        }
    }
}
