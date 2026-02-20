package ma.akwa.portalrh.livraison.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.common.enums.DeliveryStatus;
import ma.akwa.portalrh.livraison.dto.DeliveryRequest;
import ma.akwa.portalrh.livraison.dto.DeliveryResponse;
import ma.akwa.portalrh.livraison.service.DeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
@Tag(name = "Delivery API", description = "API pour la gestion des livraisons")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Créer une nouvelle livraison")
    @ApiResponse(responseCode = "200", description = "Livraison créée avec succès")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryResponse> create(
            @Parameter(description = "Données de la livraison à créer", required = true)
            @Valid @RequestBody DeliveryRequest request) {
        return ResponseEntity.ok(deliveryService.create(request));
    }

    @Operation(summary = "Mettre à jour une livraison existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livraison mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Livraison non trouvée")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIVREUR')")
    public ResponseEntity<DeliveryResponse> update(
            @Parameter(description = "ID de la livraison à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Données mises à jour de la livraison", required = true)
            @Valid @RequestBody DeliveryRequest request) {
        return ResponseEntity.ok(deliveryService.update(id, request));
    }

    @Operation(summary = "Supprimer une livraison")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livraison supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Livraison non trouvée")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la livraison à supprimer", required = true)
            @PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer une livraison par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livraison récupérée"),
            @ApiResponse(responseCode = "404", description = "Livraison non trouvée")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN') or hasRole('LIVREUR')")
    public ResponseEntity<DeliveryResponse> getById(
            @Parameter(description = "ID de la livraison à récupérer", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getById(id));
    }

    @Operation(summary = "Récupérer une page de livraisons")
    @ApiResponse(responseCode = "200", description = "Liste paginée des livraisons récupérée avec succès")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIVREUR')")
    public ResponseEntity<Page<DeliveryResponse>> getAllPaginated(
            @Parameter(description = "Taille de la page", example = "10", required = true)
            @RequestParam int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0", required = true)
            @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(deliveryService.getAllPaginated(pageable));
    }

    @Operation(summary = "Récupérer les livraisons par statut")
    @ApiResponse(responseCode = "200", description = "Liste des livraisons pour le statut spécifié")
    @GetMapping("/by-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIVREUR')")
    public ResponseEntity<Page<DeliveryResponse>> getByStatus(
            @Parameter(description = "Statut de la livraison", required = true)
            @RequestParam DeliveryStatus status,
            @Parameter(description = "Taille de la page", example = "10", required = true)
            @RequestParam int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0", required = true)
            @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(deliveryService.getByStatus(status, pageable));
    }

    @Operation(summary = "Rechercher des livraisons par ID de commande ou adresse")
    @ApiResponse(responseCode = "200", description = "Liste paginée des livraisons correspondant aux critères de recherche")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIVREUR')")
    public ResponseEntity<Page<DeliveryResponse>> searchDeliveries(
            @Parameter(description = "Terme de recherche (ID de commande ou adresse)", required = false)
            @RequestParam(required = false) String searchTerm,
            @Parameter(description = "Taille de la page", example = "10", required = true)
            @RequestParam int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0", required = true)
            @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(deliveryService.searchDeliveries(searchTerm, pageable));
    }

    @Operation(summary = "Mettre à jour le statut d'une livraison")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Statut de la livraison mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Livraison non trouvée")
    })
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIVREUR')")
    public ResponseEntity<Void> updateDeliveryStatus(
            @Parameter(description = "ID de la livraison à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouveau statut de la livraison", required = true)
            @RequestParam DeliveryStatus status) {
        deliveryService.updateDeliveryStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}