package ma.akwa.portalrh.livreur.service;


import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.common.enums.Role;
import ma.akwa.portalrh.livreur.dto.LivreurRequest;
import ma.akwa.portalrh.livreur.dto.LivreurResponse;
import ma.akwa.portalrh.livreur.entities.Livreur;
import ma.akwa.portalrh.livreur.mapper.LivreurMapper;
import ma.akwa.portalrh.livreur.repository.LivreurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LivreurServiceImpl implements LivreurService {

    private final LivreurRepository livreurRepository;
    private final LivreurMapper livreurMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<LivreurResponse> findAll() {
        List<Livreur> livreurs = livreurRepository.findAll();
        return livreurMapper.toResponseList(livreurs);
    }

    @Override
    public LivreurResponse findById(Long id) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Livreur not found with id " + id));
        return livreurMapper.toResponse(livreur);
    }

    @Override
    public LivreurResponse create(LivreurRequest request) {
        Livreur livreur = livreurMapper.toEntity(request);
        livreur.setPassword(passwordEncoder.encode(request.password()));
        livreur.setRole(Role.LIVREUR); // Définir le rôle par défaut pour les clients
        livreur.setLocked(false); // Compte non verrouillé par défaut
        livreur.setEnabled(true); // Compte activé par défaut
        Livreur createdClient = livreurRepository.save(livreur);
        livreur = livreurRepository.save(livreur);
        return livreurMapper.toResponse(livreur);
    }

    @Override
    public LivreurResponse update(Long id, LivreurRequest request) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Livreur not found with id " + id));
        livreurMapper.updateEntityFromRequest(request, livreur);
        livreur = livreurRepository.save(livreur);
        return livreurMapper.toResponse(livreur);
    }

    @Override
    public void delete(Long id) {
        if (!livreurRepository.existsById(id)) {
            throw new NoSuchElementException("Livreur not found with id " + id);
        }
        livreurRepository.deleteById(id);
    }

    @Override
    public LivreurResponse toggleAvailability(Long id) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Livreur not found with id " + id));
        livreur.setAvailable(!Boolean.TRUE.equals(livreur.getAvailable()));
        livreur = livreurRepository.save(livreur);
        return livreurMapper.toResponse(livreur);
    }
}

