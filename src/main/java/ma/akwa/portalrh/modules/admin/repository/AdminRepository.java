package ma.akwa.portalrh.modules.admin.repository;

import ma.akwa.portalrh.modules.client.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Client, Long> {
}
