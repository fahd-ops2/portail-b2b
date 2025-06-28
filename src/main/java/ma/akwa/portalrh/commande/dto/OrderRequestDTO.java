package ma.akwa.portalrh.commande.dto;

import java.util.List;

public record OrderRequestDTO(
        List<OrderLineDTO> items
) {
    public record OrderLineDTO(String productId, Double quantity) {}
}
