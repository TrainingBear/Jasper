package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.Audiences;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class Logger {
    private Audience audience;
    private final Player player;

    public Logger(Player player){
        this.player = player;
        this.audience = player;
    }
    public Logger(Collection<Player> audience){
        this.player=null;
        setAudience(audience);
    }


    public void setAudience(Collection<Player> audiences){
        if (player!=null) audiences.add(player);
        audience = Audience.audience(audiences);
    }

    public void infoactionbar(String message, TagResolver... placeholder){
        Component component = MiniMessage.miniMessage().deserialize(message, placeholder);
        if(audience!=null)audience.sendActionBar(component);
    }

    public void infoSound(Sound sound) {
        if(audience!=null)audience.playSound(sound);
    }
    public void info(String message, TagResolver... placeholder){
        Component component = MiniMessage.miniMessage().deserialize(message, placeholder);
        if(audience!=null) {
            audience.sendMessage(component);
            return;
        }
        JasperProject.getPlugin().getLogger().info(PlainTextComponentSerializer.plainText().serialize(component));
    }

}
