package ma.akwa.portalrh.client.service;

import ma.akwa.portalrh.client.dto.ClientRequest;
import ma.akwa.portalrh.client.dto.ClientResponse;
import ma.akwa.portalrh.produit.dto.ProduitRequest;
import ma.akwa.portalrh.produit.dto.ProduitResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ClientService {
    ClientResponse create(ClientRequest request);
    ClientResponse update(Long id, ClientRequest request);
    void delete(Long id);
    ClientResponse getById(Long id);
    Page<ClientResponse> getAllPaginated(Pageable pageable);
}