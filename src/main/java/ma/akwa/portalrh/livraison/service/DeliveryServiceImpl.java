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
import ma.akwa.portalrh.livreur.entities.Livreur;
import ma.akwa.portalrh.livreur.repository.LivreurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryServiceImpl.class);

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final LivreurRepository livreurRepository;
    private final DeliveryMapper deliveryMapper;

    private static final Map<DeliveryStatus, List<DeliveryStatus>> VALID_STATUS_TRANSITIONS = new HashMap<>();

    static {
        VALID_STATUS_TRANSITIONS.put(DeliveryStatus.SCHEDULED, Arrays.asList(DeliveryStatus.IN_PROGRESS, DeliveryStatus.CANCELLED));
        VALID_STATUS_TRANSITIONS.put(DeliveryStatus.IN_PROGRESS, Arrays.asList(DeliveryStatus.DELIVERED, DeliveryStatus.CANCELLED, DeliveryStatus.FAILED));
        VALID_STATUS_TRANSITIONS.put(DeliveryStatus.DELIVERED, Arrays.asList());
        VALID_STATUS_TRANSITIONS.put(DeliveryStatus.CANCELLED, Arrays.asList());
        VALID_STATUS_TRANSITIONS.put(DeliveryStatus.FAILED, Arrays.asList());
    }

    @Override
    public DeliveryResponse create(@Valid DeliveryRequest request) {
        logger.info("Création d'une nouvelle livraison pour la commande ID {}", request.orderId());

        Order order = orderRepository.findById(String.valueOf(request.orderId()))
                .orElseThrow(() -> new EntityNotFoundException("Commande avec l'ID " + request.orderId() + " non trouvée"));
        Livreur livreur = livreurRepository.findById(request.livreurId())
                .orElseThrow(() -> new EntityNotFoundException("Livreur avec l'ID " + request.livreurId() + " non trouvé"));

        Delivery delivery = deliveryMapper.toEntity(request);
        delivery.setOrder(order);
        delivery.setLivreur(livreur);

        delivery = deliveryRepository.save(delivery);
        logger.info("Livraison créée avec l'ID {}", delivery.getId());
        return deliveryMapper.toResponse(delivery);
    }

    @Override
    public DeliveryResponse update(Long id, @Valid DeliveryRequest request) {
        logger.info("Mise à jour de la livraison ID {}", id);

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livraison avec l'ID " + id + " non trouvée"));

        Order order = orderRepository.findById(String.valueOf(request.orderId()))
                .orElseThrow(() -> new EntityNotFoundException("Commande avec l'ID " + request.orderId() + " non trouvée"));
        Livreur livreur = livreurRepository.findById(request.livreurId())
                .orElseThrow(() -> new EntityNotFoundException("Livreur avec l'ID " + request.livreurId() + " non trouvé"));

        if (!isValidStatusTransition(delivery.getStatus(), request.status())) {
            throw new IllegalStateException(
                    "Transition de statut de " + delivery.getStatus() + " à " + request.status() + " non autorisée");
        }

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
        logger.info("Livraison ID {} mise à jour avec succès", id);
        return deliveryMapper.toResponse(delivery);
    }

    @Override
    public void delete(Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new EntityNotFoundException("Livraison avec l'ID " + id + " non trouvée");
        }
        deliveryRepository.deleteById(id);
        logger.info("Livraison ID {} supprimée avec succès", id);
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
    public Page<DeliveryResponse> getByStatus(DeliveryStatus status, Pageable pageable) {
        logger.info("Récupération des livraisons avec le statut {}", status);
        Page<Delivery> deliveryPage = deliveryRepository.findByStatus(status, pageable);
        return deliveryPage.map(deliveryMapper::toResponse);
    }

    @Override
    public Page<DeliveryResponse> searchDeliveries(String searchTerm, Pageable pageable) {
        logger.info("Recherche des livraisons avec le terme '{}'", searchTerm);
        if (searchTerm == null || searchTerm.isBlank()) {
            return deliveryRepository.findAll(pageable).map(deliveryMapper::toResponse);
        }
        Page<Delivery> deliveryPage = deliveryRepository.findByOrderIdContainingIgnoreCaseOrDeliveryAddressContainingIgnoreCase(
                searchTerm, searchTerm, pageable);
        return deliveryPage.map(deliveryMapper::toResponse);
    }

    @Override
    public void updateDeliveryStatus(Long id, DeliveryStatus status) {
        logger.info("Mise à jour du statut de la livraison ID {} à {}", id, status);

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livraison avec l'ID " + id + " non trouvée"));

        if (!isValidStatusTransition(delivery.getStatus(), status)) {
            throw new IllegalStateException(
                    "Transition de statut de " + delivery.getStatus() + " à " + status + " non autorisée");
        }

        delivery.setStatus(status);
        if (status == DeliveryStatus.DELIVERED && delivery.getDeliveredAt() == null) {
            delivery.setDeliveredAt(LocalDateTime.now());
        }

        deliveryRepository.save(delivery);
        logger.info("Statut de la livraison ID {} mis à jour à {}", id, status);
    }

    private boolean isValidStatusTransition(DeliveryStatus currentStatus, DeliveryStatus newStatus) {
        return VALID_STATUS_TRANSITIONS.getOrDefault(currentStatus, Arrays.asList()).contains(newStatus);
    }
}