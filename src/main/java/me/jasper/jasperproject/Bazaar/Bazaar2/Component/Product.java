package me.jasper.jasperproject.Bazaar.Bazaar2.Component;

import lombok.Getter;
import lombok.val;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public final class Product extends ItemStack implements Content {
    @Getter private int sell_price;
    @Getter private int buy_price;
    @Getter private int stock;
    @Getter private int demand;
    @Getter private ItemStack prototype;

    public Product(Material material){
        this(new ItemStack(material));
    }
    public Product(ItemStack item){
        super(item);
        prototype = item;
        ItemMeta meta = prototype.getItemMeta();

        meta.lore(List.of(
                text("<!i><gold>sell price: <sell_price></gold>", Placeholder.unparsed("sell_price", sell_price)),
                text("<!i><gold>buy price: <buy_price></gold>", Placeholder.unparsed("buy_price", buy_price)),
                text("<!i><dark_green>stock: <v>, demand: <v2></dark_green>",
                        Placeholder.unparsed("v", String.valueOf(stock)),
                        Placeholder.unparsed("v2", String.valueOf(demand))
                        )
        ));
    }

    public void buy(Player buyer, int ammount){
        int price = ammount*buy_price;
        val eco = JasperProject.getEconomy();
        if(!eco.has(buyer, price)){
            double morecoin = price - eco.getBalance(buyer);
            buyer.sendMessage(MessageEnum.BAZAAR.append(text("<red>You need <v> more coins to buy this item!</red>", Placeholder.unparsed("v", String.valueOf(morecoin)))));
            return;
        }

        int avaible_slot = getPlayerAvaibleSlot(buyer);
        int max_stack = this.getMaxStackSize();
        if(avaible_slot < (double) (ammount / max_stack)){
            buyer.sendMessage(MessageEnum.BAZAAR.append(text("<red>Your inventory is full!</red>")));
            return;
        }

        stock-=ammount;


    }

    private int getPlayerAvaibleSlot(Player player){
        @Nullable ItemStack[] storageContents = player.getInventory().getStorageContents();
        int i = 0;
        for (@Nullable ItemStack item : storageContents) {
            if(item==null) i++;
        }
        return i;
    }

    private Component text(String s, TagResolver... r){
        return MiniMessage.miniMessage().deserialize(s, r);
    }

    @Override
    public int getID() {
        return 6942;
    }

    @Override
    public ItemStack getItem() {
        return this;
    }
}
