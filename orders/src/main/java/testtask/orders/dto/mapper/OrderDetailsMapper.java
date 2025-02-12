package testtask.orders.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.entity.OrderDetails;

@Component
@RequiredArgsConstructor
public class OrderDetailsMapper {

    public OrderDetailsDto toOrderDetailsDto(OrderDetails orderDetails) {
        return orderDetails == null ? new OrderDetailsDto() : OrderDetailsDto.builder()
                .id(orderDetails.getId())
                .productArticle(orderDetails.getProductArticle())
                .productName(orderDetails.getProductName())
                .productPrice(orderDetails.getProductPrice())
                .productQuantity(orderDetails.getProductQuantity())
                .build();
    }

    public OrderDetails toOrderDetailsEntity(OrderDetailsDto orderDetailsDto) {
        return orderDetailsDto == null ? new OrderDetails() : OrderDetails.builder()
                .productArticle(orderDetailsDto.getProductArticle())
                .productName(orderDetailsDto.getProductName())
                .productQuantity(orderDetailsDto.getProductQuantity())
                .productPrice(orderDetailsDto.getProductPrice())
                .build();
    }
}
