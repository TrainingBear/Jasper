package me.jasper.jasperproject.JMinecraft.Loot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Loot {
    private long total_weight;
    public final Map<String, Long> loot = new HashMap<>();
    public void addLoot(String loot, long weight){
        total_weight+=weight;
        this.loot.put(loot, weight);
    }

    /// @return return a products with amount
    public Map<String, Integer> roll(){
        return roll(1);
    }
    public Map<String, Integer> roll(float multiplier){
        Map<String, Integer> loots = new HashMap<>();
        for (String s : loot.keySet()) {
            long weight = (long) (loots.get(s)+(loots.get(s)*multiplier));
            if(weight>total_weight){
                long count = weight / total_weight;
                loots.put(s, (int) (loots.getOrDefault(s, 0)+ count));
                weight = weight- (total_weight*count);
            }
            long rolled = ThreadLocalRandom.current().nextLong(total_weight)+1;
            if(weight >= rolled) loots.put(s,loots.getOrDefault(s, 0)+1);
        }
        return loots;
    }

    public void getLoot(Inventory... killer){
        for (Inventory player : killer) {
            for (String s : roll().keySet()) {
                /// PRODUCTMAP.get(s).sendPlayer
            }
        }
    }

    public float getChance(String product){
        return (float) total_weight / (float) loot.get(product);
    }

    public void setChance(String product, float persentase){
        long weight = (long) (Math.max(total_weight, 100) *(persentase/100f));
        loot.put(product, weight);
    }
}
