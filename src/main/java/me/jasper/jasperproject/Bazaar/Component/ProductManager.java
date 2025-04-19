package me.jasper.jasperproject.Bazaar.Component;

import lombok.Getter;
import me.jasper.jasperproject.Bazaar.Bazaar;
import me.jasper.jasperproject.Bazaar.Product.BazaarDatabase;
import me.jasper.jasperproject.Bazaar.Product.Product;
import me.jasper.jasperproject.JasperProject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public abstract class ProductManager {
    ///  String = name, Product = contents
    @Getter private static Map<String, Product> products = new HashMap<>();

    ///  String = groub, List<> = contents</>
    @Getter private static Map<String, List<Product>> product_by_group = new HashMap<>();

    public static void init() throws SQLException, IOException {
        products = BazaarDatabase.getProducts();
        for (String name : products.keySet()) {
            String category = BazaarDatabase.getCategory(name);
            product_by_group.computeIfAbsent(category, k -> new ArrayList<>()).add(products.get(name));
        }
    }

    public static void init_GroupID(){
    }

    public static void createProduct(String group, String name, @NotNull Product product) throws SQLException {
        if(!Bazaar.getGroups().contains(group)) return;
        BazaarDatabase.newProduct(group, name, product.serialize());
        products.put("name", product);
        product_by_group.computeIfAbsent(group, k->new ArrayList<>()).add(product);
    }

    public static void removeProduct(String name) throws SQLException {
        BazaarDatabase.remove(name);
    }

    public static void changeGroup(String item, String new_group) throws SQLException {
        BazaarDatabase.setCategory(item, new_group);
    }

    public static void saveAll() throws SQLException {
        JasperProject plugin = JasperProject.getPlugin();
        long start = System.currentTimeMillis();
        for (String name : products.keySet()) {
            long last = System.currentTimeMillis();
            byte[] bytes = products.get(name).serialize();
            BazaarDatabase.saveProduct("name", bytes);
            long time_took = (System.currentTimeMillis() - last);
            plugin.getLogger().info("[Bazaar] saving "+name+" took "+time_took+" ms");
        }
        long time_took = (System.currentTimeMillis() - start);
        plugin.getLogger().info("[Bazaar] All product has been saved! took "+time_took+" ms");
    }

}
