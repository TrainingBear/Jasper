package me.jasper.jasperproject.Bazaar.Bazaar2;

import me.jasper.jasperproject.Util.ContainerMenu.Content;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TaskID {
    public static final byte SWAP_CATEGORY = 0;

    public final static Map<Byte, UpdateInventory> MAP;
    static {
        MAP = new HashMap<>();
    }

    public static final UpdateInventory SWAP_1 = (inv , ID) -> {
        List<Content> contents = Bazaar.getCategories();
        List<Content> result = new ArrayList<>();
        for (Content content : contents) {
            if(content.getID()==ID){
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
        // set whole glass pane according to the what item has been clicked
    };

    public static void UpdateSubcategory(Content selectedItem){

    }
}
