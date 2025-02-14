package testtask.gen.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import testtask.gen.entity.GeneratedOrderNumber;
import testtask.gen.repository.OrderNumberRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static testtask.gen.constants.ConstantUtil.DATE_FORMATTER;
import static testtask.gen.constants.ConstantUtil.NUMBER_LENGTH;

@RequiredArgsConstructor
@Getter
@Setter
@Service
public class GenerateOrderNumberGenarator {

    private final OrderNumberRepository orderNumberRepository;

    public String generateOrderNumber() {
        String number = generateUniqueNumber();
        GeneratedOrderNumber generatedOrderNumber = GeneratedOrderNumber.builder()
                .orderNumber(number)
                        .build();
        orderNumberRepository.save(generatedOrderNumber);

        return number;
    }

    private synchronized String generateUniqueNumber() {
        do {
            String date = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
            StringBuilder number = new StringBuilder(NUMBER_LENGTH);
            for (int i = 0; i < NUMBER_LENGTH; i++) {
                number.append(ThreadLocalRandom.current().nextInt(10));
            }
            String result = number + date;
            if (orderNumberRepository.findGeneratedOrderNumberByOrderNumber(result).isEmpty()) {
                return result;
            }
        } while (true);
    }
}
