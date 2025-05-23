package me.jasper.jasperproject.Util.Commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class CommandManager {
    private static CommandManager instance;

    public static CommandManager getInstance(){
        if(instance==null){
            instance = new CommandManager();
        }
        return instance;
    }

    @NotNull private LifecycleEventManager<Plugin> manager;
    private Plugin plugin;


    CommandManager(){
        plugin = JasperProject.getPlugin();
        manager = plugin.getLifecycleManager();
    }

    public CommandManager register(@NotNull JasperCommand command, String description, Collection<String> aliases){
        LiteralArgumentBuilder<CommandSourceStack> created = command.createCommand();
        plugin.getLogger().info("[JasperProject] Registering Command{}"+ created.getLiteral());

        manager.registerEventHandler(LifecycleEvents.COMMANDS, (c) ->
                c.registrar().register(created.build(), description, aliases)
        );
        return this;
    }

    public CommandManager register(@NotNull JasperCommand command, Collection<String> alias){
        return register(command, null, alias);
    }
    public CommandManager register(@NotNull JasperCommand command){
       return register(command, null, Collections.emptyList());
    }
}
