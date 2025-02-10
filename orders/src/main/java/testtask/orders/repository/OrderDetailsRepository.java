package testtask.orders.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.entity.OrderDetails;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {
    <S extends OrderDetails> S save(OrderDetails entity);

    @Query("SELECT * FROM orderdetails where orderId = ?1")
    Optional<List<OrderDetailsDto>> findByOrderId(Long orderId);
}
