package ma.akwa.portalrh.produit.dto;

import lombok.*;
import ma.akwa.portalrh.common.enums.Type;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProduitRequest {
    private String nom;
    private float prixUnitaire;
    private int stock;
    private Type type;
    private String description;
    private String image;
}
