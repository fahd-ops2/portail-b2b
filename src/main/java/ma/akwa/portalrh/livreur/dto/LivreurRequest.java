package ma.akwa.portalrh.livreur.dto;

import ma.akwa.portalrh.common.enums.Role;

public record LivreurRequest(
        String nom,
        String email,
        String password,
        String phone,
        Role role
) {}

