package ma.akwa.portalrh.reclamation.mapper;
import ma.akwa.portalrh.reclamation.dto.ReclamationRequest;
import ma.akwa.portalrh.reclamation.dto.ReclamationResponse;
import ma.akwa.portalrh.reclamation.entities.Reclamation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReclamationMapper {

    Reclamation toEntity(ReclamationRequest request);
    ReclamationResponse toResponse(Reclamation reclamation);
}
