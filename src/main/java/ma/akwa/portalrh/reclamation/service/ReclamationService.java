package ma.akwa.portalrh.reclamation.service;

import ma.akwa.portalrh.produit.dto.ProduitRequest;
import ma.akwa.portalrh.produit.dto.ProduitResponse;
import ma.akwa.portalrh.reclamation.dto.ReclamationRequest;
import ma.akwa.portalrh.reclamation.dto.ReclamationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReclamationService {

        ReclamationResponse create(ReclamationRequest request);
        ReclamationResponse update(Long id, ReclamationRequest request);
        void delete(Long id);
        ReclamationResponse getById(Long id);
        Page<ReclamationResponse> getAllPaginated(Pageable pageable);
    }

