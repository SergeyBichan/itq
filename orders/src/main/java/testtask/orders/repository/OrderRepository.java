package testtask.orders.repository;

import testtask.orders.dto.OrderDtoWithoutDetails;
import testtask.orders.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository {

    void save(Order order);

    Order findById(Long id);

    Order findByOrderNumber(String orderNumber);

    List<Order> findOrdersByDateAndMoreThanTotalAmount(String date, BigDecimal amount);

    List<Order> findOrdersBetweenDatesAndExcludingProduct(String productName, String dateStart, String dateEnd);

    List<Order> findAll();

    void deleteById(Long id);
}
