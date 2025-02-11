package testtask.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderWithoutDetailsDto;
import testtask.orders.dto.mapper.OrderMapper;
import testtask.orders.entity.Order;
import testtask.orders.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsService orderDetailsService;
    private final RestTemplate restTemplate;

    private final OrderMapper orderMapper;

    public OrderDto findOrderById(Long id) {
        List<OrderDetailsDto> orderDetailsDtoList = orderDetailsService.getAllOrderDetailsByOrderId(id);

        return orderRepository.findOrderById(id)
                .map(order -> orderMapper.toTDtoWithDetails(order, orderDetailsDtoList))
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                        "Order with id: " + id + " not found"));
    }

    public List<OrderWithoutDetailsDto> findAll() {
        List<Order> all = orderRepository.findAll();
        if (all.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No orders found");
        }

        return all.stream()
                .map(orderMapper::toTDtoWithoutDetails)
                .toList();
    }

//TODO
//    public ResponseEntity<OrderDto> saveOrder(OrderDto orderDto) {
//            String generatedOrderNumber = restTemplate.getForObject(URI_FOR_GENERATE_NUMBER, String.class);
//        return null;
//    }

}
