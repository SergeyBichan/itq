package testtask.orders.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import testtask.orders.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(Order order) {
        String sql = "INSERT INTO orders (order_number, " +
                     "total_amount, " +
                     "order_date, " +
                     "order_consumer, " +
                     "delivery_address, " +
                     "payment_method, " +
                     "delivery_method) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                order.getOrderNumber(),
                order.getTotalAmount(),
                order.getOrderDate(),
                order.getOrderConsumer(),
                order.getDeliveryAddress(),
                order.getPaymentMethod(),
                order.getDeliveryMethod());
    }

    public Order findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id);
    }

    public Order findByOrderNumber(String orderNumber) {
        String sql = "SELECT * FROM orders WHERE order_number = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), orderNumber);
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    private static class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Order.builder()
                    .id(rs.getLong("id"))
                    .orderNumber(rs.getString("order_number"))
                    .totalAmount(rs.getBigDecimal("total_amount"))
                    .orderDate(rs.getObject("order_date", LocalDate.class))
                    .orderConsumer(rs.getString("order_consumer"))
                    .deliveryAddress(rs.getString("delivery_address"))
                    .paymentMethod(rs.getString("payment_method"))
                    .deliveryMethod(rs.getString("delivery_method"))
                    .build();
        }
    }
}
