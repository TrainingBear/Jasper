package me.jasper.jasperproject.Util.Commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

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

    public CommandManager register(JasperCommand command){
        LiteralArgumentBuilder<CommandSourceStack> created = command.createCommand();
        plugin.getLogger().info("[JasperProject] Registering Command{}"+ created.getLiteral());

        manager.registerEventHandler(LifecycleEvents.COMMANDS, (c) ->
                c.registrar().register(created.build())
        );
        return this;
    }

//    public static CommandManager getInstance(BootstrapContext contex){
//        if(instance==null){
//            instance = new CommandManager(contex);
//        }
//        return instance;
//    }
//
//    @NotNull private LifecycleEventManager<BootstrapContext> manager;
//    private BootstrapContext bootstrapContext;
//
//
//    CommandManager(BootstrapContext contex){
//        manager = contex.getLifecycleManager();
//        bootstrapContext = contex;
//    }
//
//    public CommandManager register(JasperCommand command){
//        LiteralArgumentBuilder<CommandSourceStack> created = command.createCommand();
//        bootstrapContext.getLogger().info("[JasperProject] registering {}", created.getLiteral());
//
//        manager.registerEventHandler(LifecycleEvents.COMMANDS, (c) ->
//           c.registrar().register(created.build(), "jasper")
//        );
//        return this;
//    }
//
//
}
