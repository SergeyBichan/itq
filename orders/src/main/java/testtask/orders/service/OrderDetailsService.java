package testtask.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.entity.OrderDetails;
import testtask.orders.repository.OrderDetailsRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class OrderDetailsService {

   private final OrderDetailsRepository orderDetailsRepository;

   public List<OrderDetailsDto> getAllOrderDetailsByOrderId(Long orderId) {
       List<OrderDetails> fromDb = orderDetailsRepository.findByOrderId(orderId);

        return fromDb.stream()
                .map(orderDetails -> OrderDetailsDto.builder()
                        .id(orderDetails.getId())
                        .productArticle(orderDetails.getProduct_article())
                        .productName(orderDetails.getProduct_name())
                        .productQuantity(orderDetails.getProduct_quantity())
                        .productPrice(orderDetails.getProduct_price())
                        .build())
                .collect(toList());
   }
}
