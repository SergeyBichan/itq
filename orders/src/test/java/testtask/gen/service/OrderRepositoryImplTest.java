package testtask.gen.service;

import exception.ResourceNotFoundException;
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
import testtask.orders.repository.OrderRepository;
import testtask.orders.repository.impl.OrderDetailsRepositoryImpl;
import testtask.orders.repository.impl.OrderRepositoryImpl;
import testtask.orders.service.OrderService;
import testtask.orders.service.impl.OrderServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplTest {
    @Mock
    private OrderRepositoryImpl orderRepositoryImpl;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailsRepositoryImpl orderDetailsRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderDetailsMapper orderDetailsMapper;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;
    private OrderService orderService;
    private OrderDtoForCreateOrder orderDtoForCreateOrder;
    private Order order;
    private OrderDto orderDto;
    private OrderDetails orderDetails;
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

        orderDetails = OrderDetails.builder()
                .id(1L)
                .productName("Product")
                .productArticle(123L)
                .productPrice(5000L)
                .productQuantity(10L)
                .orderId(order)
                .build();

        ReflectionTestUtils.setField(orderServiceImpl, "uriForGenerateNumber",
                "http://localhost:8081/api/v1/numbers");
        ReflectionTestUtils.setField(orderServiceImpl, "correctDateFormat", "yyyyMMdd");
    }

    @Test
    void getOrderById_Success() {
        List<OrderDetails> orderDetailsList = List.of(orderDetails);
        List<OrderDetailsDto> orderDetailsDtoList = List.of(orderDetailsDto);

        when(orderRepository.findById(1L)).thenReturn(order);
        when(orderDetailsRepository.findAllByOrderId(1L)).thenReturn(orderDetailsList);
        when(orderDetailsMapper.toOrderDetailsDto(orderDetails)).thenReturn(orderDetailsDto);
        when(orderMapper.toTDtoWithDetails(order, orderDetailsDtoList)).thenReturn(orderDto);

        OrderDto result = orderServiceImpl.getOrderById(1L);

        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderDetailsRepository, times(1)).findAllByOrderId(1L);
        verify(orderDetailsMapper, times(1)).toOrderDetailsDto(orderDetails);
        verify(orderMapper, times(1)).toTDtoWithDetails(order, orderDetailsDtoList);
    }

    @Test
    void getOrderById_OrderNotFound() {
        lenient().when(orderRepositoryImpl.findById(1L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> orderServiceImpl.getOrderById(1L));
    }

    @Test
    void createOrder_Success() {
        String url = ReflectionTestUtils.getField(orderServiceImpl, "uriForGenerateNumber").toString();
        String generatedOrderNumber = "1111120250213";
        LocalDate date = LocalDate.parse("20250213", DateTimeFormatter.ofPattern("yyyyMMdd"));

        when(restTemplate.getForObject(url, String.class))
                .thenReturn(generatedOrderNumber);
        when(orderMapper.toEntity(any(OrderDtoForCreateOrder.class), eq(date), eq(generatedOrderNumber), any(BigDecimal.class)))
                .thenReturn(order);
        doNothing().when(orderRepository).save(any(Order.class));
        when(orderRepository.findByOrderNumber(any(String.class))).thenReturn(order);
        when(orderDetailsMapper.toOrderDetailsEntity(any(OrderDetailsDto.class), any(Order.class)))
                .thenReturn(orderDetails);
        doNothing().when(orderDetailsRepository).save(any(OrderDetails.class));

        String result = orderServiceImpl.createOrder(orderDtoForCreateOrder);

        assertNotNull(result);
        assertEquals("Order 1111120250213 created successfully", result);

        verify(restTemplate, times(1)).getForObject(url, String.class);
        verify(orderMapper, times(1)).toEntity(any(OrderDtoForCreateOrder.class), eq(date), eq(generatedOrderNumber), any(BigDecimal.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderRepository, times(1)).findByOrderNumber(generatedOrderNumber);
        verify(orderDetailsRepository, times(1)).save(any(OrderDetails.class));
    }

    @Test
    void createOrder_GenerateOrderNumberFailed() {
        String url = ReflectionTestUtils.getField(orderServiceImpl, "uriForGenerateNumber").toString();

        when(restTemplate.getForObject(url, String.class)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderServiceImpl.createOrder(orderDtoForCreateOrder));
        assertEquals("Generate order number failed", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, String.class);
        verify(orderRepositoryImpl, never()).save(any(Order.class));
        verify(orderDetailsRepository, never()).save(any(OrderDetails.class));
    }

    @Test
    void createOrder_IncorrectDateFormat() {
        String url = ReflectionTestUtils.getField(orderServiceImpl, "uriForGenerateNumber").toString();

        String generatedOrderNumber = "ORDER123InvalidDate";
        when(restTemplate.getForObject(url, String.class))
                .thenReturn(generatedOrderNumber);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderServiceImpl.createOrder(orderDtoForCreateOrder));
        assertTrue(exception.getMessage().contains("Text '123InvalidDate' could not be parsed"));
        verify(restTemplate, times(1))
                .getForObject(url, String.class);
        verify(orderRepositoryImpl, never()).save(any(Order.class));
        verify(orderDetailsRepository, never()).save(any(OrderDetails.class));
    }

    @Test
    void getReduce_CalculatesTotalAmountCorrectly() {
        OrderDtoForCreateOrder dtoForCreateOrder = OrderDtoForCreateOrder.builder()
                .orderDetails(List.of(
                        OrderDetailsDto.builder().productPrice(100L).productQuantity(2L).build(),
                        OrderDetailsDto.builder().productPrice(50L).productQuantity(3L).build()
                ))
                .build();

        BigDecimal reduce = dtoForCreateOrder.getOrderDetails().stream()
                .map(detail ->
                        BigDecimal.valueOf(detail.getProductPrice())
                                .multiply(BigDecimal.valueOf(detail.getProductQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(BigDecimal.valueOf(350), reduce);
    }

    @Test
    void createOrder_SavesOrderDetailsCorrectly() {

        String url = ReflectionTestUtils.getField(orderServiceImpl, "uriForGenerateNumber").toString();
        String generatedOrderNumber = "1111120250213";

        when(restTemplate.getForObject(url, String.class)).thenReturn(generatedOrderNumber);

        doNothing().when(orderRepositoryImpl).save(any(Order.class));
        when(orderRepositoryImpl.findByOrderNumber(generatedOrderNumber)).thenReturn(order);

        orderServiceImpl.createOrder(orderDtoForCreateOrder);

        verify(orderDetailsRepository, times(1)).save(any(OrderDetails.class));
    }

    @Test
    void deleteOrder_WithNullId_ThrowsException() {

        Long id = null;

        assertThrows(RuntimeException.class, () -> orderServiceImpl.deleteOrder(null));
        assertEquals("id is null", assertThrows(RuntimeException.class,
                () -> orderServiceImpl.deleteOrder(null)).getMessage());
    }

    @Test
    void deleteOrder_WithNonExistingOrder_ThrowsException() {
        Long id = 1L;
        when(orderRepositoryImpl.findById(id)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> orderServiceImpl.deleteOrder(id));
        assertEquals("Order not found", assertThrows(RuntimeException.class,
                () -> orderServiceImpl.deleteOrder(id)).getMessage());
    }

    @Test
    void deleteOrder_WithExistingOrder_DeletesSuccessfully() {

        when(orderRepository.findById(any(Long.class))).thenReturn(order);
        doNothing().when(orderDetailsRepository).deleteAllByOrderId(any(Long.class));
        doNothing().when(orderRepository).deleteById(any(Long.class));

        Order byId = orderRepository.findById(1L);
        orderDetailsRepository.deleteAllByOrderId(1L);
        orderRepository.deleteById(1L);

        assertEquals(byId, order);

        verify(orderRepository, times(1)).findById(1L);
        verify(orderDetailsRepository, times(1)).deleteAllByOrderId(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }
}



