package ma.akwa.portalrh.order.api.controller;

import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.order.api.dto.OrderRequestDTO;
import ma.akwa.portalrh.order.api.dto.OrderResponseDTO;
import ma.akwa.portalrh.order.domain.service.OrderService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        return orderService.createOrder(orderRequestDTO);
    }

    @GetMapping
    public Page<OrderResponseDTO> getOrdersByCompany(){
        return orderService.listOrdersByCompany();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long orderId){
        return orderService.getOrder(orderId);
    }

    @GetMapping("/{id}/cancel")
    public OrderResponseDTO cancelOrder(@PathVariable Long orderId){
        return orderService.cancelOrder(orderId);
    }
}
