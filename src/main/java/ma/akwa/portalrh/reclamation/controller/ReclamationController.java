package ma.akwa.portalrh.reclamation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.common.enums.ReclamationStatus;
import ma.akwa.portalrh.reclamation.dto.ReclamationRequest;
import ma.akwa.portalrh.reclamation.dto.ReclamationResponse;
import ma.akwa.portalrh.reclamation.service.ReclamationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/v1/reclamation")
@RequiredArgsConstructor
@Tag(name = "Reclamation API", description = "API pour la gestion des reclamations")
public class ReclamationController {

    private final ReclamationService reclamationService;

    @Operation(summary = "Créer une nouvelle réclamation", description = "Crée une réclamation avec ou sans fichier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réclamation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "415", description = "Type de média non supporté"),
            @ApiResponse(responseCode = "404", description = "Commande ou client non trouvé")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReclamationResponse> create(
            @Parameter(description = "ID de la commande", required = true)
            @RequestPart(name = "orderId") @NotBlank String orderId,
            @Parameter(description = "Description de la réclamation", required = true)
            @RequestPart(name = "description") @NotBlank String description,
            @Parameter(description = "Type de réclamation", required = true)
            @RequestPart(name = "type") @NotBlank String type,
            @Parameter(description = "Statut de la réclamation", required = true)
            @RequestPart(name = "status") @NotNull String status,
            @Parameter(description = "ID du client", required = true)
            @RequestPart(name = "clientId") @NotNull String clientId,
            @Parameter(description = "Date de création", required = true)
            @RequestPart(name = "date") @NotNull String date,
            @Parameter(description = "Notes de résolution (optionnel)")
            @RequestPart(name = "resolutionNotes", required = false) String resolutionNotes,
            @Parameter(description = "Fichier optionnel (image, PDF, etc.)", required = false)
            @RequestPart(name = "file", required = false) MultipartFile file) {
        try {
            ReclamationRequest request = ReclamationRequest.builder()
                    .orderId(orderId)
                    .description(description)
                    .type(type)
                    .status(ReclamationStatus.valueOf(status))
                    .clientId(Long.parseLong(clientId))
                    .date(LocalDate.parse(date))
                    .resolutionNotes(resolutionNotes)
                    .build();
            return ResponseEntity.ok(reclamationService.create(request, file));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide, attendu YYYY-MM-DD", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("clientId doit être un nombre valide", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide: " + status, e);
        }
    }

    @Operation(summary = "Mettre à jour une réclamation existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réclamation mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Réclamation non trouvée")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReclamationResponse> update(
            @Parameter(description = "ID de la réclamation à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID de la commande", required = true)
            @RequestPart(name = "orderId") @NotBlank String orderId,
            @Parameter(description = "Description de la réclamation", required = true)
            @RequestPart(name = "description") @NotBlank String description,
            @Parameter(description = "Type de réclamation", required = true)
            @RequestPart(name = "type") @NotBlank String type,
            @Parameter(description = "Statut de la réclamation", required = true)
            @RequestPart(name = "status") @NotNull String status,
            @Parameter(description = "ID du client", required = true)
            @RequestPart(name = "clientId") @NotNull String clientId,
            @Parameter(description = "Date de création", required = true)
            @RequestPart(name = "date") @NotNull String date,
            @Parameter(description = "Notes de résolution (optionnel)")
            @RequestPart(name = "resolutionNotes", required = false) String resolutionNotes,
            @Parameter(description = "Fichier optionnel (image, PDF, etc.)", required = false)
            @RequestPart(name = "file", required = false) MultipartFile file) {
        try {
            ReclamationRequest request = ReclamationRequest.builder()
                    .orderId(orderId)
                    .description(description)
                    .type(type)
                    .status(ReclamationStatus.valueOf(status))
                    .clientId(Long.parseLong(clientId))
                    .date(LocalDate.parse(date))
                    .resolutionNotes(resolutionNotes)
                    .build();
            return ResponseEntity.ok(reclamationService.update(id, request, file));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide, attendu YYYY-MM-DD", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("clientId doit être un nombre valide", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide: " + status, e);
        }
    }

    @Operation(summary = "Supprimer une réclamation")
    @ApiResponse(responseCode = "204", description = "Réclamation supprimée avec succès")
    @DeleteMapping("/{id}")
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
    public ResponseEntity<ReclamationResponse> getById(
            @Parameter(description = "ID de la réclamation à récupérer", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.getById(id));
    }

    @Operation(summary = "Récupérer une page des réclamations")
    @ApiResponse(responseCode = "200", description = "Liste paginée des réclamations récupérée avec succès")
    @GetMapping
    public ResponseEntity<Page<ReclamationResponse>> getAllPaginated(
            @Parameter(description = "Taille de la page", example = "10", required = true)
            @RequestParam int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0", required = true)
            @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reclamationService.getAllPaginated(pageable));
    }
}