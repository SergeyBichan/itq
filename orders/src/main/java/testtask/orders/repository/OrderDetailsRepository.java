package testtask.orders.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import testtask.orders.entity.OrderDetails;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {
    <S extends OrderDetails> S save(OrderDetails entity);

    @Query("SELECT * FROM orderdetails where order_id = :order_id")
    List<OrderDetails> findByOrderId(@Param("order_id") Long order_id);
}
