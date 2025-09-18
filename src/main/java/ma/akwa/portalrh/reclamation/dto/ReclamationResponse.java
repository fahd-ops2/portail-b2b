package ma.akwa.portalrh.reclamation.dto;

import lombok.*;
import ma.akwa.portalrh.common.enums.ReclamationStatus;
import ma.akwa.portalrh.common.enums.TypeRc;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationResponse {
    private Long id;
    private String description;
    private TypeRc type; // Retourné comme string dans JSON via Jackson
    private ReclamationStatus status;
    private String orderId;
    private Long clientId;
    private LocalDate date;
    private LocalDate updatedAt;
    private String resolutionNotes;
    private String filePath;
}