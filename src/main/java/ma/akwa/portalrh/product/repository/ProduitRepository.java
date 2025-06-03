package ma.akwa.portalrh.product.repository;

import ma.akwa.portalrh.product.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
