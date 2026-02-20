package ma.akwa.portalrh.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ma.akwa.portalrh.common.enums.Role;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder

public class AdminRequest {
    @NotNull
    private String nom;

    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String telephone;

    @NotNull
    private Role role;
}
