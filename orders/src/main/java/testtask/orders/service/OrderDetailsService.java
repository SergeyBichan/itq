package testtask.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailsService {

//   private final OrderDetailsRepository orderDetailsRepository;
//   private final OrderDetailsMapper orderDetailsMapper;
//
//   public List<OrderDetailsDto> getAllOrderDetailsByOrderId(Long orderId) {
//       List<OrderDetails> fromDb = orderDetailsRepository.findByOrderId(orderId);
//
//        return fromDb.stream()
//                .map(orderDetails -> OrderDetailsDto.builder()
//                        .id(orderDetails.getId())
//                        .productArticle(orderDetails.getProductArticle())
//                        .productName(orderDetails.getProductName())
//                        .productQuantity(orderDetails.getProductQuantity())
//                        .productPrice(orderDetails.getProductPrice())
//                        .build())
//                .collect(toList());
//   }
//
//    public List<OrderDetailsDto> saveOrderDetails(List<OrderDetailsDto> orderDetailsDtoList, Order order) {
//        return orderDetailsDtoList.stream()
//                .map(orderDetailsDto -> {
//                    OrderDetails entity = orderDetailsMapper.toOrderDetailsEntity(orderDetailsDto);
//                    entity.setOrderId(order);
//                    orderDetailsRepository.save(entity);
//                    return orderDetailsMapper.toOrderDetailsDto(entity);
//                })
//                .toList();
//    }
}
