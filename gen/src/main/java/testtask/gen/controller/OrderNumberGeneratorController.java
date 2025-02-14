package testtask.gen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.gen.service.GenerateOrderNumberGenarator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderNumberGeneratorController {

    private final GenerateOrderNumberGenarator generateOrderNumberGenarator;

    @GetMapping("/numbers")
    public String generateUniqOrderNumber() {
        return generateOrderNumberGenarator.generateOrderNumber();
    }
}
