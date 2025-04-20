package me.jasper.jasperproject.TrashCan.Old_Bazaar.Trash;

import lombok.Setter;
import me.jasper.jasperproject.Bazaar.Component.Curve;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class Demand {
    @Setter ItemStack product;
    Map<UUID, Curve> curve = new HashMap<>();

    public void createDemand(Player player, int quantity, float price){
        Curve curve= this.curve.putIfAbsent(player.getUniqueId(), new Curve(quantity, price));

    }

    public Curve getCurve(UUID id){
        return curve.get(id);
    }

    public @Nullable UUID getBestOffer(){
        if(curve.isEmpty()){
            return null;
        }
        UUID holder = null;
        float price = Float.MIN_VALUE;
        for (UUID uuid : curve.keySet()) {
//            float j = curve.get(uuid).p;
//            if(j > price) {
//                holder = uuid;
//                price = j;
//            }
        }
        return holder;
    }

}
