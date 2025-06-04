package ma.akwa.portalrh.livreur.dto;

public record LivreurRequest(
        String username,
        String email,
        String password,
        String phone
) {}

