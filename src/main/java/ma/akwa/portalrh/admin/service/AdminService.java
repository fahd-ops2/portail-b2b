package ma.akwa.portalrh.admin.service;

import ma.akwa.portalrh.admin.dto.AdminRequest;
import ma.akwa.portalrh.admin.dto.AdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    AdminResponse create(AdminRequest request);
    AdminResponse update(Long id, AdminRequest request);
    void delete(Long id);
    AdminResponse getById(Long id);
    Page<AdminResponse> getAllPaginated(Pageable pageable);
}
