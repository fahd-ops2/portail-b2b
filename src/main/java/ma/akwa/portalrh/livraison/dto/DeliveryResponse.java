package ma.akwa.portalrh.livraison.dto;

import ma.akwa.portalrh.common.enums.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryResponse(
        Long id,
        Long orderId,
        Long livreurId,
        LocalDateTime scheduledTime,
        DeliveryStatus status,
        String deliveryAddress,
        String notes,
        LocalDateTime deliveredAt,
        Boolean isUrgent
) {}