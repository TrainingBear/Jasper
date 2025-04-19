package me.jasper.jasperproject.Bazaar;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.val;
import me.jasper.jasperproject.Bazaar.Component.ProductManager;
import me.jasper.jasperproject.Bazaar.Product.Product;
import me.jasper.jasperproject.Util.Commands.JasperCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BazaarCommand implements JasperCommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("Bazaar")
                .then(Commands.literal("admin")
                        .then(Commands.literal("new_product")
                                .then(Commands.argument("group", StringArgumentType.string())
                                        .suggests((commandContext, suggestionsBuilder) -> {
                                            for (String group : Bazaar.getGroups()) {
                                                suggestionsBuilder.suggest(group);
                                            }
                                            return suggestionsBuilder.buildFuture();
                                        })
                                        .then(Commands.argument("product name", StringArgumentType.string())
                                                .then(Commands.argument("namespace", StringArgumentType.string())
                                                        .suggests((s, b)->{
                                                            if(!(s.getSource().getSender()instanceof Player player)) return b.buildFuture();
                                                            for (NamespacedKey key : getCurrentItemNamespaces(player, false)) {
                                                                b.suggest(key.getNamespace()+":"+key.getKey());
                                                            }
                                                            b.suggest("null");
                                                            return b.buildFuture();
                                                        })
                                                        .executes(e -> {
                                                            if(!(e.getSource().getSender()instanceof Player player)) return  Command.SINGLE_SUCCESS;
                                                            ItemStack item = player.getInventory().getItemInMainHand();
                                                            val productName = StringArgumentType.getString(e, "product name");
                                                            val namespace = StringArgumentType.getString(e, "namespace");

                                                            Product product = new Product(item, productName, stringToNamespace(namespace));
                                                            try {
                                                                ProductManager.createProduct(
                                                                        StringArgumentType.getString(e, "group"),
                                                                        productName,
                                                                        product
                                                                );
                                                            } catch (SQLException ex) {
                                                                player.sendMessage(ex.getMessage());
                                                                throw new RuntimeException(ex);
                                                            }

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        ).executes(e -> {
                                            if(!(e.getSource().getSender()instanceof Player player)) return Command.SINGLE_SUCCESS;
                                            val productName = StringArgumentType.getString(e, "product name");
                                            ItemStack item = player.getInventory().getItemInMainHand();
                                            Product product = new Product(item, productName, (NamespacedKey) null);
                                            try {
                                                ProductManager.createProduct(
                                                        StringArgumentType.getString(e, "group"),
                                                        productName,
                                                        product
                                                );
                                            } catch (SQLException ex) {
                                                player.sendMessage(ex.getMessage());
                                                throw new RuntimeException(ex);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )

                        )
                        .then(Commands.literal("namespace")
                                .executes(e -> {
                                    if(!(e.getSource().getSender()instanceof Player player)) return Command.SINGLE_SUCCESS;
                                    getCurrentItemNamespaces(player, true);
                                    return Command.SINGLE_SUCCESS;
                                })
                        ).then(Commands.literal("remove")
                                .then(Commands.argument("product name", StringArgumentType.string())
                                        .suggests((s, b)->{
                                            for (String name : ProductManager.getProducts().keySet()) {
                                                b.suggest(name);
                                            }
                                            return b.buildFuture();
                                        })
                                        .executes(e->{
                                            try {
                                                ProductManager.removeProduct(StringArgumentType.getString(e, "product name"));
                                            } catch (SQLException ex) {
                                                e.getSource().getSender().sendMessage(ex.getMessage());
                                                ex.printStackTrace();
                                                throw new RuntimeException(ex);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        ).then(Commands.literal("change_group")
                                .then(Commands.argument("product name", StringArgumentType.string())
                                        .suggests((s, b)->{
                                            for (String name : ProductManager.getProducts().keySet()) {
                                                b.suggest(name);
                                            }
                                            return b.buildFuture();
                                        })
                                        .then(Commands.argument("new group", StringArgumentType.string())
                                                .suggests((s, b) ->{
                                                    for (String group : Bazaar.getGroups()) {
                                                        b.suggest(group);
                                                    }
                                                    return b.buildFuture();
                                                })
                                                .executes(e->{
                                                    try {
                                                        ProductManager.changeGroup(
                                                                StringArgumentType.getString(e, "product name"),
                                                                StringArgumentType.getString(e, "new group")
                                                        );
                                                    } catch (SQLException ex) {
                                                        e.getSource().getSender().sendMessage(ex.getMessage());
                                                        ex.printStackTrace();
                                                        throw new RuntimeException(ex);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )

                                )
                        )
                ).executes(e->{
                    if(!(e.getSource().getSender()instanceof Player player)) return Command.SINGLE_SUCCESS;

                    Bazaar.open(player);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private NamespacedKey stringToNamespace(@NotNull String namespace){
        String[] ns = namespace.split(":");
        NamespacedKey key = null;
        if(namespace.equals("null")){
            key = null;
        } else if (ns.length==2) {
            key = new NamespacedKey(ns[0], ns[1]);
        }
        return key;
    }

    public Set<NamespacedKey> getCurrentItemNamespaces(@NotNull Player player, boolean show_player){
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.isEmpty() || !item.hasItemMeta()) return new HashSet<>();
        Set<NamespacedKey> keys = item.getItemMeta().getPersistentDataContainer().getKeys();
        if(!show_player) return keys;
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow><displayname></yellow> <dark_green>namespaces</dark_green>:",
                Placeholder.component("displayname", item.displayName())
                ));
        for (NamespacedKey key : keys) {
            Component component = MiniMessage.miniMessage().deserialize("<click:copy_to_clipboard:"+key.getNamespace()+":"+key.getKey()+"><hover:show_text:'Click to copy!'><dark_green><namespace></dark_green>:<green><key></green></hover></click>",
                    Placeholder.unparsed("namespace", key.getNamespace()),
                    Placeholder.unparsed("key", key.getKey())
            );
            player.sendMessage(component);
        }
        return keys;
    }
}
