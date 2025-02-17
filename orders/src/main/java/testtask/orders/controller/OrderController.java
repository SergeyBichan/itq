package testtask.orders.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderDtoForCreateOrder;
import testtask.orders.dto.OrderDtoWithoutDetails;
import testtask.orders.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Order Management", description = "API for managing orders")
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    @Operation(summary = "Get all orders", description = "Returns a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public List<OrderDtoWithoutDetails> getAllOrders() {
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
    public ResponseEntity<String> createOrder(
            @RequestBody @NotNull(message = "Заказ не может быть пустым") OrderDtoForCreateOrder orderDto
    ) {
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete order by id", description = "Delete order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully done"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable @Positive(message = "id должен быть больше 0") Long id) {
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
            @RequestParam @Positive(message = "Сумма должна быть больше 0") BigDecimal totalAmount) {
        return new ResponseEntity<>(orderService.getOrdersByDateAndMoreThanTotalAmount(date, totalAmount), HttpStatus.OK);
    }

    @Operation(summary = "Get orders by Date and Excluding some products",
            description = "Returns list of orders by parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully done"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping("/by-date-excluding-product")
    public ResponseEntity<List<OrderDto>> getOrdersByDateExcludingProduct(
            @RequestParam @NotNull(message = "Имя продукта не может быть пустым") String productName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateEnd) {
        return new ResponseEntity<>(
                orderService.getAllOrdersBetweenDatesAndExcludingProduct(productName, dateStart, dateEnd),HttpStatus.OK
        );
    }

}
