package ma.akwa.portalrh.livraison.repository;

import ma.akwa.portalrh.common.enums.DeliveryStatus;
import ma.akwa.portalrh.livraison.entities.Delivery;
import ma.akwa.portalrh.livreur.entities.Livreur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

        Page<Delivery> findByStatus(DeliveryStatus status, Pageable pageable);

        List<Delivery> findByLivreur(Livreur livreur);

        List<Delivery> findByScheduledTimeBetween(LocalDateTime start, LocalDateTime end);

        Delivery findByOrder_Id(String orderId);

        Page<Delivery> findByOrderIdContainingIgnoreCaseOrDeliveryAddressContainingIgnoreCase(
                String orderId, String deliveryAddress, Pageable pageable);
}