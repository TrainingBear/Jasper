package me.jasper.jasperproject.Bazaar.Component;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public final class BazaarStock implements Serializable {
    @Serial
    private static final long serialVersionUID = -8870074578238586547L;
    private float bank;
    private int stock;
    private float sellOffer;
    private float buyOffer;

    public BazaarStock(){
        this(0,0,0);
    }

    public BazaarStock(float sell, float buy, float bank){
        this(0, sell, buy, bank);
    }
    public BazaarStock(int stock, float sell, float buy, float bank){
        this.bank = bank;
        this.sellOffer = sell;
        this.buyOffer = buy;
        this.stock = stock;
    }

    public void updateSellOffer(float new_value){
        this.sellOffer = new_value;
    }

    public void updateBuyOffer(float new_value){
        this.buyOffer = new_value;
    }

    public void fillStock(int value){
        this.bank-=value*this.buyOffer;
//        if(bank<0) return;
        this.stock+=value;
    }
    public int sellStock(int amount){
        this.stock-=amount;
        return amount;
    }
}
