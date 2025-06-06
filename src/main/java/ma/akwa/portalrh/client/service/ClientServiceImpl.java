package ma.akwa.portalrh.client.service;

import lombok.AllArgsConstructor;
import ma.akwa.portalrh.client.dto.ClientRequest;
import ma.akwa.portalrh.client.dto.ClientResponse;
import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.client.mapper.ClientMapper;
import ma.akwa.portalrh.client.repository.ClientRepository;
import ma.akwa.portalrh.produit.dto.ProduitRequest;
import ma.akwa.portalrh.produit.dto.ProduitResponse;
import ma.akwa.portalrh.produit.entities.Produit;
import ma.akwa.portalrh.produit.mapper.ProduitMapper;
import ma.akwa.portalrh.produit.repository.ProduitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;


    @Override
    public ClientResponse create(ClientRequest request) {
        Client client = clientMapper.toEntity(request);
        Client createdClient = clientRepository.save(client);
        return clientMapper.toResponse(createdClient);
    }

    @Override
    public ClientResponse update(Long id, ClientRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ClientResponse getById(Long id) {
        return null;
    }

    @Override
    public Page<ClientResponse> getAllPaginated(Pageable pageable) {
        return null;
    }

}