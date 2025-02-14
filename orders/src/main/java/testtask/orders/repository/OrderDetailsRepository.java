package testtask.orders.repository;

import testtask.orders.entity.OrderDetails;

import java.util.List;

public interface OrderDetailsRepository {

    void save(OrderDetails orderDetails);

    OrderDetails findByOrderId(Long id);

    List<OrderDetails> findAllByOrderId(Long id);

    List<OrderDetails> findAllProductsExcludingProductName(String productName, Long id);

    void deleteById(Long id);

    void deleteAllByOrderId(Long id);
}
