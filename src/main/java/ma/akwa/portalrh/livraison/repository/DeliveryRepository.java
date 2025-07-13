package ma.akwa.portalrh.livraison.repository;

import ma.akwa.portalrh.common.enums.DeliveryStatus;
import ma.akwa.portalrh.livraison.entities.Delivery;
import ma.akwa.portalrh.livreur.entities.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {


        // Trouver toutes les livraisons par statut
        List<Delivery> findByStatus(DeliveryStatus status);

        // Trouver les livraisons d'un livreur spécifique
        List<Delivery> findByLivreur(Livreur livreur);

        // Trouver les livraisons planifiées entre deux dates
        List<Delivery> findByScheduledTimeBetween(LocalDateTime start, LocalDateTime end);

        // Trouver les livraisons pour une commande spécifique
        Delivery findByOrderId(String orderId);
}
