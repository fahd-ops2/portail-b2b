package ma.akwa.portalrh.common.security.config;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationtRequest {
    private String email;
    private String password;
}
