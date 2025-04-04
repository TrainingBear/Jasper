package me.jasper.jasperproject.Commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandManager {
    static CommandManager instance;

    public static CommandManager getInstance(){
        if(instance==null){
            instance = new CommandManager();
        }
        return instance;
    }

    @NotNull private LifecycleEventManager<Plugin> manager;

    CommandManager(){
        manager = JasperProject.getPlugin().getLifecycleManager();
    }



    public void register(JasperCommand command){
        LiteralArgumentBuilder<CommandSourceStack> created = command.createCommand();
        JasperProject.getPlugin().getLogger().info("[JasperProject] registering "+created.getLiteral());
        manager.registerEventHandler(LifecycleEvents.COMMANDS, (c) ->
           c.registrar().register(created.build(), "jasper")
        );
    }
}
