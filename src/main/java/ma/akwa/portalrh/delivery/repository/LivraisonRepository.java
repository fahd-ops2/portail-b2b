package ma.akwa.portalrh.delivery.repository;

import ma.akwa.portalrh.delivery.entities.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
}
