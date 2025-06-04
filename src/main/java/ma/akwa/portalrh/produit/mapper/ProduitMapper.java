package ma.akwa.portalrh.produit.mapper;

import ma.akwa.portalrh.produit.dto.ProduitRequest;
import ma.akwa.portalrh.produit.dto.ProduitResponse;
import ma.akwa.portalrh.produit.entities.Produit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProduitMapper {
    Produit toEntity(ProduitRequest request);
    ProduitResponse toResponse(Produit produit);
}
