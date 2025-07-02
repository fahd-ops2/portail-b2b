package ma.akwa.portalrh.commande.dto;

import java.util.List;

// Nouveau DTO pour une liste d'Orders
public record BulkOrderRequestDTO(
        List<OrderRequestDTO> orders
) {}
