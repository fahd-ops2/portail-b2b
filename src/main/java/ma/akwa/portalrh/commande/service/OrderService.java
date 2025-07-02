package ma.akwa.portalrh.commande.service;

import ma.akwa.portalrh.commande.dto.BulkOrderRequestDTO;
import ma.akwa.portalrh.commande.dto.OrderRequestDTO;
import ma.akwa.portalrh.commande.dto.OrderResponseDTO;
import ma.akwa.portalrh.common.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO dto);
    List<OrderResponseDTO> createOrders(BulkOrderRequestDTO bulkDto);

    Page<OrderResponseDTO> listOrdersByCompany();

    OrderResponseDTO getOrder(Long orderId);

    OrderResponseDTO cancelOrder(Long orderId);

    OrderResponseDTO updateStatus(Long orderId, OrderStatus newStatus);
}
