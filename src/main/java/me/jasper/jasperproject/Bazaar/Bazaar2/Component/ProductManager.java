package me.jasper.jasperproject.Bazaar.Bazaar2.Component;

import me.jasper.jasperproject.Bazaar.Bazaar2.Product.BazaarItemsDatabase;
import me.jasper.jasperproject.Bazaar.Bazaar2.Product.Product;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class ProductManager {
    private static Map<Integer, Product> products = new HashMap<>();

    public static void init() throws SQLException, IOException {
        products = BazaarItemsDatabase.getProducts();
    }

    public static void createProduct(String name,Product product) throws SQLException {
        BazaarItemsDatabase.newProduct(name, product.serialize());
    }

}
