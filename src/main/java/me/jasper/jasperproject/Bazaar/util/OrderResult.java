package me.jasper.jasperproject.Bazaar.util;

import lombok.Getter;
import lombok.val;
import me.jasper.jasperproject.Bazaar.Component.BazaarStock;
import me.jasper.jasperproject.Bazaar.Component.Order;
import me.jasper.jasperproject.Bazaar.Component.Product;
import me.jasper.jasperproject.JasperProject;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public class OrderResult {
    private final Economy eco = JasperProject.getEconomy();
    private final Type order_type;
    private BazaarStock main_shop;
    private int main_shop_amount;
    private final Map<Order, Integer> cartMap = new HashMap<>();

    public OrderResult(Type type){
        this.order_type = type;
    }

    public void execute(Player pelaku, Product product){
        for (Order order : cartMap.keySet()) {
            NamespacedKey key = product.getKey();
            int amount = cartMap.get(order);
            int final_amount = 0;
            if(this.order_type.equals(Type.BUY)){
                int capacity = maxPlayerCapacity(pelaku, product);
                final_amount = Math.min(capacity, order.getStock());
                ItemStack item = product.getProduct();
                item.setAmount(final_amount);
                pelaku.getInventory().addItem(item);
                order.fillSellOrder(pelaku, final_amount);
                continue;
            }
            /// Sell Action
            for (ItemStack e : pelaku.getInventory().getStorageContents()) {
                if (e==null) continue;
                if (key==null) if(!e.isSimilar(product.getProduct())) continue;
                if (!e.getItemMeta().getPersistentDataContainer().has(key)) continue;

                val itemAmount = e.getAmount();
                if(amount<itemAmount) {
                    e.setAmount(itemAmount- amount);
                    final_amount+=amount;
                }
                else {
                    e.setAmount(0);
                    amount-=itemAmount;
                    final_amount+=itemAmount;
                }
            }
            final_amount = Math.min(final_amount, order.getStock());
            order.fillBuyOrder(pelaku, final_amount);
        }
    }

    public void addCart(BazaarStock stock, int amount){
        main_shop_amount+=amount;
        main_shop=stock;
    }

    public void addCart(Order toko, int amount){
        cartMap.put(toko, amount);
    }

    public int getAmount(){
        int amount = 0;
        for (Integer value : cartMap.values()) {
            amount+=value;
        }
        return amount;
    }


    public float getBills(){
        return getBills(Type.SELL);
    }
    public float getBills(Type type){
        float bills = 0;
        for (Order order : cartMap.keySet()) {
            val i = cartMap.get(order);
            bills+=i*order.getOffer();
        }
        if(main_shop!=null && type.equals(Type.BUY)) bills += main_shop_amount * main_shop.getBuyOffer();
        if(main_shop!=null && type.equals(Type.SELL)) bills += main_shop_amount * main_shop.getSellOffer();
        return bills;
    }

    public List<OfflinePlayer> getBusinessMans(){
        List<OfflinePlayer> mans = new ArrayList<>();
        for (Order order : cartMap.keySet()) {
            OfflinePlayer a = Bukkit.getOfflinePlayer(order.getBusinessman());
            mans.add(a);
        }
        return mans;
    }

    public Collection<Order> getShops(){
        return cartMap.keySet();
    }

    private int maxPlayerCapacity(Player player, Product item){
        int capacity = 0;
        int maxStack = item.getProduct().getMaxStackSize();
        NamespacedKey key = item.getKey();
        for (@Nullable ItemStack content : player.getInventory().getStorageContents()) {
            if(content==null) {
                capacity+=maxStack;
                continue;
            }
            if(key==null) if(!content.isSimilar(item.getProduct())) continue;
            capacity+=maxStack-content.getAmount();
        }
        return capacity;
    }

    public enum Type{
        BUY,
        SELL
    }
}
