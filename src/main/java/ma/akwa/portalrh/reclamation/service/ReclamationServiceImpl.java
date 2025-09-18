package ma.akwa.portalrh.reclamation.service;

import lombok.AllArgsConstructor;
import ma.akwa.portalrh.common.enums.ReclamationStatus;
import ma.akwa.portalrh.common.enums.TypeRc;
import ma.akwa.portalrh.reclamation.dto.ReclamationRequest;
import ma.akwa.portalrh.reclamation.dto.ReclamationResponse;
import ma.akwa.portalrh.reclamation.entities.Reclamation;
import ma.akwa.portalrh.reclamation.mapper.ReclamationMapper;
import ma.akwa.portalrh.reclamation.repository.ReclamationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReclamationServiceImpl implements ReclamationService {
    private static final Logger logger = LoggerFactory.getLogger(ReclamationServiceImpl.class);
    private final ReclamationRepository reclamationRepository;
    private final ReclamationMapper reclamationMapper;

    @Override
    public ReclamationResponse create(ReclamationRequest request, MultipartFile file) {
        logger.info("Création d'une nouvelle réclamation pour le client ID: {}", request.getClientId());
        validateReclamationRequest(request);

        if (request.getStatus() == null) {
            request.setStatus(ReclamationStatus.EN_ATTENTE);
        }
        if (request.getDate() == null) {
            request.setDate(LocalDate.now());
        }

        Reclamation reclamation = reclamationMapper.toEntity(request);

        if (file != null && !file.isEmpty()) {
            String filePath = saveFile(file);
            reclamation.setFilePath(filePath);
            logger.debug("Fichier enregistré pour la réclamation: {}", filePath);
        }

        Reclamation createdReclamation = reclamationRepository.save(reclamation);
        logger.info("Réclamation créée avec l'ID: {}", createdReclamation.getId());
        return reclamationMapper.toResponse(createdReclamation);
    }

    @Override
    public ReclamationResponse update(Long id, ReclamationRequest request, MultipartFile file) {
        logger.info("Mise à jour de la réclamation ID: {}", id);
        validateReclamationRequest(request);
        Reclamation reclamation = reclamationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation " + id + " non trouvée"));

        reclamationMapper.updateEntityFromRequest(request, reclamation);

        if (file != null && !file.isEmpty()) {
            deleteFileIfExists(reclamation.getFilePath());
            String filePath = saveFile(file);
            reclamation.setFilePath(filePath);
            logger.debug("Nouveau fichier enregistré pour la réclamation ID: {}", id);
        }

        Reclamation updatedReclamation = reclamationRepository.save(reclamation);
        logger.info("Réclamation mise à jour avec l'ID: {}", updatedReclamation.getId());
        return reclamationMapper.toResponse(updatedReclamation);
    }

    @Override
    public void delete(Long id) {
        logger.info("Suppression de la réclamation ID: {}", id);
        Reclamation reclamation = reclamationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation " + id + " non trouvée"));
        deleteFileIfExists(reclamation.getFilePath());
        reclamationRepository.delete(reclamation);
        logger.info("Réclamation ID: {} supprimée", id);
    }

    @Override
    public ReclamationResponse getById(Long id) {
        logger.info("Récupération de la réclamation ID: {}", id);
        Reclamation reclamation = reclamationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation " + id + " non trouvée"));
        return reclamationMapper.toResponse(reclamation);
    }

    @Override
    public Page<ReclamationResponse> getAllPaginated(Pageable pageable) {
        logger.info("Récupération de toutes les réclamations avec pagination");
        Page<Reclamation> reclamations = reclamationRepository.findAll(pageable);
        return reclamations.map(reclamationMapper::toResponse);
    }

    @Override
    public Page<ReclamationResponse> getFilteredReclamations(
            Long clientId,
            TypeRc type,
            ReclamationStatus status,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {
        logger.info("Récupération des réclamations filtrées pour clientId: {}, type: {}, status: {}", clientId, type, status);
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;
        Page<Reclamation> reclamations = reclamationRepository.findByFilters(clientId, type, status, startDateTime, endDateTime, pageable);
        return reclamations.map(reclamationMapper::toResponse);
    }

    @Override
    public byte[] getFile(Long id) {
        logger.info("Récupération du fichier pour la réclamation ID: {}", id);
        Reclamation reclamation = reclamationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation " + id + " non trouvée"));
        String filePath = reclamation.getFilePath();
        if (filePath == null || filePath.isBlank()) {
            throw new RuntimeException("Aucun fichier associé à cette réclamation");
        }
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("Échec de la lecture du fichier pour la réclamation ID: {}", id, e);
            throw new RuntimeException("Échec de la lecture du fichier", e);
        }
    }

    private void validateReclamationRequest(ReclamationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La requête de réclamation ne peut pas être nulle");
        }
        if (request.getClientId() == null) {
            throw new IllegalArgumentException("L'identifiant du client est requis");
        }
        try {
            TypeRc.valueOf(request.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de réclamation invalide : " + request.getType());
        }
    }

    private String saveFile(MultipartFile file) {
        try {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("Le fichier est trop volumineux (max 10MB)");
            }
            String[] allowedTypes = {"image/jpeg", "image/png", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
            if (file.getContentType() != null && !Arrays.asList(allowedTypes).contains(file.getContentType())) {
                throw new IllegalArgumentException("Type de fichier non autorisé : " + file.getContentType());
            }

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

    private void deleteFileIfExists(String filePath) {
        if (filePath != null && !filePath.isBlank()) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                logger.warn("Échec de la suppression du fichier : {}", filePath, e);
            }
        }
    }
}