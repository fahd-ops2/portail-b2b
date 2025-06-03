package ma.akwa.portalrh.complaint.repository;

import ma.akwa.portalrh.product.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Produit, Long> {
}
