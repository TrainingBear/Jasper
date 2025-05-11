package me.jasper.jasperproject.Bazaar.util;

import lombok.val;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BazaarDatabaseTest {

    @Test
    public void test() throws SQLException {
        BazaarDatabase.startConnection();
        Connection connection = BazaarDatabase.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select * from product");
        ResultSet execute = preparedStatement.executeQuery();

        val name = execute.getString("name");
        System.out.printf(name);
    }

}