package ma.akwa.portalrh.commande.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.commande.dto.BulkOrderRequestDTO;
import ma.akwa.portalrh.commande.dto.OrderRequestDTO;
import ma.akwa.portalrh.commande.dto.OrderResponseDTO;
import ma.akwa.portalrh.commande.service.OrderService;
import ma.akwa.portalrh.common.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Commande API", description = "API pour la gestion des commandes")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Créer une nouvelle commande")
    @ApiResponse(responseCode = "200", description = "Commande créée avec succès")
    @PostMapping
    public OrderResponseDTO createOrder(
            @Parameter(description = "Détails de la commande à créer", required = true)
            @RequestBody OrderRequestDTO orderRequestDTO){
        return orderService.createOrder(orderRequestDTO);
    }

    @Operation(summary = "Créer plusieurs commandes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commandes créées avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de la requête invalides")
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<OrderResponseDTO>> createOrders(
            @Parameter(description = "Liste des commandes à créer", required = true)
            @RequestBody BulkOrderRequestDTO bulkOrderRequestDTO) {
        List<OrderResponseDTO> responses = orderService.createOrders(bulkOrderRequestDTO);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lister les commandes de l'entreprise")
    @ApiResponse(responseCode = "200", description = "Liste des commandes récupérée avec succès")
    @GetMapping
    public Page<OrderResponseDTO> getOrdersByCompany() {
        return orderService.listOrdersByCompany();
    }


    @Operation(summary = "Obtenir une commande par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commande récupérée"),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée")
    })
    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(
            @Parameter(description = "ID de la commande à récupérer", required = true)
            @PathVariable("id") Long orderId){
        return orderService.getOrder(orderId);
    }

    @Operation(summary = "Annuler une commande par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commande annulée avec succès"),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée"),
            @ApiResponse(responseCode = "400", description = "Impossible d'annuler la commande")
    })
    @PutMapping("/{id}/cancel")
    public OrderResponseDTO cancelOrder(
            @Parameter(description = "ID de la commande à annuler", required = true)
            @PathVariable("id") Long orderId){
        return orderService.cancelOrder(orderId);
    }

    @Operation(summary = "Mettre à jour le statut d'une commande")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée"),
            @ApiResponse(responseCode = "400", description = "Transition de statut invalide")
    })
    @PutMapping("/{id}/status")
    public OrderResponseDTO updateStatus(
            @Parameter(description = "ID de la commande", required = true)
            @PathVariable("id") Long orderId,
            @Parameter(description = "Nouveau statut de la commande", required = true)
            @RequestBody String status) {
        OrderStatus orderStatus = OrderStatus.valueOf( status);
        return orderService.updateStatus(orderId, orderStatus);
    }
}
