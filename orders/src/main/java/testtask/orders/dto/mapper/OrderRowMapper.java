package testtask.orders.dto.mapper;

import org.springframework.jdbc.core.RowMapper;
import testtask.orders.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderRowMapper implements RowMapper<Order> {
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
