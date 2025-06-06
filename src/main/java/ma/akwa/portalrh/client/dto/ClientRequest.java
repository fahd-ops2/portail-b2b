package ma.akwa.portalrh.client.dto;

import lombok.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class ClientRequest {

        private String nomClient;
        private String password;
        private String email;
        private String address;
    }

