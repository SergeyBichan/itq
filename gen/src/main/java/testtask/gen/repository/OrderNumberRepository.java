package testtask.gen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import testtask.gen.entity.GeneratedOrderNumber;

import java.util.Optional;

@Repository
public interface OrderNumberRepository extends MongoRepository<GeneratedOrderNumber, String> {

    @Override
    <S extends GeneratedOrderNumber> S save(S entity);

    Optional<GeneratedOrderNumber> findGeneratedOrderNumberByOrderNumber(String orderNumber);
}
