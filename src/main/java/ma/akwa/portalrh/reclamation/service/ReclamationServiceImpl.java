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

@Service
@AllArgsConstructor
public class ReclamationServiceImpl implements ReclamationService {
    private final ReclamationRepository reclamationRepository;
    private final ReclamationMapper reclamationMapper;
    @Override
    public ReclamationResponse create(ReclamationRequest request) {
        Reclamation reclamation  = reclamationMapper.toEntity(request);
        Reclamation createdReclamation = reclamationRepository.save(reclamation);
        return reclamationMapper.toResponse(createdReclamation);
    }

    @Override
    public ReclamationResponse update(Long id, ReclamationRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {
        Reclamation reclamation = reclamationRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("reclamation "+ id +" not found"));
        reclamationRepository.delete(reclamation);

    }

    @Override
    public ReclamationResponse getById(Long id) {
        Reclamation reclamation = reclamationRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("reclamation "+ id +" not found"));;
        return reclamationMapper.toResponse(reclamation);
    }

    @Override
    public Page<ReclamationResponse> getAllPaginated(Pageable pageable) {
        Page<Reclamation> reclamations = reclamationRepository.findAll(pageable);
        return reclamations.map(reclamationMapper::toResponse);
    }
}
