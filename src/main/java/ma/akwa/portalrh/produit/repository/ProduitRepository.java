package ma.akwa.portalrh.produit.repository;

import ma.akwa.portalrh.produit.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    boolean existsByNom(String nom);
}
