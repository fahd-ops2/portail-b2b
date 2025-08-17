package ma.akwa.portalrh.reclamation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.reclamation.dto.ReclamationRequest;
import ma.akwa.portalrh.reclamation.dto.ReclamationResponse;
import ma.akwa.portalrh.reclamation.service.ReclamationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reclamation")
@RequiredArgsConstructor
@Tag(name = "Reclamation API", description = "API pour la gestion des reclamations")
public class ReclamationController {

    private final ReclamationService reclamationService;

    @Operation(summary = "Créer un nouvelle reclamation")
    @ApiResponse(responseCode = "200", description = "Reclamation créé avec succès")
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ReclamationResponse> create(
            @Parameter(description = "Données du reclamation à créer", required = true)
            @RequestBody ReclamationRequest request) {
        return ResponseEntity.ok((ReclamationResponse) reclamationService.create(request));
    }

    @Operation(summary = "Mettre à jour un produit existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ReclamationResponse> update(
            @Parameter(description = "ID du reclamation à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Données mises à jour du reclamation", required = true)
            @RequestBody ReclamationRequest request) {
        return ResponseEntity.ok((ReclamationResponse) reclamationService.update(id, request));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du reclamation à supprimer", required = true)
            @PathVariable Long id) {
        reclamationService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Récupérer une reclamation par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamation récupéré"),
            @ApiResponse(responseCode = "404", description = "Reclamation non trouvé")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<ReclamationResponse> getById(
            @Parameter(description = "ID du reclamation à récupérer", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.getById(id));
    }

    @Operation(summary = "Récupérer une page des reclamations")
    @ApiResponse(responseCode = "200", description = "Liste paginée des reclamations récupérée avec succès")
    @GetMapping
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<Page<ReclamationResponse>> getAllPaginated(
            @Parameter(description = "Taille de la page", example = "10", required = true)
            @RequestParam int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0", required = true)
            @RequestParam int page) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reclamationService.getAllPaginated(pageable));
    }


}