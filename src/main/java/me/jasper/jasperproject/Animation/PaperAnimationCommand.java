package me.jasper.jasperproject.Animation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.sk89q.worldedit.regions.Region;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import me.jasper.jasperproject.Commands.JasperCommand;
import me.jasper.jasperproject.FileConfiguration.Configurator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.Items;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.enginehub.linbus.stream.token.LinToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PaperAnimationCommand implements JasperCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("animate")
                .then(Commands.literal("edit")
                        .then(Commands.argument("Animation name", StringArgumentType.string()).suggests(
                                (context, builder) -> {
                                    if(!(context.getSource().getSender() instanceof Player player)) return builder.buildFuture();
                                    return Animation.getOwnerAnimations(player.getName(), builder);
                                })
                                .then(Commands.literal("add_frame")
                                        .then(Commands.argument("Frame name",StringArgumentType.string())
                                                .executes(contex ->
                                                {
                                                    if(!(contex.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                                                    final String animation_name = StringArgumentType.getString(contex, "Animation name");
                                                    final String frame_name = StringArgumentType.getString(contex, "Frame name");

                                                    return Animation.addFrame(player, animation_name, frame_name);
                                                }))
                                ).then(Commands.literal("add_member")
                                        .then(Commands.argument("member", StringArgumentType.greedyString())
                                                .executes(c -> {
                                                    if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                                                    final String animation_name = StringArgumentType.getString(c, "Animation name");
                                                    final String member = StringArgumentType.getString(c, "member");
                                                    return Animation.addMember(player, animation_name, member);
                                                }))

                                ).then(Commands.literal("load")
                                        .executes( c -> {
                                            if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                                            return Animation.loadFrame(player, StringArgumentType.getString(c, "Animation name"));
                                        })
                                ).then(Commands.literal("clear").executes(c -> {
                                    if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                                    return Animation.clearFrame(player, StringArgumentType.getString(c, "Animation name"));})
                                ).then(Commands.literal("delete")
                                        .then(Commands.argument("frame name", StringArgumentType.string())
                                                .executes(c ->{
                                                    if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                                                    return Animation.deleteFrame(player, StringArgumentType.getString(c, "Animation name"), StringArgumentType.getString(c, "frame name"));
                                                })
                                        )

                                ).then(Commands.literal("setFPS")
                                        .then(Commands.argument("FPS", FloatArgumentType.floatArg(0.1f, 20f)))
                                        .executes(c -> {
                                            if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                                            return Animation.setFPS(player, StringArgumentType.getString(c, "Animation name"), FloatArgumentType.getFloat(c, "FPS"));
                                        })

                                ).then(Commands.literal("setRegion")
                                        .executes(c -> {
                                            if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                                            return Animation.setRegion(player, StringArgumentType.getString(c, "Animation name"), Animator.getRegions().get(player.getUniqueId()));
                                        })

                                ).then(Commands.literal("setLocation")
                                        .then(Commands.argument("X", IntegerArgumentType.integer())
                                                .then(Commands.argument("Y", IntegerArgumentType.integer())
                                                        .then(Commands.argument("Z", IntegerArgumentType.integer())
                                                                .executes(c -> {
                                                                    if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                                                                    return Animation.setLocation(player, StringArgumentType.getString(c, "Animation name"),
                                                                            new Location(player.getWorld(),
                                                                                    IntegerArgumentType.getInteger(c, "X"),
                                                                                    IntegerArgumentType.getInteger(c, "Y"),
                                                                                    IntegerArgumentType.getInteger(c, "Z"))
                                                                    );

                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                ).then(Commands.literal("create")
                        .then(Commands.argument("Animation name", StringArgumentType.word())
                                .executes(c -> {
                                    if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                                    return Animation.createNew(
                                            player,
                                            StringArgumentType.getString(c, "Animation name"),
                                            Animator.getRegions().get(player.getUniqueId()));
                                }))
                ).then(Commands.literal("play").then(Commands.argument("Animation name", StringArgumentType.word())
                        .suggests((c,b) -> {
                            if(!(c.getSource().getSender() instanceof Player player)) return b.buildFuture();
                            return Animation.getOwnerAnimations(player.getName(), b);
                        })
                        .executes(c -> {
                            if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                            return Animation.play(
                                    player,
                                    StringArgumentType.getString(c, "Animation name"));
                        }))
                ).then(Commands.literal("stop").then(Commands.argument("Animation name", StringArgumentType.word())
                        .suggests((c,b) -> {
                            if(!(c.getSource().getSender() instanceof Player player)) return b.buildFuture();
                            return Animation.getOwnerAnimations(player.getName(), b);
                        })
                        .executes(c -> {
                            if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                            return Animation.stop(
                                    player,
                                    StringArgumentType.getString(c, "Animation name"));
                        }))
                ).then(Commands.literal("update").then(Commands.argument("Animation name", StringArgumentType.word())
                        .suggests((c,b) -> {
                            if(!(c.getSource().getSender() instanceof Player player)) return b.buildFuture();
                            return Animation.getOwnerAnimations(player.getName(), b);
                        })
                        .executes(c -> {
                            if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                            return Animation.update(
                                    player,
                                    StringArgumentType.getString(c, "Animation name"));
                        }))
                ).then(Commands.literal("wand")
                        .executes(c -> {
                            if(!(c.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                            Items.animate_wannd.send(player);
                            return Command.SINGLE_SUCCESS;
                        })
                );
    }


}
