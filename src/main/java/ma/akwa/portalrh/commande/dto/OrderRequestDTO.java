package ma.akwa.portalrh.commande.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ma.akwa.portalrh.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDTO(
        long clientId,
        LocalDateTime scheduledFor,
        String note,
        LocalDateTime deliveredAt,
        Double totalAmount,
        OrderStatus status,
        List<OrderLineDTO> items
) {
    public OrderRequestDTO {
        if (clientId <= 0) {
            throw new IllegalArgumentException("Client ID must be positive");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    }

    public record OrderLineDTO(
            String productId,
            Double quantity,
            String note,
            Double unitPrice,
            Double subtotal
    ) {
        public OrderLineDTO {
            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }
        }
    }
}