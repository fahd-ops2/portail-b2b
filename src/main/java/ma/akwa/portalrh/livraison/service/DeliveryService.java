package ma.akwa.portalrh.livraison.service;


import ma.akwa.portalrh.common.enums.DeliveryStatus;
import ma.akwa.portalrh.livraison.dto.DeliveryRequest;
import ma.akwa.portalrh.livraison.dto.DeliveryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryService {
        DeliveryResponse create(DeliveryRequest request);
        DeliveryResponse update(Long id, DeliveryRequest request);
        void delete(Long id);
        DeliveryResponse getById(Long id);
        Page<DeliveryResponse> getAllPaginated(Pageable pageable);
        void updateDeliveryStatus(Long id, DeliveryStatus status);


}

