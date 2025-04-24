package me.jasper.jasperproject.Bazaar.util;

import lombok.Getter;
import me.jasper.jasperproject.Bazaar.Component.BazaarStock;
import me.jasper.jasperproject.Bazaar.Component.Order;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class OrderResult {
    private final Type order_type;
    private int item = 0;
    private float bills = 0;
    private BazaarStock main_shop;
    private final Map<UUID, Order> cartMap = new HashMap<>();

    public OrderResult(Type type){
        this.order_type = type;
    }

    public void execute(Player pelaku, Collection<Order> orders){
        switch (order_type){
            case SELL -> {

            }
            case BUY -> {

            }
        }
    }

    public void addCart(BazaarStock stock, int amount){
        switch (order_type){
            case BUY -> bills+=stock.getSellOffer()*amount;
            case SELL -> bills+=stock.getBuyOffer()*amount;
        }
        item+=amount;
        main_shop=stock;
    }

    public void addCart(UUID pemilik_toko, Order toko, int amount){
        cartMap.put(pemilik_toko, toko);
        bills+=toko.getOffer()*amount;
        item+=amount;
    }

    public Set<UUID> getBusinessMans(){
        return cartMap.keySet();
    }

    public Collection<Order> getShops(){
        return cartMap.values();
    }

    public enum Type{
        BUY,
        SELL
    }
}
