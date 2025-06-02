package ma.akwa.portalrh.modules.livraison.repository;

import ma.akwa.portalrh.modules.livraison.entities.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
}
