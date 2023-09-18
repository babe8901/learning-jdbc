package com.shubhamyadav.lil.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JDBCExecutor {
    public static void main(String... args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "hplussport", "postgres", "root");

        try {
            Connection connection = dcm.getConnection();
            OrderDAO orderDAO = new OrderDAO(connection);
            List<Order> orders = orderDAO.getOrderForCustomer(789);
            orders.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
