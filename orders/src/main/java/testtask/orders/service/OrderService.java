package testtask.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.mapper.OrderMapper;
import testtask.orders.entity.Order;
import testtask.orders.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor

public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsService orderDetailsService;

    private final OrderMapper orderMapper;

    public OrderDto findOrderById(Long id) {
        List<OrderDetailsDto> orderDetailsDtoList = orderDetailsService.getAllOrderDetailsByOrderId(id);

        return orderRepository.findOrderById(id)
                .map(order -> orderMapper.toTDto(order, orderDetailsDtoList))
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public List<Order> findAll() {
        List<Order> all = orderRepository.findAll();
        System.out.println(all);
        return all;
    }

    public ResponseEntity<OrderDto> saveOrder(OrderDto orderDto) {
        return null;
    }
}
