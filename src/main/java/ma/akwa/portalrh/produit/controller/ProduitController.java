package ma.akwa.portalrh.produit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.produit.dto.ProduitRequest;
import ma.akwa.portalrh.produit.dto.ProduitResponse;
import ma.akwa.portalrh.produit.service.ProduitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/produits")
@RequiredArgsConstructor
@Tag(name = "Produit API", description = "API pour la gestion des produits")
public class ProduitController {

    private final ProduitService produitService;

    @Operation(summary = "Créer un nouveau produit")
    @ApiResponse(responseCode = "200", description = "Produit créé avec succès")
    @PostMapping
    public ResponseEntity<ProduitResponse> create(
            @Parameter(description = "Données du produit à créer", required = true)
            @RequestBody ProduitRequest request) {
        return ResponseEntity.ok(produitService.create(request));
    }

    @Operation(summary = "Mettre à jour un produit existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProduitResponse> update(
            @Parameter(description = "ID du produit à mettre à jour", required = true)
            @PathVariable String id,
            @Parameter(description = "Données mises à jour du produit", required = true)
            @RequestBody ProduitRequest request) {
        return ResponseEntity.ok(produitService.update(id, request));
    }

    @Operation(summary = "Supprimer un produit")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produit supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du produit à supprimer", required = true)
            @PathVariable String id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer un produit par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit récupéré"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProduitResponse> getById(
            @Parameter(description = "ID du produit à récupérer", required = true)
            @PathVariable String id) {
        return ResponseEntity.ok(produitService.getById(id));
    }

    @Operation(summary = "Récupérer une page de produits")
    @ApiResponse(responseCode = "200", description = "Liste paginée des produits récupérée avec succès")
    @GetMapping
    public ResponseEntity<Page<ProduitResponse>> getAllPaginated(
            @Parameter(description = "Taille de la page", example = "10", required = true)
            @RequestParam int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0", required = true)
            @RequestParam int page) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(produitService.getAllPaginated(pageable));
    }
}
