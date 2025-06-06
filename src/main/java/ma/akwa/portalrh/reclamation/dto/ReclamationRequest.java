package ma.akwa.portalrh.reclamation.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationRequest {
    private String description;
    private String type;
    private String status;
    private LocalDate date;
}