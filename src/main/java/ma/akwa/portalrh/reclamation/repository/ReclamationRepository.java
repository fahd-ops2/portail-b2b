package ma.akwa.portalrh.reclamation.repository;

import ma.akwa.portalrh.produit.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Produit, Long> {
}
