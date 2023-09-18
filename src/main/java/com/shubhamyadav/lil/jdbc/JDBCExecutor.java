package com.shubhamyadav.lil.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JDBCExecutor {
    public static void main(String... args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "hplussport", "postgres", "root");

        try {
            Connection connection = dcm.getConnection();
            CustomerDAO customerDAO = new CustomerDAO(connection);
            customerDAO.findAllSorted(20).forEach(System.out::println);
            System.out.println("Paged");
            for (int i = 1; i < 3; i++) {
                System.out.println("Page Number: " + i);
                customerDAO.findAllPaged(10, i).forEach(System.out::println);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
