package ma.akwa.portalrh.modules.reclamation.repository;

import ma.akwa.portalrh.modules.produit.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Produit, Long> {
}
