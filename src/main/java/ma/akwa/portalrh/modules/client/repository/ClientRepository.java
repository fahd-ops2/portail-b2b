package ma.akwa.portalrh.modules.client.repository;

import ma.akwa.portalrh.modules.client.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
