package ma.akwa.portalrh.commande.repository;

import ma.akwa.portalrh.commande.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
