package testtask.orders.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import testtask.orders.entity.Order;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    <S extends Order> S save(Order order);

    @Query("SELECT * FROM orders WHERE id=:id")
    Optional<Order> findOrderById(@Param("id")Long id);

    @Query("SELECT * FROM orders")
    List<Order> findAll();

}
