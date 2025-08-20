package ma.akwa.portalrh.commande.dto;

import ma.akwa.portalrh.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDTO(
        long clientId,
        String deliveryAddress,
        LocalDateTime scheduledFor,
        String note,
        LocalDateTime deliveredAt,
        Double totalAmount,
        OrderStatus status,
        List<OrderLineDTO> items
) {

    public record OrderLineDTO(
            String productId,
            Double quantity,
            String note,
            Double unitPrice,
            Double subtotal
    ) {}

}



