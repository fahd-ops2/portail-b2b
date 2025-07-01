package ma.akwa.portalrh.commande.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.client.repository.ClientRepository;
import ma.akwa.portalrh.commande.dto.OrderRequestDTO;
import ma.akwa.portalrh.commande.dto.OrderResponseDTO;
import ma.akwa.portalrh.commande.entities.Order;
import ma.akwa.portalrh.commande.entities.OrderItem;
import ma.akwa.portalrh.commande.mapper.OrderMapper;
import ma.akwa.portalrh.commande.repository.OrderRepository;
import ma.akwa.portalrh.common.enums.OrderStatus;
import ma.akwa.portalrh.produit.entities.Produit;
import ma.akwa.portalrh.produit.repository.ProduitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto){
        // Valider le client
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

        // Mapper le DTO vers l'entité
        Order order = orderMapper.toOrder(dto);
        order.setClient(client);

        // Valider et configurer les OrderItems
        if (dto.items() != null) {
            for (OrderItem item : order.getItems()) {
                Produit produit = produitRepository.findById(item.getProduit().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé: " + item.getProduit().getId()));
                item.setProduit(produit);
                item.setOrder(order);
                item.setUnitPrice((double) produit.getPrixUnitaire()); // Suppose que Produit a une méthode getPrix()
                item.setSubtotal(item.getQuantity() * item.getUnitPrice());
            }
            // Calculer total_amount
            order.setTotalAmount(order.getItems().stream()
                    .mapToDouble(OrderItem::getSubtotal)
                    .sum());
        } else {
            order.setItems(new ArrayList<>());
        }
        // Définir le statut par défaut si non fourni
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.EN_ATTENTE);
        }

        // Sauvegarder la commande
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDTO(savedOrder);


    }

    @Override
    public Page<OrderResponseDTO> listOrdersByCompany() {
        // TODO: Implémenter le filtrage par entreprise (ex. via SecurityContextHolder ou champ dans Client)
        return orderRepository.findAll(PageRequest.of(0, 10))
                .map(orderMapper::toOrderResponseDTO);
    }

    @Transactional
    @Override
    public OrderResponseDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée"));
        return orderMapper.toOrderResponseDTO(order);
    }

    @Transactional
    @Override
    public OrderResponseDTO cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée"));

        if (order.getStatus() == OrderStatus.LIVREE || order.getStatus() == OrderStatus.ANNULEE) {
            throw new IllegalStateException("Impossible d'annuler une commande déjà livrée ou annulée");
        }

        order.setStatus(OrderStatus.ANNULEE);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    @Transactional
    @Override
    public OrderResponseDTO updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée"));

        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new IllegalStateException("Transition de statut invalide : " + order.getStatus() + " -> " + newStatus);
        }

        order.setStatus(newStatus);
        if (newStatus == OrderStatus.LIVREE) {
            order.setDeliveredAt(LocalDateTime.now());
        }
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case EN_ATTENTE:
                return next == OrderStatus.EN_COURS || next == OrderStatus.ANNULEE;
            case EN_COURS:
                return next == OrderStatus.LIVREE || next == OrderStatus.ANNULEE;
            case LIVREE:
            case ANNULEE:
                return false; // Pas de transition possible
            default:
                return false;
        }
    }
}
