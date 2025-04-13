package me.jasper.jasperproject.Bazaar.Bazaar2;

import lombok.Getter;
import me.jasper.jasperproject.Bazaar.Bazaar2.Component.Category;
import me.jasper.jasperproject.Bazaar.Bazaar2.Component.SubCategory;
import me.jasper.jasperproject.Bazaar.BazaarEnum;
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
//    private final static Category Stone = new Category(100, Material.COBBLESTONE, deserialize("Stone"), (byte) 100, JKey.BAZAAR_SUBCATEGORY);
//    private final static Category Ore = new Category(101, Material.COAL, deserialize("Ore"), (byte) 101, JKey.BAZAAR_CATEGORY);
//    private final static List<Content> Mining1_sub = List.of(Stone, Ore);
    @Getter private final static Map<Integer, List<Content>> SubCategories = Map.of(
            1, List.of(//SLIMEFUN

            )
            ,2,List.of(//MOB LOOT
                new Category(100, Material.ROTTEN_FLESH, deserialize("<!i>Zombie"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.BONE, deserialize("<!i>Skeleton"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.SPIDER_EYE, deserialize("<!i>Spider"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.GUNPOWDER, deserialize("<!i>Creeper"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.ENDER_PEARL, deserialize("<!i>Enderman"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.BLAZE_ROD, deserialize("<!i>Blaze"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
            )
            ,3, List.of(//FARMING
                new Category(100, Material.WHEAT, deserialize("<!i>Wheat"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.CARROT, deserialize("<!i>Carrot"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.POTATO, deserialize("<!i>Potato"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.BEETROOT, deserialize("<!i>Beetroot"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.CACTUS, deserialize("<!i>Cacti"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.COCOA, deserialize("<!i>Cocoa Beans"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.SUGAR_CANE, deserialize("<!i>Sugar Cane"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.NETHER_WART, deserialize("<!i>Nether Wart"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
            )
            ,4,List.of(//MINING
                new Category(100, Material.COBBLESTONE, deserialize("<!i>Stone"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.COAL, deserialize("<!i>Coal"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.IRON_INGOT, deserialize("<!i>Iron"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.GOLD_INGOT, deserialize("<!i>Gold"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.DIAMOND, deserialize("<!i>Diamond"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
                ,new Category(100, Material.NETHERITE_INGOT, deserialize("<!i>Netherite"), (byte) 100, JKey.BAZAAR_SUBCATEGORY)
            )
    );

    ///                     CATEGORIES
//    private final static Category Slimefun = new Category(1, Material.MAGMA_CREAM, deserialize("<!i><color:#09ff00>Slimefun"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
//    private final static Category MobLoot= new Category(2, Material.ROTTEN_FLESH, deserialize("<!i><color:#b34a00>Mob Loot"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
//    private final static Category Farming= new Category(3, Material.WHEAT, deserialize("<!i><color:#FFD700>Farming"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
//    private final static Category Mining= new Category(4, Material.GOLDEN_PICKAXE, deserialize("<!i><color:#78f5f5>Mining"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
//    private final static Category Mining5= new Category(5, Material.GOLDEN_PICKAXE, deserialize("Mining5"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY);
    @Getter private final static List<Content> Categories = List.of(
        new Category(1, Material.MAGMA_CREAM, deserialize(BazaarEnum.SLIMEFUN_CATEG.name()), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY)
        ,new Category(2, Material.ROTTEN_FLESH, deserialize(BazaarEnum.MOB_LOOT_CATEG.name()), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY)
        ,new Category(3, Material.WHEAT, deserialize(BazaarEnum.FARMING_CATEG.name()), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY)
        ,new Category(4, Material.GOLDEN_PICKAXE, deserialize(BazaarEnum.MINING_CATEG.name()), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY)
        ,new Category(5, Material.GLOWSTONE_DUST, deserialize("Magical/Oddities //Nanti, blm gw cari colorny"), TaskID.SWAP_CATEGORY, JKey.BAZAAR_CATEGORY)
);



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
