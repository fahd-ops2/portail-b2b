package ma.akwa.portalrh.modules.auth.repository;

import jakarta.transaction.Transactional;
import ma.akwa.portalrh.modules.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
