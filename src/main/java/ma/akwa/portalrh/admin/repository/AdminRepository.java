package ma.akwa.portalrh.admin.repository;

import ma.akwa.portalrh.auth.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
