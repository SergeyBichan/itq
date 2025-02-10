package testtask.orders.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import testtask.orders.entity.Order;

import java.util.Optional;


@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    <S extends Order> S save(Order order);
    Optional<Order> findOrderById(Long id);

}
