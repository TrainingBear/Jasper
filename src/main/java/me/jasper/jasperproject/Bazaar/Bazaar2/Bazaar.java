package me.jasper.jasperproject.Bazaar.Bazaar2;

import lombok.Getter;
import me.jasper.jasperproject.Bazaar.Bazaar2.Component.Category;
import me.jasper.jasperproject.Bazaar.Bazaar2.Component.SubCategory;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.ContainerMenu.Container;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Bazaar {
    private static Container container;
    private static Inventory INSTANCE(Player player){
        if(container==null){
            container = new Container(player, MiniMessage.miniMessage().deserialize("Bazaar"), layout);
            container.addContent(Categories);
            container.addContent(BORDER);
            container.addContent(POINTER);
            container.load();
            TaskID.UpdateDecoration(Mining3, container.getContainer());
            TaskID.UpdateSubcategory(Mining3.getID(), container.getContainer());
        }
        return container.getContainer();
    }
    ///                     GUI UTIL
    private final static Border BORDER = new Border(0, Material.BLACK_STAINED_GLASS_PANE, false);
    public final static Border POINTER = new Border(7, Material.LIME_STAINED_GLASS_PANE, false, deserialize(""), false);
    private final static Border DEFAULT_DECORATION = new Border(8, Material.LIME_STAINED_GLASS_PANE, false);
    private final static Border Mining1_DECORATION = new Border(8, Material.GRAY_STAINED_GLASS, false);
    @Getter private final static Map<Integer, Border> DecorationMap;
    static {
        DecorationMap = new HashMap<>();
        DecorationMap.put(0, DEFAULT_DECORATION);
        DecorationMap.put(1, Mining1_DECORATION);
    }


    ///                     SUB CATEGORIES
    private final static Category Stone = new Category(100, Material.COBBLESTONE, deserialize("Stone"), (byte) 100, JKey.BAZAAR_SUBCATEGORY);
    private final static Category Ore = new Category(101, Material.COAL, deserialize("Ore"), (byte) 101, JKey.BAZAAR_CATEGORY);
    private final static List<Content> Mining1_sub = List.of(Stone, Ore);
    @Getter private final static Map<Integer, List<Content>> SubCategories = Map.of(
            1, Mining1_sub
    );

    ///                     CATEGORIES
    /// gini cara buatnya gini
    private final static Category Slimefun =new Category(1, Material.MAGMA_CREAM, deserialize("Mining1"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
    private final static Category Mining2= new Category(2, Material.STONE_PICKAXE, deserialize("Mining2"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
    private final static Category Mining3= new Category(3, Material.GOLDEN_PICKAXE, deserialize("Mining3"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
    private final static Category Mining4= new Category(4, Material.GOLDEN_PICKAXE, deserialize("Mining4"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
    private final static Category Mining5= new Category(5, Material.GOLDEN_PICKAXE, deserialize("Mining5"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
    @Getter private final static List<Content> Categories = List.of(Slimefun, Mining2, Mining3, Mining4, Mining5);



    private static int[][] layout = {
            {0, 0, 1, 2, 3, 4, 5, 0, 0},
            {0, 0, 0, 0, 7, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    ///         MAIN METHOD
    public static void open(Player player){
        player.openInventory(INSTANCE(player));
    }

    private static Component deserialize(String s, TagResolver... t){
        return MiniMessage.miniMessage().deserialize(s, t);
    }

}
