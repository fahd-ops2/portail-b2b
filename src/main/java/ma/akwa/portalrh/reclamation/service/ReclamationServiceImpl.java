package ma.akwa.portalrh.reclamation.service;

import lombok.AllArgsConstructor;
import ma.akwa.portalrh.reclamation.dto.ReclamationRequest;
import ma.akwa.portalrh.reclamation.dto.ReclamationResponse;
import ma.akwa.portalrh.reclamation.entities.Reclamation;
import ma.akwa.portalrh.reclamation.mapper.ReclamationMapper;
import ma.akwa.portalrh.reclamation.repository.ReclamationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReclamationServiceImpl implements ReclamationService {
    private final ReclamationRepository reclamationRepository;
    private final ReclamationMapper reclamationMapper;

    @Override
    public ReclamationResponse create(ReclamationRequest request, MultipartFile file) {
        Reclamation reclamation = reclamationMapper.toEntity(request);

        if (file != null && !file.isEmpty()) {
            String filePath = saveFile(file);
            reclamation.setFilePath(filePath);
        }

        Reclamation createdReclamation = reclamationRepository.save(reclamation);
        return reclamationMapper.toResponse(createdReclamation);
    }

    @Override
    public ReclamationResponse update(Long id, ReclamationRequest request, MultipartFile file) {
        Reclamation reclamation = reclamationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation " + id + " non trouvée"));

        reclamationMapper.updateEntityFromRequest(request, reclamation);

        if (file != null && !file.isEmpty()) {
            String filePath = saveFile(file);
            reclamation.setFilePath(filePath);
        }

        Reclamation updatedReclamation = reclamationRepository.save(reclamation);
        return reclamationMapper.toResponse(updatedReclamation);
    }

    @Override
    public void delete(Long id) {
        Reclamation reclamation = reclamationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation " + id + " non trouvée"));
        reclamationRepository.delete(reclamation);
    }

    @Override
    public ReclamationResponse getById(Long id) {
        Reclamation reclamation = reclamationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation " + id + " non trouvée"));
        return reclamationMapper.toResponse(reclamation);
    }

    @Override
    public Page<ReclamationResponse> getAllPaginated(Pageable pageable) {
        Page<Reclamation> reclamations = reclamationRepository.findAll(pageable);
        return reclamations.map(reclamationMapper::toResponse);
    }

    @Override
    public Object create(ReclamationRequest request) {
        return null;
    }

    @Override
    public Object update(Long id, ReclamationRequest request) {
        return null;
    }

    private String saveFile(MultipartFile file) {
        try {
            String uploadDir = "uploads/reclamations/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'enregistrement du fichier", e);
        }
    }
}