package ma.akwa.portalrh.client.mapper;

import ma.akwa.portalrh.client.dto.ClientRequest;
import ma.akwa.portalrh.client.dto.ClientResponse;
import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.produit.dto.ProduitResponse;
import ma.akwa.portalrh.produit.entities.Produit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientRequest request);
    ClientResponse toResponse(Client client);
}
