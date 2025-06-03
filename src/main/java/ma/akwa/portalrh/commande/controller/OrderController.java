package ma.akwa.portalrh.commande.controller;

import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.commande.dto.OrderRequestDTO;
import ma.akwa.portalrh.commande.dto.OrderResponseDTO;

import ma.akwa.portalrh.commande.service.OrderService;
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
