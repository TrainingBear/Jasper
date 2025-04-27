package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchants;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchant;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Sharpness extends Enchant {
    public Sharpness(){
        super.max_level = 5;
        super.maxPrestige = 3;
        super.baseModifier = 15f;
        prestigedModifier = baseModifier;
        modifier = prestigedModifier;
    }

    @EventHandler
    public void action(Sharpness e){
    }

    @Override
    public List<Component> getLore() {
        return List.of(
                Util.deserialize("<!i>Deal <modifier> more damage with your melee damage!",
                        Placeholder.component("modifier", Component.text(modifier+"%").color(NamedTextColor.GREEN))
                        ).color(NamedTextColor.GRAY)
        );
    }
}
