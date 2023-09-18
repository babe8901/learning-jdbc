package com.shubhamyadav.lil.jdbc;

import com.shubhamyadav.lil.jdbc.util.DataAccessObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAO extends DataAccessObject<Order> {
    public static final String GET_ONE = "SELECT c.first_name, c.last_name, c.email, o.order_id, o.creation_date, o.total_due, o.status, s.first_name, s.last_name, s.email, oi.quantity, p.code, p.name, p.size, p.variety, p.price FROM orders o JOIN customer c ON o.customer_id=c.customer_id JOIN salesperson s ON o.salesperson_id=s.salesperson_id JOIN order_item oi ON oi.order_id=o.order_id JOIN product p ON oi.product_id=p.product_id WHERE o.order_id = ?";

    private static final String GET_FOR_CUST = "SELECT * FROM get_orders_by_customer(?)";
    public OrderDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Order findById(long id) {
        List<OrderLine> orderLines = new ArrayList<>();
        OrderLine orderLine = null;
        Order order = new Order();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            long orderId = -1;
            while (resultSet.next()) {
                orderLine = new OrderLine();
                if (orderId == -1) {
                    order.setCustomerFirstName(resultSet.getString("first_name"));
                    order.setCustomerLastName(resultSet.getString("last_name"));
                    order.setCustomerEmail(resultSet.getString("email"));
                    order.setId(resultSet.getLong("order_id"));
                    orderId = order.getId();
                    order.setCreationDate(new Date(resultSet.getDate("creation_date").getTime()));
                    order.setTotalDue(resultSet.getDouble("total_due"));
                    order.setStatus(resultSet.getString("status"));
                    order.setSalespersonFirstName(resultSet.getString(8));
                    order.setSalespersonLastName(resultSet.getString(9));
                    order.setSalespersonEmail(resultSet.getString(10));
                }
                orderLine.setQuantity(resultSet.getInt("quantity"));
                orderLine.setProductCode(resultSet.getString("code"));
                orderLine.setProductName(resultSet.getString("name"));
                orderLine.setProductSize(resultSet.getInt("size"));
                orderLine.setProductVariety(resultSet.getString("variety"));
                orderLine.setProductPrice(resultSet.getBigDecimal("price"));
                orderLines.add(orderLine);
            }
            order.setOrderLines(orderLines);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order update(Order dto) {
        return null;
    }

    @Override
    public Order create(Order dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    public List<Order> getOrderForCustomer(long customerId) {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_FOR_CUST)) {
            statement.setLong(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            long orderId = -1;
            Order order = null;
            while (resultSet.next()) {
                long localOrderId = resultSet.getLong(4);
                if (orderId != localOrderId) {
                    order = new Order();
                    orders.add(order);
                    order.setId(localOrderId);
                    orderId = localOrderId;
                    order.setCustomerFirstName(resultSet.getString(1));
                    order.setCustomerLastName(resultSet.getString(2));
                    order.setCustomerEmail(resultSet.getString(3));
                    order.setCreationDate(new Date(resultSet.getDate(5).getTime()));
                    order.setTotalDue(resultSet.getDouble(6));
                    order.setStatus(resultSet.getString(7));
                    order.setSalespersonFirstName(resultSet.getString(8));
                    order.setSalespersonLastName(resultSet.getString(9));
                    order.setSalespersonEmail(resultSet.getString(10));
                    List<OrderLine> orderLines = new ArrayList<>();
                    order.setOrderLines(orderLines);
                }
                OrderLine orderLine = new OrderLine();
                orderLine.setQuantity(resultSet.getInt(11));
                orderLine.setProductCode(resultSet.getString(12));
                orderLine.setProductName(resultSet.getString(13));
                orderLine.setProductSize(resultSet.getInt(14));
                orderLine.setProductVariety(resultSet.getString(15));
                orderLine.setProductPrice(resultSet.getBigDecimal(16));
                order.getOrderLines().add(orderLine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return orders;
    }
}
