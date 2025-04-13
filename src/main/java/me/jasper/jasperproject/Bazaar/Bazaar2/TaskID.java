package me.jasper.jasperproject.Bazaar.Bazaar2;

import me.jasper.jasperproject.Util.ContainerMenu.Content;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public final class TaskID {
    public static final byte SWAP_CATEGORY = 0;
    public static final byte CLOSE = 11;
    public static final byte TITLE = 12;
    public static final byte CATEG_NAV_NEXT = 13;
    public static final byte CATEG_NAV_BACK = 14;

    public final static Map<Byte, InventoryUpdater> MAP;
    static {
        MAP = new HashMap<>();
        MAP.put(SWAP_CATEGORY,
                (inv , ID, e) -> {
            List<Content> contents = Bazaar.getCategories();
            List<Content> result;
            Content selected_content = null;
            for (Content content : contents) {
                if(content.getID()==ID){
                    selected_content = content;
                    int index = contents.indexOf(content);
                    int subFirst = -(2-index);
                    int size = contents.size();

                    result = contents.subList(Math.max(subFirst, 0), size);
                    int[] cs = {1, 2, 3, 4, 5};
                    for (int c : cs) {
                        inv.setItem(c, null);
                    }
                    int startingIndex = Math.max(-subFirst, 0)+1;
                    for (int i = 0; i < result.size(); i++) {
                        if((i+startingIndex)==6) break;
                        inv.setItem(i+startingIndex, result.get(i).getItem());
                    }
                    break;
                }
            }
            TaskID.UpdateSubcategory(ID, inv);
            TaskID.UpdateDecoration(selected_content, inv);
        });
        MAP.put(
            CLOSE,
            (inv , ID, e) -> e.getWhoClicked().closeInventory());


    }


    public static void UpdateDecoration(Content selectedItem, Inventory inventory){
        int[] indexSlots = {

                19,                  26,
                28,                  35,
                37,                  44,
                46,47,48,   50,51,52,53,
        };
//        Border decoration = Bazaar.getDecorationMap().getOrDefault(ID.getID(), Bazaar.getDecorationMap().get(0));
//        Border pointer = Bazaar.POINTER;

        ItemStack item = Bazaar.POINTER.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.lore(List.of(
                MiniMessage.miniMessage().deserialize("")
                ,MiniMessage.miniMessage().deserialize("<!i><gray><b>▷</b> "
                        +MiniMessage.miniMessage().serialize(selectedItem.getItem().getItemMeta().displayName())
                        +" <gray><b>◁")
        ));
        item.setItemMeta(meta);
        inventory.setItem(12, item);
        ItemStack selectedItemStack = selectedItem.getItem().clone();
        ItemMeta selectedItemmeta = item.getItemMeta();
        selectedItemmeta.lore(List.of(
                        MiniMessage.miniMessage().deserialize("")
                        ,MiniMessage.miniMessage().deserialize("<color:#77aa77>Selected")
        ));
        selectedItemStack.setItemMeta(selectedItemmeta);
        inventory.setItem(3, selectedItemStack);

        for (int i : indexSlots) {
            inventory.setItem(
                    i,
                    Bazaar.getDecorationMap().getOrDefault(selectedItem.getID(), Bazaar.getDecorationMap().get(0)).getItem()
            );
        }



    }

    public static void UpdateSubcategory(int ID, Inventory inventory){
        int[] indexes = {
                20, 21, 22, 23, 24, 25,
                29, 30, 31, 32, 33, 34,
                38, 39, 40, 41, 42, 43
        };
        for (int i : indexes) inventory.setItem(i, null);

        List<Content> SubCategory = Bazaar.getSubCategories().getOrDefault(ID, null);
        if(SubCategory==null) return;
        Iterator<Content> iterator = Bazaar.getSubCategories().get(ID).iterator();
        for (int index : indexes) {
            if(!iterator.hasNext()) return;
            inventory.setItem(index, iterator.next().getItem());
        }
    }
}
