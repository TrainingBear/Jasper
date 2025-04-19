package me.jasper.jasperproject.Bazaar.Product;

import lombok.Getter;

import java.io.Serializable;

public class Curve implements Serializable {
    /// Quantity
    protected int q;
    /// Price
    @Getter protected float p = 0.1f;

    public Curve(int stock, float price){
        this.q = stock;
        this.p = price;
    }

    public void setProfit(float price){
        this.p+=price;
    }

    public void addStock(int ammount){
        this.q+=ammount;
    }
    public void decreasePrice(float price){
        this.p-=price;
    }

    public void decreaseQuantity(int ammount){
        this.q-=ammount;
    }

}
