package ma.akwa.portalrh.reclamation.repository;

import ma.akwa.portalrh.common.enums.ReclamationStatus;
import ma.akwa.portalrh.common.enums.TypeRc;
import ma.akwa.portalrh.reclamation.entities.Reclamation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    @Query("SELECT r FROM Reclamation r WHERE " +
            "(:clientId IS NULL OR r.client.id = :clientId) AND " +
            "(:type IS NULL OR r.type = :type) AND " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(:startDate IS NULL OR r.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR r.createdAt <= :endDate)")
    Page<Reclamation> findByFilters(
            @Param("clientId") Long clientId,
            @Param("type") TypeRc type,
            @Param("status") ReclamationStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}