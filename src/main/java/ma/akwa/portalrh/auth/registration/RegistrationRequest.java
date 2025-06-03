package ma.akwa.portalrh.auth.registration;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class RegistrationRequest {
    private  String firstName;
    private  String lastName;
    private  String email;
    private  String password;

}
