package ma.akwa.portalrh.livreur.dto;

import ma.akwa.portalrh.common.enums.Role;

public record LivreurResponse(
        Long id,
        String nom,
        String email,
        String phone,
        Boolean available,
        Role role
) {}

