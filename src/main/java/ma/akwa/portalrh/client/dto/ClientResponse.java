package ma.akwa.portalrh.client.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String nomClient;
    private String password;
    private String email;
    private String address;
}

