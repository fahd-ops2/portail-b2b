package ma.akwa.portalrh.produit.service;

import lombok.AllArgsConstructor;
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
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    @Override
    public ProduitResponse create(ProduitRequest request) {
        Produit produit  = produitMapper.toEntity(request);
        Produit createdProduct = produitRepository.save(produit);
        return produitMapper.toResponse(createdProduct);
    }

    @Override
    public ProduitResponse update(Long id, ProduitRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {
        Produit produit = produitRepository
                    .findById(id)
                    .orElseThrow(()-> new RuntimeException("product "+ id +" not found"));
        produitRepository.delete(produit);
    }

    @Override
    public ProduitResponse getById(Long id) {
        Produit produit = produitRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("product "+ id +" not found"));;
        return produitMapper.toResponse(produit);
    }

    @Override
    public Page<ProduitResponse> getAllPaginated(Pageable pageable) {
        Page<Produit> produits = produitRepository.findAll(pageable);
        return produits.map(produitMapper::toResponse);
    }
}
