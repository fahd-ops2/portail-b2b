package ma.akwa.portalrh.reclamation.dto;

import lombok.*;
import ma.akwa.portalrh.common.enums.ReclamationStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationResponse {
    private Long id;

    private String description;

    private String type;

    private ReclamationStatus status;

    private String orderId;

    private Long clientId;

    private LocalDate date;

    private LocalDate updatedAt;

    private String resolutionNotes;

    private String filePath; // Chemin du fichier uploadé
}