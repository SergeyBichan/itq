package testtask.orders.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import testtask.orders.entity.Order;
import testtask.orders.entity.OrderDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderDetailsRepository {
    private final JdbcTemplate jdbcTemplate;

    public void save(OrderDetails orderDetails) {
        String sql = "INSERT INTO orderdetails (" +
                     "product_article, " +
                     "product_name, " +
                     "product_quantity, " +
                     "product_price, " +
                     "order_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderDetails.getProductArticle(), orderDetails.getProductName(),
                orderDetails.getProductQuantity(), orderDetails.getProductPrice(), orderDetails.getOrderId().getId());
    }

    public OrderDetails findByOrderId(Long id) {
        String sql = "SELECT * FROM orderdetails WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderDetailsRowMapper(), id);
    }

    public List<OrderDetails> findAllByOrderId(Long id) {
        String sql = "SELECT * FROM orderdetails WHERE order_id = ?";
        return jdbcTemplate.query(sql, new OrderDetailsRowMapper(), id);
    }

    public List<OrderDetails> findAll() {
        String sql = "SELECT * FROM orderdetails";
        return jdbcTemplate.query(sql, new OrderDetailsRowMapper());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM orderdetails WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAllByOrderId(Long id) {
        String sql = "DELETE FROM orderdetails WHERE order_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<OrderDetails> findAllProductsExcludingProductName(String productName, Long id) {
        String sql = "SELECT * FROM orderdetails WHERE product_name <> ? AND order_id = ?";
        return jdbcTemplate.query(sql, new OrderDetailsRowMapper(), productName, id);
    }

    private static class OrderDetailsRowMapper implements RowMapper<OrderDetails> {
        @Override
        public OrderDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setId(rs.getLong("order_id"));

            return OrderDetails.builder()
                    .id(rs.getLong("id"))
                    .productArticle(rs.getLong("product_article"))
                    .productName(rs.getString("product_name"))
                    .productQuantity(rs.getLong("product_quantity"))
                    .productPrice(rs.getLong("product_price"))
                    .orderId(order)
                    .build();
        }
    }
}
