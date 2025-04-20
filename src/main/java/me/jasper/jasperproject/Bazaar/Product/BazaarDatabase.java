package me.jasper.jasperproject.Bazaar.Product;

import org.bukkit.util.io.BukkitObjectInputStream;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public abstract class BazaarDatabase {
    private static Connection connection;
    public static void startConnection(){
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mysql",
                    "root",
                    "mysql12345"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public static Product getProduct(String name) throws SQLException, IOException {
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

    public static Map<String, Product> getProducts() throws SQLException, IOException {
        Map<String, Product> products = new HashMap<>();
        ResultSet resultSet = connection.createStatement().executeQuery(
                "select * from items");
        String name;
        Product product;

        while (resultSet.next()){
            name = resultSet.getString("name");
            product = (Product) retriveObject(resultSet.getBytes("product"));

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
