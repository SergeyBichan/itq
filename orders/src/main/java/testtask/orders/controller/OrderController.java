package testtask.orders.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderDtoForCreateOrder;
import testtask.orders.entity.Order;
import testtask.orders.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Order Managment", description = "API for managing orders")
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    @Operation(summary = "Get all orders", description = "Returns a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id", description = "Returns order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create order", description = "Create order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully order created"),
            @ApiResponse(responseCode = "404")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderDtoForCreateOrder orderDto) {
        orderService.createOrder(orderDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Delete order by id", description = "Delete order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully done"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @Operation(summary = "Get orders by Date and Total amount", description = "Returns list of orders by parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully done"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping("/by-date-and-totalamount")
    public ResponseEntity<List<OrderDto>> getOrdersByDateAndTotalAmount(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam BigDecimal totalAmount
    ) {
        return new ResponseEntity<>(orderService.getOrdersByDateAndMoreThanTotalAmount(date, totalAmount),
                HttpStatus.OK);
    }

    @Operation(summary = "Get orders by Date and Excluding some products", description = "Returns list of orders by parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully done"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping("/by-date-excluding-product")
    public ResponseEntity<List<OrderDto>> getOrdersByDateExcludingProduct(
            @RequestParam String productName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateEnd
    ) {
        return new ResponseEntity<>(
                orderService.getAllOrdersBetweenDatesAndExcludingProduct(productName, dateStart, dateEnd)
                , HttpStatus.OK
        );
    }

}
