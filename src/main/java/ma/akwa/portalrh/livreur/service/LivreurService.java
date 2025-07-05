package ma.akwa.portalrh.livreur.service;

import ma.akwa.portalrh.client.dto.ClientResponse;
import ma.akwa.portalrh.livreur.dto.LivreurRequest;
import ma.akwa.portalrh.livreur.dto.LivreurResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LivreurService {

    List<LivreurResponse> findAll();

    LivreurResponse findById(Long id);

    LivreurResponse create(LivreurRequest request);

    LivreurResponse update(Long id, LivreurRequest request);

    void delete(Long id);

    LivreurResponse toggleAvailability(Long id);


}
