package me.jasper.jasperproject.JasperItem.ItemAttributes.Enchants;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Enchant;
import org.bukkit.event.EventHandler;

public class Sharpness extends Enchant {
    public Sharpness(){
        super.max_level = 5;
        super.maxPrestige = 3;
        super.baseModifier = 15f;
        super.lore = "<!i>Deal <modifier> more damage with your melee damage!";
    }

    @EventHandler
    public void action(Sharpness e){

    }
}
