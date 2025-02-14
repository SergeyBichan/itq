package testtask.orders.service.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import testtask.orders.dto.OrderDtoForCreateOrder;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilMethods {
    public static BigDecimal calculateTotalAmountForOrder(OrderDtoForCreateOrder orderDtoForCreateOrder) {
        return orderDtoForCreateOrder.getOrderDetails().stream()
                .map(detail ->
                        BigDecimal.valueOf(detail.getProductPrice())
                                .multiply(BigDecimal.valueOf(detail.getProductQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static String getStringFromFormattedDate(LocalDate date) {
        return String.valueOf(date);
    }

}
