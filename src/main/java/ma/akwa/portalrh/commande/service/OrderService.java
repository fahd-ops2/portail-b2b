package ma.akwa.portalrh.commande.service;

import jakarta.transaction.Transactional;
import ma.akwa.portalrh.commande.dto.OrderRequestDTO;
import ma.akwa.portalrh.commande.dto.OrderResponseDTO;
import ma.akwa.portalrh.common.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO dto);

    Page<OrderResponseDTO> listOrdersByCompany();

    OrderResponseDTO getOrder(Long orderId);

    OrderResponseDTO cancelOrder(Long orderId);

    @Transactional
    OrderResponseDTO updateStatus(Long orderId, OrderStatus newStatus);
}
