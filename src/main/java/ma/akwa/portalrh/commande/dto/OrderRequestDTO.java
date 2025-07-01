package ma.akwa.portalrh.commande.dto;

import ma.akwa.portalrh.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDTO(
        Long clientId,
        String deliveryAddress,
        LocalDateTime scheduledFor,
        String note,
        Double totalAmount,
        OrderStatus status,
        List<OrderLineDTO> items
) {
    public record OrderLineDTO(String productId, Double quantity, String note) {}
}
