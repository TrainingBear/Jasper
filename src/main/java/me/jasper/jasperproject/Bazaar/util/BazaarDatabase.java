package me.jasper.jasperproject.Bazaar.util;

import lombok.Getter;
import lombok.val;
import me.jasper.jasperproject.Bazaar.Component.Product;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.util.io.BukkitObjectInputStream;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BazaarDatabase {
    @Getter private static Connection connection;
    public static boolean startConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bazaar",
                    "root",
                    "1234"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS product(" +
                "  id mediumint NOT NULL AUTO_INCREMENT," +
                "  name varchar(255) NOT NULL," +
                "  product mediumblob NOT NULL," +
                "  category varchar(255) NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE KEY name (name)" +
                ")");
    }

    public static void newProduct(String category, String name, byte[] product) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into product (name, product, category) values (?, ?, ?)");
        preparedStatement.setString(1, name);
        preparedStatement.setBytes(2, product);
        preparedStatement.setString(3, category);
        preparedStatement.execute();
    }

    public static void saveProduct(String name, byte[] product) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update product set " +
                "product = ? where name = ?"
        );
        preparedStatement.setBytes(1, product);
        preparedStatement.setString(2, name);
        preparedStatement.execute();
    }

    public static @org.jetbrains.annotations.Nullable Product getProduct(String name) throws SQLException, IOException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from product where name = ?");
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return (Product) retriveObject(resultSet.getBytes("product"));
        }
        return null;
    }

    public static void remove(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from product where name = ?");
        preparedStatement.setString(1, name);
        preparedStatement.execute();
    }

    public static void loadDB() throws SQLException, IOException {
        Map<String, Product> productMap = new HashMap<>();
        Map<String, List<Product>> groupedProduct = new HashMap<>();

        ResultSet resultSet = connection.createStatement().executeQuery(
                "select * from product");
        while (resultSet.next()){
            val last = System.currentTimeMillis();
            String name = resultSet.getString("name");
            byte[] bytes = resultSet.getBytes("product");
            Product product = (Product) retriveObject(bytes);
            String category = resultSet.getString("category");
            productMap.put(name, product);
            groupedProduct.computeIfAbsent(category, k->new ArrayList<>()).add(product);

            val productName = product.getProduct_name();
            JasperProject.getPlugin().getLogger().info("[BazaarDB] "+productName+" registered ("+(System.currentTimeMillis()-last)+" ms)");
        }
        ProductManager.setProductMap(productMap);
        ProductManager.setGroupedProduct(groupedProduct);
    }

    public static Map<String, Product> getProducts() throws SQLException, IOException {
        Map<String, Product> products = new HashMap<>();
        ResultSet resultSet = connection.createStatement().executeQuery(
                "select name, product from product");
        String name;
        Product product;

        while (resultSet.next()){
            name = resultSet.getString("name");
            product = (Product) retriveObject(resultSet.getBytes("product"));

            val productName = product.getProduct_name();
            products.put(name, product);
        }

        return products;
    }

    public static @Nullable String getCategory(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select category from product where name = ?");
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            return resultSet.getString("category");
        }
        return null;
    }

    public static void setCategory(String name, String new_groupname) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update product set category = ? where name = ?");
        preparedStatement.setString(1, new_groupname);
        preparedStatement.setString(2, name);
        preparedStatement.execute();
    }

    private static Object retriveObject(byte[] bytes) throws IOException {
        Object item;
        try(BukkitObjectInputStream input =
                    new BukkitObjectInputStream(new ByteArrayInputStream(bytes))){
            item = input.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return item;
    }

}
