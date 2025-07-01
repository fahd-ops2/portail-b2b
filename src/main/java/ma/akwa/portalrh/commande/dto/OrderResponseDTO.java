package ma.akwa.portalrh.commande.dto;

import ma.akwa.portalrh.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        Long clientId,
        String deliveryAddress,
        LocalDateTime scheduledFor,
        LocalDateTime deliveredAt,
        String note,
        Double totalAmount,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderLineDTO> items
) {
    public record OrderLineDTO(Long productId, Double quantity,Double unitPrice, Double subtotal, String note) {}
}
