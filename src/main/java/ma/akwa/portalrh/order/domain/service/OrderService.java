package ma.akwa.portalrh.order.domain.service;

import ma.akwa.portalrh.order.api.dto.OrderRequestDTO;
import ma.akwa.portalrh.order.api.dto.OrderResponseDTO;
import org.springframework.data.domain.Page;


import java.util.Optional;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO dto);

    Page<OrderResponseDTO> listOrdersByCompany();

    OrderResponseDTO getOrder(Long orderId);

    OrderResponseDTO cancelOrder(Long orderId);
}
