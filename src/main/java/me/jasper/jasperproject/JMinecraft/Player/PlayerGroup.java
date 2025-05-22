package me.jasper.jasperproject.JMinecraft.Player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class PlayerGroup {
    @Setter private JPlayer leader;
    private final Set<JPlayer> members = new HashSet<>();

    public PlayerGroup(Player player){
        this(JPlayer.getJPlayer(player));
    }
    public PlayerGroup(JPlayer player){
        this.leader = player;
        members.add(this.leader);
    }

    public void addMember(Collection<JPlayer> player){
        members.addAll(player);
    }
    public void addMember(JPlayer player){
        members.add(player);
    }

    public void kickMember(JPlayer player){
        members.remove(player);
    }
}
