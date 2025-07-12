package ma.akwa.portalrh.admin.dto;

import lombok.*;
import ma.akwa.portalrh.common.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {
    private Long id;
    private String nom;
    private String email;
    private Role role;
}
