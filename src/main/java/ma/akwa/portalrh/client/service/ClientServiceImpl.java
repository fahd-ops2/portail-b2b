package ma.akwa.portalrh.client.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import ma.akwa.portalrh.client.dto.ClientRequest;
import ma.akwa.portalrh.client.dto.ClientResponse;
import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.client.mapper.ClientMapper;
import ma.akwa.portalrh.client.repository.ClientRepository;
import ma.akwa.portalrh.common.enums.Role;
import ma.akwa.portalrh.produit.dto.ProduitRequest;
import ma.akwa.portalrh.produit.dto.ProduitResponse;
import ma.akwa.portalrh.produit.entities.Produit;
import ma.akwa.portalrh.produit.mapper.ProduitMapper;
import ma.akwa.portalrh.produit.repository.ProduitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ClientResponse create(ClientRequest request) {
        Client client = clientMapper.toEntity(request);
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setRole(Role.ROLE_CLIENT); // Définir le rôle par défaut pour les clients
        client.setLocked(false); // Compte non verrouillé par défaut
        client.setEnabled(true); // Compte activé par défaut
        Client createdClient = clientRepository.save(client);
        return clientMapper.toResponse(createdClient);
    }

    @Override
    public ClientResponse update(Long id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client avec l'ID " + id + " non trouvé"));
        client.setNom(request.getNom()); // Supposant que nomClient mappe à nom
        client.setEmail(request.getEmail());
        client.setAddress(request.getAddress());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            client.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        Client updatedClient = clientRepository.save(client);
        return clientMapper.toResponse(updatedClient);

    }

    @Override
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client avec l'ID " + id + " non trouvé");
        }
        clientRepository.deleteById(id);

    }

    @Override
    public ClientResponse getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client avec l'ID " + id + " non trouvé"));
        return clientMapper.toResponse(client);
    }
    @Override
    public Page<ClientResponse> getAllPaginated(Pageable pageable) {
        Page<Client> clientPage = clientRepository.findAll(pageable);
        return clientPage.map(clientMapper::toResponse);
    }

    }



