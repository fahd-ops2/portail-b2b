package ma.akwa.portalrh.admin.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import ma.akwa.portalrh.admin.dto.AdminRequest;
import ma.akwa.portalrh.admin.dto.AdminResponse;
import ma.akwa.portalrh.auth.entities.Admin;
import ma.akwa.portalrh.admin.mapper.AdminMapper;
import ma.akwa.portalrh.admin.repository.AdminRepository;
import ma.akwa.portalrh.common.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminResponse create(AdminRequest request) {
        Admin admin = adminMapper.toEntity(request);
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ROLE_ADMIN); // Définir le rôle par défaut pour les admins
        admin.setLocked(false); // Compte non verrouillé par défaut
        admin.setEnabled(true); // Compte activé par défaut
        Admin createdAdmin = adminRepository.save(admin);
        return adminMapper.toResponse(createdAdmin);
    }

    @Override
    public AdminResponse update(Long id, AdminRequest request) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin avec l'ID " + id + " non trouvé"));
        admin.setNom(request.getNom());
        admin.setEmail(request.getEmail());
        admin.setTelephone(request.getTelephone());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        Admin updatedAdmin = adminRepository.save(admin);
        return adminMapper.toResponse(updatedAdmin);
    }

    @Override
    public void delete(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new EntityNotFoundException("Admin avec l'ID " + id + " non trouvé");
        }
        adminRepository.deleteById(id);
    }

    @Override
    public AdminResponse getById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin avec l'ID " + id + " non trouvé"));
        return adminMapper.toResponse(admin);
    }

    @Override
    public Page<AdminResponse> getAllPaginated(Pageable pageable) {
        Page<Admin> adminPage = adminRepository.findAll(pageable);
        return adminPage.map(adminMapper::toResponse);
    }
}