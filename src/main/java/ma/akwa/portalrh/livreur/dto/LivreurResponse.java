package ma.akwa.portalrh.livreur.dto;

public record LivreurResponse(
        Long id,
        String username,
        String phone,
        Boolean available
) {}

