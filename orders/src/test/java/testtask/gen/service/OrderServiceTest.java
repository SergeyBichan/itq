package testtask.gen.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderDtoForCreateOrder;
import testtask.orders.dto.mapper.OrderDetailsMapper;
import testtask.orders.dto.mapper.OrderMapper;
import testtask.orders.entity.Order;
import testtask.orders.entity.OrderDetails;
import testtask.orders.repository.OrderDetailsRepository;
import testtask.orders.repository.OrderRepository;
import testtask.orders.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderDetailsMapper orderDetailsMapper;

    @InjectMocks
    private OrderService orderService;
    private OrderDtoForCreateOrder orderDtoForCreateOrder;
    private Order order;
    private OrderDetails orderDetails;
    private OrderDto orderDto;
    private OrderDetailsDto orderDetailsDto;


    @BeforeEach
    void setUp() {
        orderDetailsDto = OrderDetailsDto.builder()
                .id(1L)
                .productArticle(1L)
                .productPrice(1000L)
                .productQuantity(5L)
                .productName("product")
                .build();

        orderDtoForCreateOrder = OrderDtoForCreateOrder.builder()
                .orderConsumer("Consumer")
                .deliveryAddress("Adress")
                .paymentMethod("cash")
                .deliveryMethod("delivery")
                .orderDetails(List.of(orderDetailsDto))
                .build();

        order = Order.builder()
                .id(1L)
                .orderNumber("1111120250213")
                .totalAmount(BigDecimal.valueOf(5000.00))
                .orderDate(LocalDate.now())
                .orderConsumer("Consumer")
                .deliveryAddress("Address")
                .paymentMethod("Cash")
                .deliveryMethod("Courier")
                .build();

        orderDetails = OrderDetails.builder()
                .id(1L)
                .productArticle(1L)
                .productName("Product")
                .productQuantity(5L)
                .productPrice(1000L)
                .orderId(order)
                .build();

        orderDto = OrderDto.builder()
                .id(1L)
                .orderNumber("1111120250213")
                .totalOrderPrice(BigDecimal.valueOf(5000L))
                .orderDate(LocalDate.now())
                .orderConsumer("Consumer")
                .deliveryAddress("Adress")
                .paymentMethod("cash")
                .deliveryMethod("Courier")
                .orderDetails(List.of(orderDetailsDto))
                .build();

        ReflectionTestUtils.setField(orderService, "uriForGenerateNumber", "http://localhost:8081/api/v1/numbers");
        ReflectionTestUtils.setField(orderService, "correctDateFormat", "yyyyMMdd");
    }

    @Test
    public void getOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(order);
        when(orderDetailsRepository.findAllByOrderId(1L)).thenReturn(Collections.emptyList());
        when(orderMapper.toTDtoWithDetails(order, Collections.emptyList())).thenReturn(orderDto);

        OrderDto result = orderService.getOrderById(1L);

        assertEquals(orderDto, result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderDetailsRepository, times(1)).findAllByOrderId(1L);
        verify(orderMapper, times(1)).toTDtoWithDetails(order, Collections.emptyList());

        Assertions.assertEquals(orderDto.getId(), result.getId());
    }

    @Test
    public void getOrderById_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(null); //

        assertThrows(RuntimeException.class, () -> {
            orderService.getOrderById(1L);
        });
    }

    @Test
    void createOrder_Success() {
        String url = ReflectionTestUtils.getField(orderService, "uriForGenerateNumber").toString();

        when(restTemplate.getForObject(url, String.class))
                .thenReturn("1111120250213");

        doNothing().when(orderRepository).save(any(Order.class));

        when(orderRepository.findByOrderNumber(order.getOrderNumber())).thenReturn(order);

        doNothing().when(orderDetailsRepository).save(any(OrderDetails.class));

        String result = orderService.createOrder(orderDtoForCreateOrder);

        assertEquals("Order 1111120250213 created successfully", result);
        verify(restTemplate, times(1)).getForObject(url, String.class);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderDetailsRepository, times(1)).save(any(OrderDetails.class));
    }

    @Test
    void createOrder_GenerateOrderNumberFailed() {
        String url = ReflectionTestUtils.getField(orderService, "uriForGenerateNumber").toString();

        when(restTemplate.getForObject(url, String.class)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(orderDtoForCreateOrder));
        assertEquals("Generate order number failed", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, String.class);
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderDetailsRepository, never()).save(any(OrderDetails.class));
    }

    @Test
    void createOrder_IncorrectDateFormat() {
        String url = ReflectionTestUtils.getField(orderService, "uriForGenerateNumber").toString();

        String generatedOrderNumber = "ORDER123InvalidDate";
        when(restTemplate.getForObject(url, String.class))
                .thenReturn(generatedOrderNumber);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(orderDtoForCreateOrder));
        assertTrue(exception.getMessage().contains("Text '123InvalidDate' could not be parsed"));
        verify(restTemplate, times(1))
                .getForObject(url, String.class);
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderDetailsRepository, never()).save(any(OrderDetails.class));
    }

    @Test
    void getReduce_CalculatesTotalAmountCorrectly() {
        OrderDtoForCreateOrder orderDto = OrderDtoForCreateOrder.builder()
                .orderDetails(List.of(
                        OrderDetailsDto.builder().productPrice(100L).productQuantity(2L).build(),
                        OrderDetailsDto.builder().productPrice(50L).productQuantity(3L).build()
                ))
                .build();

        BigDecimal reduce = orderDto.getOrderDetails().stream()
                .map(detail ->
                        BigDecimal.valueOf(detail.getProductPrice())
                                .multiply(BigDecimal.valueOf(detail.getProductQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(BigDecimal.valueOf(350), reduce);
    }

    @Test
    void createOrder_SavesOrderDetailsCorrectly() {

        String url = ReflectionTestUtils.getField(orderService, "uriForGenerateNumber").toString();
        String generatedOrderNumber = "1111120250213";

        when(restTemplate.getForObject(url, String.class)).thenReturn(generatedOrderNumber);

        doNothing().when(orderRepository).save(any(Order.class));
        when(orderRepository.findByOrderNumber(generatedOrderNumber)).thenReturn(order);

        orderService.createOrder(orderDtoForCreateOrder);

        verify(orderDetailsRepository, times(1)).save(any(OrderDetails.class));
    }

    @Test
    void deleteOrder_WithNullId_ThrowsException() {

        Long id = null;

        assertThrows(RuntimeException.class, () -> orderService.deleteOrder(id));
        assertEquals("id is null", assertThrows(RuntimeException.class,
                () -> orderService.deleteOrder(id)).getMessage());
    }

    @Test
    void deleteOrder_WithNonExistingOrder_ThrowsException() {
        Long id = 1L;
        when(orderRepository.findById(id)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> orderService.deleteOrder(id));
        assertEquals("Order not found", assertThrows(RuntimeException.class,
                () -> orderService.deleteOrder(id)).getMessage());
    }

    @Test
    void deleteOrder_WithExistingOrder_DeletesSuccessfully() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(order);

        orderService.deleteOrder(id);

        verify(orderDetailsRepository, times(1)).deleteAllByOrderId(id);
        verify(orderRepository, times(1)).deleteById(id);
    }
}



