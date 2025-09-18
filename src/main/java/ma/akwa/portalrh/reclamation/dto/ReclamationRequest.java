package ma.akwa.portalrh.reclamation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ma.akwa.portalrh.common.enums.ReclamationStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationRequest {
    @NotBlank(message = "La description ne peut pas être vide")
    private String description;

    @NotBlank(message = "Le type de réclamation ne peut pas être vide")
    private String type; // String dans le DTO, converti en TypeRc dans le mapper

    private ReclamationStatus status; // Optionnel, défaut EN_ATTENTE dans l'entité

    @NotBlank(message = "L'identifiant de la commande ne peut pas être vide")
    private String orderId;

    @NotNull(message = "L'identifiant du client ne peut pas être nul")
    private Long clientId;

    private String resolutionNotes;

    private LocalDate date; // Optionnel, défini à LocalDate.now() si null
}