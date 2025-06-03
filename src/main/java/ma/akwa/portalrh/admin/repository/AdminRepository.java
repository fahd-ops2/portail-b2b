package ma.akwa.portalrh.admin.repository;

import ma.akwa.portalrh.client.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Client, Long> {
}
