package testtask.orders.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderWithoutDetailsDto;
import testtask.orders.service.OrderService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderWithoutDetailsDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }


}
