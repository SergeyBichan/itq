package testtask.orders.service;

import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderDtoForCreateOrder;
import testtask.orders.dto.OrderDtoWithoutDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface OrderService {
    String createOrder(OrderDtoForCreateOrder orderDtoForCreateOrder);

    OrderDto getOrderById(Long id);

    void deleteOrder(Long id);

    List<OrderDto> getAllOrdersBetweenDatesAndExcludingProduct(String productName, LocalDate dateStart, LocalDate dateEnd);

    List<OrderDto> getOrdersByDateAndMoreThanTotalAmount(LocalDate date, BigDecimal totalAmount);

    List<OrderDtoWithoutDetails> getAllOrders();

}
