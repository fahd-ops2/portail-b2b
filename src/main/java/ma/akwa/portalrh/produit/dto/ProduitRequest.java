package ma.akwa.portalrh.produit.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProduitRequest {
    private String nom;
    private float prixUnitaire;
    private int stock;
}
