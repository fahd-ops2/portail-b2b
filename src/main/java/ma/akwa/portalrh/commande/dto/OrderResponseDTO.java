package ma.akwa.portalrh.commande.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        String status,
        boolean recurring,
        LocalDateTime createdAt,
        List<OrderLineDTO> items
) {
    public record OrderLineDTO(Long productId, Double quantity) {}
}
