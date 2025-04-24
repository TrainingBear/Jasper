package me.jasper.jasperproject.Bazaar.Component;

import lombok.Getter;
import lombok.val;
import me.jasper.jasperproject.Bazaar.util.OrderException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Order implements Serializable {
    /**
     * DIGUNAIN BUAT SELL/BUY ORDER
     * bank = buat store hasil jualan / modal untuk membeli barang.
     * goods = buat store jumlah barang terbeli / total barang yang terjual.
     * max = menunjukan total barang yang akan di jual atau di beli.
     * offer = menunjukan harga barang/pcs
     * */
    private float bank;
    protected int goods;
    protected int max;
    protected float offer;
    private final int ID;
    private @Nullable UUID businessman; /// pelaku usaha

    public Order(@Nullable UUID businessman, int amount, float offer, int orderID){
        this.ID = orderID;
        this.businessman = businessman;
        this.max = amount;
        this.offer = offer;
    }

    public static Order createSellOrder(int orderID, Player businessman, int sell_amount, float offer){
        return new Order(businessman.getUniqueId(), sell_amount, offer, orderID);
    }

    public static Order createBuyOrder(int orderID, Player businessman, int buy_amount, float offer) throws OrderException{
        val price = buy_amount * offer;
        if(!Product.hasPurse(businessman, price)) throw new OrderException("Business Man has no money :(");
        Order order = new Order(businessman.getUniqueId(), buy_amount, offer, orderID);
        order.bank = price;
        return order;
    }

    public int getStock(){
        return max - goods;
    }

    /**
     *
     * @param amount
     * @return return total price (amount * this order buy offer)
     */
    public float fillBuyOrder(int amount){
        this.goods += amount;
        val price = amount * this.offer;
        this.bank -= price;
        return price;
    }
    public boolean fillSellOrder(Player buyer, int amount){
        val price = amount * this.offer;
        if(Product.withdraw(buyer, price)) return false;
        this.goods+=amount;
        this.bank+=price;
        return true;
    }

    public float withdrawBank(){
        return withdrawBank(bank);
    }
    public float withdrawBank(float value){
        return this.bank-value;
    }

    /**
     *
     * @return all supply
     */
    public int claimSupply(){
        return claimSupply(getStock());
    }
    public int claimSupply(int value){
        if(getStock() > value) return 0;
        max-=value;
        return value;
    }

    public int claimGoods(){
        return claimGoods(goods);
    }
    public int claimGoods(int amount){
        if(goods > amount) return 0;
        goods-=amount;
        return max-=amount;
    }
    public boolean isClaimed(boolean isSellOrder){
        if(isSellOrder) return goods >= max && bank <=0;
        return max <= 0 && goods <= 0;
    }
    public boolean isAvailable(){
        return getStock() > 0;
    }
}
