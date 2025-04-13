package me.jasper.jasperproject.Bazaar.Bazaar2;

import lombok.Getter;
import me.jasper.jasperproject.Bazaar.Bazaar2.Component.Category;
import me.jasper.jasperproject.Bazaar.Bazaar2.Component.Items;
import me.jasper.jasperproject.Bazaar.BazaarEnum;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.ContainerMenu.Container;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;

public final class Bazaar {
    private static Container container;
  
    ///                     GUI UTIL
    private final static Border BLANK = new Border(-1, Material.AIR, false);
    private final static Border BORDER = new Border(0, Material.BLACK_STAINED_GLASS_PANE, false);
    public final static Border POINTER = new Border(7, Material.LIME_STAINED_GLASS_PANE, false
            , miniMsgDese("<!i><green>Selected category"), false);
    public final static Category CATEG_NAVI_NEXT = new Category(13, Material.SPECTRAL_ARROW,miniMsgDese("<!i>Next") ,TaskID.CATEG_NAV_NEXT);
    public final static Category CATEG_NAVI_BACK = new Category(14, Material.SPECTRAL_ARROW,miniMsgDese("<!i>Back") ,TaskID.CATEG_NAV_BACK);

    private final static Category TITLE_SIGN = new Category(12, Material.ACACIA_HANGING_SIGN
            , (Component) BazaarEnum.TITLE_NAME.get(),TaskID.TITLE,List.of(
                miniMsgDese("<!i><yellow>Worldwide bazaar")
                ,miniMsgDese("")
                ,miniMsgDese("<!i>"+BazaarEnum.CLICK_TEXT.get()+" <gray>to search thing")
    ));
    private final static Category CLOSE = new Category(11, Material.BARRIER
            , (Component) BazaarEnum.CLOSE_ITEM.get(),TaskID.CLOSE,List.of(
            MiniMessage.miniMessage().deserialize("")
            ,MiniMessage.miniMessage().deserialize("<!i><b><color:#ff261f>Click</color></b> <red>to close")
    ));

    private final static Border CATEG_DEFAULT_DECO = new Border(8, Material.COBWEB, false);
    private final static Border SLIMEFUN_CATEG_DECO = new Border(8, Material.LIME_STAINED_GLASS_PANE, false);
    private final static Border MOBLOOT_CATEG_DECO = new Border(8, Material.ORANGE_STAINED_GLASS_PANE, false);
    private final static Border FARMING_CATEG_DECO = new Border(8, Material.YELLOW_STAINED_GLASS_PANE, false);
    private final static Border MINING_CATEG_DECO = new Border(8, Material.LIGHT_BLUE_STAINED_GLASS_PANE, false);
    private final static Border FISHING_CATEG_DECO = new Border(8, Material.BLUE_STAINED_GLASS_PANE, false);
    private final static Border MAGICAL_CATEG_DECO = new Border(8, Material.PINK_STAINED_GLASS_PANE, false);

    @Getter private final static Map<Integer, Border> DecorationMap;
    static {
        DecorationMap = Map.of(
                0, CATEG_DEFAULT_DECO
                ,1, SLIMEFUN_CATEG_DECO
                ,2,MOBLOOT_CATEG_DECO
                ,3,FARMING_CATEG_DECO
                ,4,MINING_CATEG_DECO
                ,5,FISHING_CATEG_DECO
                ,6,MAGICAL_CATEG_DECO
        );
    }

    ///                     ITEMS


    ///                     SUB CATEGORIES
    @Getter private final static Map<Integer, List<Content>> SubCategories = Map.of(
            1, List.of(

            )
            ,2,List.of(//MOB LOOT
                new Category(100, Material.ROTTEN_FLESH, miniMsgDese("<!i>Zombie"), (byte) 100)
                ,new Category(100, Material.BONE, miniMsgDese("<!i>Skeleton"), (byte) 100)
                ,new Category(100, Material.SPIDER_EYE, miniMsgDese("<!i>Spider"), (byte) 100)
                ,new Category(100, Material.GUNPOWDER, miniMsgDese("<!i>Creeper"), (byte) 100)
                ,new Category(100, Material.ENDER_PEARL, miniMsgDese("<!i>Enderman"), (byte) 100)
                ,new Category(100, Material.BLAZE_ROD, miniMsgDese("<!i>Blaze"), (byte) 100)
            )
            ,3, List.of(//FARMING
                new Category(100, Material.WHEAT, miniMsgDese("<!i>Wheat"), (byte) 100)
                ,new Category(100, Material.CARROT, miniMsgDese("<!i>Carrot"), (byte) 100)
                ,new Category(100, Material.POTATO, miniMsgDese("<!i>Potato"), (byte) 100)
                ,new Category(100, Material.BEETROOT, miniMsgDese("<!i>Beetroot"), (byte) 100)
                ,new Category(100, Material.CACTUS, miniMsgDese("<!i>Cacti"), (byte) 100)
                ,new Category(100, Material.COCOA_BEANS, miniMsgDese("<!i>Cocoa Beans"), (byte) 100)
                ,new Category(100, Material.SUGAR_CANE, miniMsgDese("<!i>Sugar Cane"), (byte) 100)
                ,new Category(100, Material.NETHER_WART, miniMsgDese("<!i>Nether Wart"), (byte) 100)
            )
            ,4,List.of(//MINING
                new Category(100, Material.COBBLESTONE, miniMsgDese("<!i>Stone"), (byte) 100)
                ,new Category(100, Material.COAL, miniMsgDese("<!i>Coal"), (byte) 100)
                ,new Category(100, Material.IRON_INGOT, miniMsgDese("<!i>Iron"), (byte) 100)
                ,new Category(100, Material.GOLD_INGOT, miniMsgDese("<!i>Gold"), (byte) 100)
                ,new Category(100, Material.DIAMOND, miniMsgDese("<!i>Diamond"), (byte) 100)
                ,new Category(100, Material.NETHERITE_INGOT, miniMsgDese("<!i>Netherite"), (byte) 100)
            )
            ,5,List.of(//FISHING
                    new Category(100, Material.COD, miniMsgDese("<!i>Cod"), (byte) 100)
                    ,new Category(100, Material.SALMON, miniMsgDese("<!i>Salmon"), (byte) 100)
                    ,new Category(100, Material.PUFFERFISH, miniMsgDese("<!i>Pufferfish"), (byte) 100)
                    ,new Category(100, Material.TROPICAL_FISH, miniMsgDese("<!i>Tropical Fish"), (byte) 100)
                    ,new Category(100, Material.SPONGE, miniMsgDese("<!i>Sponge"), (byte) 100)
                     ,new Category(100, Material.LILY_PAD, miniMsgDese("<!i>Lily Pad"), (byte) 100)
            )
            ,6,List.of(//MAGICAL
            )
    );

    ///                     CATEGORIES
    @Getter private final static List<Content> Categories = List.of(
        new Category(1, Material.MAGMA_CREAM, miniMsgDese((String) BazaarEnum.SLIMEFUN_CATEG.get()), TaskID.SWAP_CATEGORY)
        ,new Category(2, Material.ROTTEN_FLESH, miniMsgDese((String) BazaarEnum.MOB_LOOT_CATEG.get()), TaskID.SWAP_CATEGORY)
        ,new Category(3, Material.WHEAT, miniMsgDese((String)BazaarEnum.FARMING_CATEG.get()), TaskID.SWAP_CATEGORY)
        ,new Category(4, Material.GOLDEN_PICKAXE, miniMsgDese((String) BazaarEnum.MINING_CATEG.get()), TaskID.SWAP_CATEGORY)
        ,new Category(5, Material.AXOLOTL_BUCKET, miniMsgDese((String) BazaarEnum.FISHING_CATEG.get()), TaskID.SWAP_CATEGORY)
        ,new Category(6, Material.GLOWSTONE_DUST, miniMsgDese((String) BazaarEnum.MAGICAL_CATEG.get()), TaskID.SWAP_CATEGORY)

    );



    private static int[][] layout = {
            {14, 1, 2, 3, 4, 5, 13, 0, 11},
            {0, 0, 0, 7, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 12, 0, 0, 0, 0},
    };
    ///         MAIN METHOD
    public static void open(Player player){
        Inventory inventory = INSTANCE(player);
        player.openInventory(INSTANCE(player));
    }

    private static Component miniMsgDese(String s, TagResolver... t){
        return MiniMessage.miniMessage().deserialize(s, t);
    }
    private static Inventory INSTANCE(Player player){
        if(container==null){
            container = new Container(player, miniMsgDese(BazaarEnum.TITLE_STRING.get()+" <yellow>Worldwide bazaar"), layout);
            container.addContent(Categories);
            container.addContent(POINTER);

            container.addContent(CATEG_NAVI_NEXT);
            container.addContent(CATEG_NAVI_BACK);
            container.addContent(TITLE_SIGN);
            container.addContent(CLOSE);

            container.addContent(BLANK);
            container.addContent(BORDER);

            container.load();
            TaskID.UpdateDecoration(Categories.get(0), container.getContainer());
            TaskID.UpdateSubcategory(Categories.get(0).getID(), container.getContainer());
        }
        return container.getContainer();
    }

}
