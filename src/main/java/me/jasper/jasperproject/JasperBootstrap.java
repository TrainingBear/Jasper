package me.jasper.jasperproject;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.jasper.jasperproject.Animation.PaperAnimationCommand;
import me.jasper.jasperproject.Commands.CommandManager;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class JasperBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {

        LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(
                    Commands.literal("new-command-inBoostrap")
                            .executes(ctx -> {
                                ctx.getSource().getSender().sendPlainMessage("some message");
                                return Command.SINGLE_SUCCESS;
                            })
                            .build(),
                    "some bukkit help description string",
                    List.of("an-aliases-withbooss")
            );
        });

//        CommandManager.getInstance(context).register(new PaperAnimationCommand());
    }
}

