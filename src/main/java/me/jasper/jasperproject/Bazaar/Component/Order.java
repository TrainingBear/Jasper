package me.jasper.jasperproject.Bazaar.Component;

import java.io.Serializable;
import java.util.UUID;

public class Order extends Curve implements Serializable {
    protected float bank;
    protected int goods;
    protected final int ID;
    protected UUID businessman;

    public Order(Curve curve, UUID businessman, int goods, float bank, int index) {
        super(curve.q, curve.p);
        this.bank = bank;
        this.goods = goods;
        this.ID = index;
        this.businessman = businessman;
    }

    public void setProfit(float n){
        bank+=n;
    }

    public void setGoods(int n){
        goods+=n;
    }

}
