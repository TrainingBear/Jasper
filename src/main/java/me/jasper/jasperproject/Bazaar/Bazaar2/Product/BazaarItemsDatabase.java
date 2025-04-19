package me.jasper.jasperproject.Bazaar.Bazaar2.Product;

import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public abstract class BazaarItemsDatabase {
    private static Connection connection;
    public static void startConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bazaar", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void newProduct(String name, byte[] product) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into product (name, product) values (?, ?)");
        preparedStatement.setString(0, name);
        preparedStatement.setBytes(1, product);
        preparedStatement.execute();
    }

    public static void saveProduct(int id, String name, byte[] product) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("update product set " +
                "product = ? ," +
                "name = ? " +
                "where id = ?"
        );
        preparedStatement.setBytes(0, product);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, id);

        preparedStatement.execute();
    }

    public static Product getProduct(String name) throws SQLException, IOException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from product where name = ?");
        preparedStatement.setString(0, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return (Product) retriveObject(resultSet.getBytes("product"));
        }
        return null;
    }

    public static Map<Integer, Product> getProducts() throws SQLException, IOException {
        Map<Integer, Product> products = new HashMap<>();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from items");
        int id = 0;
        String name;
        Product product;

        while (resultSet.next()){
            id = resultSet.getInt("id");
            name = resultSet.getString("name");
            product = (Product) retriveObject(resultSet.getBytes("product"));

            products.put(id, product);
        }

        return products;
    }

    private static Object retriveObject(byte[] bytes) throws IOException {
        Object item;
        try(ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes))){
            item = input.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return item;
    }

}
