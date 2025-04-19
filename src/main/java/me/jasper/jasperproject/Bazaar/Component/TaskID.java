package me.jasper.jasperproject.Bazaar.Component;

import lombok.val;
import me.jasper.jasperproject.Bazaar.Bazaar;
import me.jasper.jasperproject.Bazaar.InventoryUpdater;
import me.jasper.jasperproject.Bazaar.Product.Product;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.SignGUI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public final class TaskID {
    public static final byte SWAP_CATEGORY = 0;

    public static final byte CLOSE = 11;
    public static final byte TITLE = 12;
    public static final byte CATEG_NAV_NEXT = 13;
    public static final byte CATEG_NAV_BACK = 14;

    public static final byte SEARCH = 1;
    public static final byte BUY = 2;
    public static final byte SELL = 3;
    public static final byte UNWRAP_GROUP = 4;
    public static final byte MANAGE_ORDER = 5;
    public static final byte SELL_INV = 6;
    public static final byte OPEN_PRODUCT_MENU = 7;

    public final static Map<Byte, InventoryUpdater> MAP;
    static {
        MAP = new HashMap<>();
        MAP.put(SWAP_CATEGORY,
                (p , inv, ID) -> SwapCategory(inv,ID));
        MAP.put(CLOSE,
            (p , inv, ID) -> p.closeInventory());
        MAP.put(SEARCH,
                (p,inv,ID)-> {
                    String[] builtInText= {
                            ""
                            ,"^^^^^^^^^^^^"
                            ,"Search items"
                            ,""
                    };
                    SignGUI.getInstance().open(p,builtInText, Material.ACACIA_SIGN
                            ,(player, lines, signLoc) -> {
                                player.sendBlockChange(signLoc, signLoc.getBlock().getBlockData());//turn back to normal
                            });
                });
        MAP.put(CATEG_NAV_NEXT,
            (p , inv, ID)-> {
                val id = inv.getItem(3 + 1).getItemMeta().getPersistentDataContainer()
                        .get(JKey.BAZAAR_COMPONENT_ID, PersistentDataType.INTEGER);
                SwapCategory(inv, id);
            });
        MAP.put(CATEG_NAV_BACK,
                (p , inv, ID)-> {
                    val id = inv.getItem(3 - 1).getItemMeta().getPersistentDataContainer()
                            .get(JKey.BAZAAR_COMPONENT_ID, PersistentDataType.INTEGER);
                    SwapCategory(inv, id);
                });
        MAP.put(UNWRAP_GROUP,
                (p, inv, id) -> UnwrapGroup(id, inv));

    }

    public static void openProductMenu(Inventory inventory, String product_name){
        Map<String, Product> products = ProductManager.getProducts();
        Product product = products.get(product_name);
        /// tulis logic lu disini
    }

    private static void UnwrapGroup(final int id, Inventory inventory){
        int[] indexes = {
                20, 21, 22, 23, 24, 25,
                29, 30, 31, 32, 33, 34,
                38, 39, 40, 41, 42, 43
        };

        Map<Integer, String> groupsID = Bazaar.getGroupsID();
        List<Product> products = ProductManager.getProduct_by_group().get(groupsID.get(id));

        Iterator<Product> productIterator = products.iterator();
        for (int i : indexes) {
            inventory.setItem(i, productIterator.next().getItem());
        }

    }

    private static void SwapCategory(Inventory inv ,int ID) {
        List<Content> contents = Bazaar.getCategories();
        int[] cs = {1, 2, 3, 4, 5};
        Content selected_content = null;

        for (Content content : contents) {
            if(content.getID()==ID){
                Arrays.stream(cs).forEach(sI -> inv.setItem(sI, null)); //gw dh mudeng sama forEach so yk what it mean
                byte index = (byte) contents.indexOf(content);
                selected_content = contents.get(index);
                for (byte i = 0; i < cs.length; i++) inv.setItem(i + 1, contents.get(
                        (index + (i - 2) + contents.size()) % contents.size()
                ).getItem());
                break;
            }
        }

        TaskID.UpdateSubcategory(selected_content.getID(), inv);
        TaskID.UpdateDecoration(selected_content, inv);
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
        ItemMeta selectedItemmeta = selectedItemStack.getItemMeta();

        selectedItemmeta.addEnchant(Enchantment.UNBREAKING,1,false);
        selectedItemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        selectedItemmeta.lore(List.of(
                        MiniMessage.miniMessage().deserialize("")
                        ,MiniMessage.miniMessage().deserialize("<!i><color:#77aa77>Selected")
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

        List<Category> SubCategory = Bazaar.getSubCategories().getOrDefault(ID, null);
        if(SubCategory==null) return;
        Iterator<Category> iterator = Bazaar.getSubCategories().get(ID).iterator();
        for (int index : indexes) {
            if(!iterator.hasNext()) return;
            inventory.setItem(index, iterator.next().getItem());
        }
    }
}
