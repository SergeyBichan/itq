package testtask.gen.repository.localrepo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import testtask.gen.entity.GeneratedOrderNumber;

import java.util.HashSet;
import java.util.Set;

@Component
@Setter
@Getter
@AllArgsConstructor
public class LocalRepositoryFoGeneratedNumber {

    public Set<GeneratedOrderNumber> getGeneratedOrderNumbers;
}
