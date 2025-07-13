package ma.akwa.portalrh.reclamation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ma.akwa.portalrh.common.enums.ReclamationStatus;
import org.springframework.web.multipart.MultipartFile;

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
    private String type;

    @NotNull(message = "Le statut ne peut pas être nul")
    private ReclamationStatus status;

    @NotBlank(message = "L'identifiant de la commande ne peut pas être vide")
    private String orderId;

    @NotNull(message = "L'identifiant du client ne peut pas être nul")
    private Long clientId;

    private String resolutionNotes;

    @NotNull(message = "La date de création ne peut pas être nulle")
    private LocalDate date;

    private MultipartFile file; // Champ pour le fichier uploadé
}