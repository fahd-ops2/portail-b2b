package ma.akwa.portalrh.client.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.client.dto.ClientRequest;
import ma.akwa.portalrh.client.dto.ClientResponse;
import ma.akwa.portalrh.client.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Client API", description = "API pour la gestion des clients")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Créer un nouveau client")
    @ApiResponse(responseCode = "200", description = "Client créé avec succès")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> create(
            @Parameter(description = "Données du client à créer", required = true)
            @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.create(request));
    }

    @Operation(summary = "Mettre à jour un client existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> update(
            @Parameter(description = "ID du client à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Données mises à jour du client", required = true)
            @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.update(id, request));
    }

    @Operation(summary = "Supprimer un client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du client à supprimer", required = true)
            @PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "Récupérer un client par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client récupéré"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(
            @Parameter(description = "ID du client à récupérer", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(clientService.getById(id));
    }
   /* @GetMapping("/{id}")
    public ClientResponse getOrderById(
            @Parameter(description = "ID de la commande à récupérer", required = true)
            @PathVariable("id") Long id){
        return clientService.getById(id);
    }*/



    @Operation(summary = "Récupérer une page de clients")
    @ApiResponse(responseCode = "200", description = "Liste paginée des clientts récupérée avec succès")
    @GetMapping
    public ResponseEntity<Page<ClientResponse>> getAllPaginated(
            @Parameter(description = "Taille de la page", example = "10", required = true)
            @RequestParam int size,
            @Parameter(description = "Numéro de la page (0 = première page)", example = "0", required = true)
            @RequestParam int page) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(clientService.getAllPaginated(pageable));
    }




}
