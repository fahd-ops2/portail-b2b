package ma.akwa.portalrh.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.admin.dto.AdminRequest;
import ma.akwa.portalrh.admin.dto.AdminResponse;
import ma.akwa.portalrh.admin.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "API pour la gestion des administrateurs")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Créer un nouvel administrateur")
    @ApiResponse(responseCode = "200", description = "Administrateur créé avec succès")
    @PostMapping
    public ResponseEntity<AdminResponse> create(
            @Parameter(description = "Données de l'administrateur à créer", required = true)
            @RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminService.create(request));
    }

    @Operation(summary = "Mettre à jour un administrateur existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Administrateur mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> update(
            @Parameter(description = "ID de l'administrateur à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Données mises à jour de l'administrateur", required = true)
            @RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminService.update(id, request));
    }

    @Operation(summary = "Supprimer un administrateur")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Administrateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de l'administrateur à supprimer", required = true)
            @PathVariable Long id) {
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer un administrateur par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Administrateur récupéré"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> getById(
            @Parameter(description = "ID de l'administrateur à récupérer", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(adminService.getById(id));
    }

    @Operation(summary = "Récupérer une page d'administrateurs")
    @ApiResponse(responseCode = "200", description = "Liste paginée des administrateurs récupérée avec succès")
    @GetMapping
    public ResponseEntity<Page<AdminResponse>> getAllPaginated(
            @Parameter(description = "Taille de la page", example = "10", required = true)
            @RequestParam int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0", required = true)
            @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getAllPaginated(pageable));
    }
}