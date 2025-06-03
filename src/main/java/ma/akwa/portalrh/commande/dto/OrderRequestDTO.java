package ma.akwa.portalrh.commande.dto;

import java.util.List;

public record OrderRequestDTO(
        boolean recurring,
        List<OrderLineDTO> items
) {
    public record OrderLineDTO(Long productId, Double quantity) {}
}
