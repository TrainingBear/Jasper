package me.jasper.jasperproject.Bazaar.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.Bazaar.Bazaar;
import me.jasper.jasperproject.Bazaar.Component.Product;
import me.jasper.jasperproject.JasperProject;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public abstract class ProductManager {
    ///  String = name, Product = contents
    @Setter(AccessLevel.PROTECTED)
    @Getter private static Map<String, Product> productMap;

    ///  String = groub, List<> = contents</>
    @Setter(AccessLevel.PROTECTED)
    @Getter private static Map<String, List<Product>> groupedProduct;

    public static void createProduct(String group, String name, @NotNull Product product) throws SQLException {
        if(!Bazaar.getGroupsMap_String().keySet().contains(group)) return;
        BazaarDatabase.newProduct(group, name, product.serialize());
        productMap.put("name", product);
        groupedProduct.computeIfAbsent(group, k->new ArrayList<>()).add(product);
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
        for (String name : productMap.keySet()) {
            long last = System.currentTimeMillis();
            byte[] bytes = productMap.get(name).serialize();
            BazaarDatabase.saveProduct("name", bytes);
            long time_took = (System.currentTimeMillis() - last);
            plugin.getLogger().info("[Bazaar] saving "+name+" took "+time_took+" ms");
        }
        long time_took = (System.currentTimeMillis() - start);
        plugin.getLogger().info("[Bazaar] All product has been saved! took "+time_took+" ms");
    }

}
