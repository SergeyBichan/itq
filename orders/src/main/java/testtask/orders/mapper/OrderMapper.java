package testtask.orders.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import testtask.orders.dto.OrderDto;
import testtask.orders.entity.Order;
import testtask.orders.entity.OrderDetails;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "orderDetails", target = "orderDetailsDto")
    OrderDto toOrderDto(Order oder, List<OrderDetails> orderDetails);
    Order toOrder(OrderDto orderDto);

}
