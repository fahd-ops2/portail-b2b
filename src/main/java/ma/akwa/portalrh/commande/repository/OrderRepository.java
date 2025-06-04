package ma.akwa.portalrh.commande.repository;

import ma.akwa.portalrh.commande.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
