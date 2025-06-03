package ma.akwa.portalrh.produit.controller;

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
public class ProduitController {

    private final ProduitService produitService;

    @PostMapping
    public ResponseEntity<ProduitResponse> create(@RequestBody ProduitRequest request) {
        return ResponseEntity.ok(produitService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitResponse> update(@PathVariable Long id, @RequestBody ProduitRequest request) {
        return ResponseEntity.ok(produitService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(produitService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProduitResponse>> getAllPaginated(@RequestParam int size, @RequestParam int page) {
        Pageable pageable = PageRequest.of(size, page);
        return ResponseEntity.ok(produitService.getAllPaginated(pageable));
    }
}
