package ma.akwa.portalrh.livraison.repository;

import ma.akwa.portalrh.livraison.entities.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
}
