package testtask.orders.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import testtask.orders.dto.mapper.OrderRowMapper;
import testtask.orders.entity.Order;
import testtask.orders.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
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

    @Override
    public Order findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id);
    }

    @Override
    public Order findByOrderNumber(String orderNumber) {
        String sql = "SELECT * FROM orders WHERE order_number = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), orderNumber);
    }

    @Override
    public List<Order> findOrdersByDateAndMoreThanTotalAmount(String date, BigDecimal amount) {
        String sql = "SELECT * FROM orders WHERE order_date::date = TO_DATE(?, 'YYYY-MM-DD') AND total_amount > ?";
        return jdbcTemplate.query(sql, new OrderRowMapper(), date, amount);
    }

    @Override
    public List<Order> findOrdersBetweenDatesAndExcludingProduct(String productName, String dateStart, String dateEnd) {
        String sql = "SELECT o.*" +
                     " FROM orders o" +
                     " LEFT JOIN orderdetails od" +
                     " ON o.id = od.order_id" +
                     " AND od.product_name = ?" +
                     " WHERE od.id IS NULL" +
                     " AND o.order_date::date BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')";
        return jdbcTemplate.query(sql, new OrderRowMapper(), productName, dateStart, dateEnd);
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
