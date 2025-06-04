package ma.akwa.portalrh.livreur.mapper;


import ma.akwa.portalrh.livreur.dto.LivreurRequest;
import ma.akwa.portalrh.livreur.dto.LivreurResponse;
import ma.akwa.portalrh.livreur.entities.Livreur;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LivreurMapper {

    LivreurResponse toResponse(Livreur livreur);

    List<LivreurResponse> toResponseList(List<Livreur> livreurs);

    Livreur toEntity(LivreurRequest request);

    void updateEntityFromRequest(LivreurRequest request, @MappingTarget Livreur livreur);
}

