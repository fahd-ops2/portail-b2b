package ma.akwa.portalrh.client.dto;

import lombok.*;
import ma.akwa.portalrh.common.enums.Role;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String nom;
    private String email;
    private String address;
    private Role role;
}

