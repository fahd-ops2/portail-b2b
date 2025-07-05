package ma.akwa.portalrh.livraison.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.akwa.portalrh.commande.repository.OrderRepository;
import ma.akwa.portalrh.common.enums.DeliveryStatus;
import ma.akwa.portalrh.livraison.dto.DeliveryRequest;
import ma.akwa.portalrh.livraison.dto.DeliveryResponse;
import ma.akwa.portalrh.livraison.entities.Delivery;
import ma.akwa.portalrh.livraison.mapper.DeliveryMapper;
import ma.akwa.portalrh.commande.entities.Order;
import ma.akwa.portalrh.livraison.repository.DeliveryRepository;
import ma.akwa.portalrh.livraison.service.DeliveryService;
import ma.akwa.portalrh.livreur.entities.Livreur;
import ma.akwa.portalrh.livreur.repository.LivreurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final LivreurRepository livreurRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public DeliveryResponse create(@Valid DeliveryRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new EntityNotFoundException("Commande avec l'ID " + request.orderId() + " non trouvée"));
        Livreur livreur = livreurRepository.findById(request.livreurId())
                .orElseThrow(() -> new EntityNotFoundException("Livreur avec l'ID " + request.livreurId() + " non trouvé"));

        Delivery delivery = deliveryMapper.toEntity(request);
        delivery.setOrder(order);
        delivery.setLivreur(livreur);

        delivery = deliveryRepository.save(delivery);
        return deliveryMapper.toResponse(delivery);
    }

    @Override
    public DeliveryResponse update(Long id, @Valid DeliveryRequest request) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livraison avec l'ID " + id + " non trouvée"));

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new EntityNotFoundException("Commande avec l'ID " + request.orderId() + " non trouvée"));
        Livreur livreur = livreurRepository.findById(request.livreurId())
                .orElseThrow(() -> new EntityNotFoundException("Livreur avec l'ID " + request.livreurId() + " non trouvé"));

        delivery.setOrder(order);
        delivery.setLivreur(livreur);
        delivery.setScheduledTime(request.scheduledTime());
        delivery.setStatus(request.status());
        delivery.setDeliveryAddress(request.deliveryAddress());
        delivery.setNotes(request.notes());
        delivery.setIsUrgent(request.isUrgent() != null ? request.isUrgent() : delivery.getIsUrgent());

        if (request.status() == DeliveryStatus.DELIVERED && delivery.getDeliveredAt() == null) {
            delivery.setDeliveredAt(LocalDateTime.now());
        }

        delivery = deliveryRepository.save(delivery);
        return deliveryMapper.toResponse(delivery);
    }

    @Override
    public void delete(Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new EntityNotFoundException("Livraison avec l'ID " + id + " non trouvée");
        }
        deliveryRepository.deleteById(id);
    }

    @Override
    public DeliveryResponse getById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livraison avec l'ID " + id + " non trouvée"));
        return deliveryMapper.toResponse(delivery);
    }

    @Override
    public Page<DeliveryResponse> getAllPaginated(Pageable pageable) {
        Page<Delivery> deliveryPage = deliveryRepository.findAll(pageable);
        return deliveryPage.map(deliveryMapper::toResponse);
    }

    @Override
    public void updateDeliveryStatus(Long id, String trackingCode) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livraison avec l'ID " + id + " non trouvée"));
        // Mise à jour manuelle du statut sans appel à l'API TrackingMore
        delivery.setStatus(DeliveryStatus.IN_PROGRESS); // Statut par défaut, à ajuster selon vos besoins
        if (delivery.getStatus() == DeliveryStatus.DELIVERED && delivery.getDeliveredAt() == null) {
            delivery.setDeliveredAt(LocalDateTime.now());
        }
        deliveryRepository.save(delivery);
    }

    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}