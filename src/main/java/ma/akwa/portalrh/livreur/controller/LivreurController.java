package ma.akwa.portalrh.livreur.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.livreur.dto.LivreurRequest;
import ma.akwa.portalrh.livreur.dto.LivreurResponse;
import ma.akwa.portalrh.livreur.service.LivreurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/livreurs")
@RequiredArgsConstructor
@Tag(name = "Livreur API", description = "API pour gérer les livreurs")
public class LivreurController {

    private final LivreurService livreurService;

    @Operation(summary = "Récupérer la liste de tous les livreurs")
    @ApiResponse(responseCode = "200", description = "Liste des livreurs récupérée avec succès")
    @GetMapping
    public List<LivreurResponse> getAll() {
        return livreurService.findAll();
    }

    @Operation(summary = "Récupérer un livreur par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livreur trouvé"),
            @ApiResponse(responseCode = "404", description = "Livreur non trouvé")
    })
    @GetMapping("/{id}")
    public LivreurResponse getById(
            @Parameter(description = "ID du livreur à récupérer", required = true)
            @PathVariable Long id) {
        return livreurService.findById(id);
    }

    @Operation(summary = "Créer un nouveau livreur")
    @ApiResponse(responseCode = "201", description = "Livreur créé avec succès")
    @PostMapping
    public ResponseEntity<LivreurResponse> create(
            @Parameter(description = "Données du livreur à créer", required = true)
            @RequestBody LivreurRequest request) {
        return new ResponseEntity<>(livreurService.create(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à jour un livreur existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livreur mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Livreur non trouvé")
    })
    @PutMapping("/{id}")
    public LivreurResponse update(
            @Parameter(description = "ID du livreur à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Données mises à jour du livreur", required = true)
            @RequestBody LivreurRequest request) {
        return livreurService.update(id, request);
    }

    @Operation(summary = "Supprimer un livreur par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livreur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Livreur non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du livreur à supprimer", required = true)
            @PathVariable Long id) {
        livreurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Basculer la disponibilité d'un livreur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilité du livreur mise à jour"),
            @ApiResponse(responseCode = "404", description = "Livreur non trouvé")
    })
    @PatchMapping("/{id}/toggle")
    public LivreurResponse toggleAvailability(
            @Parameter(description = "ID du livreur dont on veut basculer la disponibilité", required = true)
            @PathVariable Long id) {
        return livreurService.toggleAvailability(id);
    }
}
