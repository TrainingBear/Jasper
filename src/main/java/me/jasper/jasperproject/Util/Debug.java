package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.Animation.Animation;
import me.jasper.jasperproject.Bazaar.Bazaar2.Bazaar;
import me.jasper.jasperproject.Bazaar.Bazaar2.Component.ProductManager;
import me.jasper.jasperproject.Bazaar.Bazaar2.Product.BazaarItemsDatabase;
import me.jasper.jasperproject.Bazaar.Bazaar2.Product.Product;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.ContainerMenu.Container;
import me.jasper.jasperproject.Util.ContainerMenu.ContentListener;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Debug implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        switch (strings[0]){
            case "save" ->{
                if(strings[1].isEmpty()) return false;
                String name = strings[1];
                String key = null;
                if(strings.length==3){
                    key = strings[2];
                }

                ItemStack item = player.getInventory().getItemInMainHand();
                Product product = new Product(item, name, key);

                try {
                    ProductManager.createProduct(name, product);
                } catch (SQLException e) {
                    for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                        player.sendMessage(stackTraceElement.getClassLoaderName());
                    }
                    e.printStackTrace();
                    player.sendMessage(e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            case "get" ->{
                if(strings[1].isEmpty()) return false;
                String name = strings[1];

                try {
                    Product product = BazaarItemsDatabase.getProduct(name);
                    assert product != null;
                    player.sendMessage(product.getKey().getKey());

                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                    player.sendMessage(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

}
