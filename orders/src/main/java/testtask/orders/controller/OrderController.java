package testtask.orders.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testtask.orders.dto.OrderDtoForCreateOrder;
import testtask.orders.entity.Order;
import testtask.orders.service.OrderService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable("id") Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderDtoForCreateOrder orderDto) {
        orderService.createOrder(orderDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
