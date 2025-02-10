package testtask.orders.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import testtask.orders.entity.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
     Order save(Order order);
}
