package me.jasper.jasperproject.JasperItem.Product;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Warper;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.Material;

public class WarpGateway extends EndGateway implements Factory {
    @Getter
    private final static WarpGateway instance = new WarpGateway();
    public WarpGateway(){
        super();
        this.setDefaultItem_name("Warp gateway");
        this.setRarity(Rarity.LEGENDARY);
        this.getAbilities().add(new Warper(20,20));
    }

    @Override
    public Jitem create() {
        return this;
    }
}
