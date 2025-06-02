package ma.akwa.portalrh.modules.produit.repository;

import ma.akwa.portalrh.modules.produit.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
