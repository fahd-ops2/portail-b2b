package ma.akwa.portalrh.reclamation.service;

import ma.akwa.portalrh.common.enums.ReclamationStatus;
import ma.akwa.portalrh.common.enums.TypeRc;
import ma.akwa.portalrh.reclamation.dto.ReclamationRequest;
import ma.akwa.portalrh.reclamation.dto.ReclamationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface ReclamationService {
        ReclamationResponse create(ReclamationRequest request, MultipartFile file);
        ReclamationResponse update(Long id, ReclamationRequest request, MultipartFile file);
        void delete(Long id);
        ReclamationResponse getById(Long id);
        Page<ReclamationResponse> getAllPaginated(Pageable pageable);
        Page<ReclamationResponse> getFilteredReclamations(
                Long clientId,
                TypeRc type,
                ReclamationStatus status,
                LocalDate startDate,
                LocalDate endDate,
                Pageable pageable);
        byte[] getFile(Long id);
}