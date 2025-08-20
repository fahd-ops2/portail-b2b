package ma.akwa.portalrh.commande.service;

import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.client.repository.ClientRepository;
import ma.akwa.portalrh.commande.dto.BulkOrderRequestDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto){

        System.out.println(dto);
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

        Order order = orderMapper.toOrder(dto);
        order.setClient(client);

        if (Objects.isNull(dto.items())) {
            order.setItems(Collections.emptyList());
        }

        for (OrderItem item : order.getItems()) {

            Produit produit = produitRepository.findById(item.getProduit().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé: " + item.getProduit().getId()));

            item.setProduit(produit);
            item.setOrder(order);
            item.setUnitPrice((double) produit.getPrixUnitaire());
            item.setSubtotal(item.getQuantity() * item.getUnitPrice());

        }

        order.setTotalAmount(order.getItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum());

        if (Objects.isNull(order.getStatus())) {
            order.setStatus(OrderStatus.EN_ATTENTE);
        }

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDTO(savedOrder);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> listOrdersByCompany() {
        // TODO: Implémenter le filtrage par entreprise (ex. via SecurityContextHolder ou champ dans Client)
        return orderRepository.findAll(PageRequest.of(0, 10))
                .map(orderMapper::toOrderResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée"));
        return orderMapper.toOrderResponseDTO(order);
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(String orderId){
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
    public OrderResponseDTO updateStatus(String orderId, OrderStatus newStatus) {
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
        return switch (current) {
            case EN_ATTENTE -> next == OrderStatus.EN_COURS || next == OrderStatus.ANNULEE;
            case EN_COURS -> next == OrderStatus.LIVREE || next == OrderStatus.ANNULEE;
            default -> false;
        };
    }
    @Transactional
    @Override
    public List<OrderResponseDTO> createOrders(BulkOrderRequestDTO bulkDto) {
        return bulkDto.orders().stream()
                .map(this::createOrder)
                .collect(Collectors.toList());
    }
}
