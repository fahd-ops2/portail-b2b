package ma.akwa.portalrh.reclamation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.common.enums.ReclamationStatus;
import ma.akwa.portalrh.common.enums.TypeRc;
import ma.akwa.portalrh.reclamation.dto.ReclamationRequest;
import ma.akwa.portalrh.reclamation.dto.ReclamationResponse;
import ma.akwa.portalrh.reclamation.service.ReclamationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reclamation")
@RequiredArgsConstructor
@Tag(name = "Reclamation API", description = "API pour la gestion des réclamations")
public class ReclamationController {

    private final ReclamationService reclamationService;

    @Operation(summary = "Créer une nouvelle réclamation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réclamation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de la requête invalides ou fichier trop volumineux"),
            @ApiResponse(responseCode = "404", description = "Commande ou client non trouvé")
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ReclamationResponse> create(
            @Parameter(description = "Données de la réclamation à créer (JSON)", required = true)
            @RequestPart("request") @Valid ReclamationRequest request,
            @Parameter(description = "Fichier associé (optionnel)")
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(reclamationService.create(request, file));
    }

    @Operation(summary = "Mettre à jour une réclamation existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réclamation mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de la requête invalides ou fichier trop volumineux"),
            @ApiResponse(responseCode = "404", description = "Réclamation, commande ou client non trouvé")
    })
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ReclamationResponse> update(
            @Parameter(description = "ID de la réclamation à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Données mises à jour de la réclamation (JSON)", required = true)
            @RequestPart("request") @Valid ReclamationRequest request,
            @Parameter(description = "Nouveau fichier associé (optionnel)")
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(reclamationService.update(id, request, file));
    }

    @Operation(summary = "Supprimer une réclamation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Réclamation supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Réclamation non trouvée")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la réclamation à supprimer", required = true)
            @PathVariable Long id) {
        reclamationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer une réclamation par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réclamation récupérée"),
            @ApiResponse(responseCode = "404", description = "Réclamation non trouvée")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<ReclamationResponse> getById(
            @Parameter(description = "ID de la réclamation à récupérer", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.getById(id));
    }

    @Operation(summary = "Récupérer une page des réclamations")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste paginée des réclamations récupérée avec succès")
    })
    @GetMapping
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<Page<ReclamationResponse>> getAllPaginated(
            @Parameter(description = "Taille de la page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0")
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reclamationService.getAllPaginated(pageable));
    }

    @Operation(summary = "Récupérer une page de réclamations avec filtres")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste paginée des réclamations filtrées récupérée avec succès")
    })
    @GetMapping("/filtered")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<Page<ReclamationResponse>> getFilteredReclamations(
            @Parameter(description = "ID du client (optionnel)")
            @RequestParam(required = false) Long clientId,
            @Parameter(description = "Type de réclamation (optionnel)")
            @RequestParam(required = false) TypeRc type,
            @Parameter(description = "Statut de la réclamation (optionnel)")
            @RequestParam(required = false) ReclamationStatus status,
            @Parameter(description = "Date de début pour la création (optionnel, format yyyy-MM-dd)")
            @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Date de fin pour la création (optionnel, format yyyy-MM-dd)")
            @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "Taille de la page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0")
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reclamationService.getFilteredReclamations(clientId, type, status, startDate, endDate, pageable));
    }

    @Operation(summary = "Récupérer le fichier associé à une réclamation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fichier récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Réclamation ou fichier non trouvé")
    })
    @GetMapping(value = "/{id}/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> getFile(
            @Parameter(description = "ID de la réclamation", required = true)
            @PathVariable Long id) {
        byte[] fileContent = reclamationService.getFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"reclamation-file\"")
                .body(fileContent);
    }
}