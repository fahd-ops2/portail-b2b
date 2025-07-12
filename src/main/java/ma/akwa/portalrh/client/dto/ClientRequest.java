package ma.akwa.portalrh.client.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.akwa.portalrh.common.enums.Role;

@Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class ClientRequest {
        @NotNull
        private String nom;

        @NotNull
        @Size(min = 8)
        private String password;

        @NotNull
        private String email;

        @NotNull
        private String address;

        @NotNull
        private Role role;
    }

