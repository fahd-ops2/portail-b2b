package ma.akwa.portalrh.produit.service;

import ma.akwa.portalrh.produit.dto.ProduitRequest;
import ma.akwa.portalrh.produit.dto.ProduitResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProduitService {
    ProduitResponse create(ProduitRequest request);
    ProduitResponse update(String id, ProduitRequest request);
    void delete(String id);
    ProduitResponse getById(String id);
    Page<ProduitResponse> getAllPaginated(Pageable pageable);
}
