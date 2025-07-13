package ma.akwa.portalrh.livraison.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ma.akwa.portalrh.common.enums.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryRequest(
        @NotNull(message = "L'ID de la commande ne peut pas être nul")
        String orderId,

        @NotNull(message = "L'ID du livreur ne peut pas être nul")
        Long livreurId,

        @NotNull(message = "L'heure prévue ne peut pas être nulle")
        LocalDateTime scheduledTime,

        @NotNull(message = "Le statut ne peut pas être nul")
        DeliveryStatus status,

        @NotBlank(message = "L'adresse de livraison ne peut pas être vide")
        String deliveryAddress,

        String notes,

        Boolean isUrgent
) {}