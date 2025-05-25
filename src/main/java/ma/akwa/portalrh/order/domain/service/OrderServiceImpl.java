package ma.akwa.portalrh.order.domain.service;

import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.order.api.dto.OrderRequestDTO;
import ma.akwa.portalrh.order.api.dto.OrderResponseDTO;
import ma.akwa.portalrh.order.domain.repository.OrderRepository;
import ma.akwa.portalrh.order.internal.mapper.OrderMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto){
        return null;
    }

    @Override
    public Page<OrderResponseDTO> listOrdersByCompany() {
        return new PageImpl<>(List.of());
    }

    @Override
    public OrderResponseDTO getOrder(Long orderId) {
        return null;
    }

    @Override
    public OrderResponseDTO cancelOrder(Long orderId){
        return null;
    }
}
