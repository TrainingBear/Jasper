package Old_Bazaar.Trash;

import lombok.Setter;
import me.jasper.jasperproject.Bazaar.Component.Curve;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class Supply {
    @Setter ItemStack product;
    Map<UUID, Curve> curve = new HashMap<>();

    public void createOffer(Player player, int stock, float price){
        curve.put(player.getUniqueId(), new Curve(stock, price));
    }

    public @Nullable UUID getBestPrice(){
        if(curve.isEmpty()) return null;
        UUID holder = null;
        float price = Float.MAX_VALUE;

        for (UUID id : curve.keySet()) {
//            float p = curve.get(id).p;
//            if(p < price){
//                holder = id;
//                price = p;
//            };
        }
        return holder;
    }

    public Curve getCurve(UUID player){
        return curve.get(player);
    }
}
