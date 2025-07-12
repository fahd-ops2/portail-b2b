package ma.akwa.portalrh.admin.repository;

import ma.akwa.portalrh.admin.entities.Admin;
import ma.akwa.portalrh.client.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
