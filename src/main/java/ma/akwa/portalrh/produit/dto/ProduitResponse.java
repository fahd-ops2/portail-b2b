package ma.akwa.portalrh.produit.dto;

import lombok.*;
import ma.akwa.portalrh.common.enums.Type;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProduitResponse {
    private String id;
    private String nom;
    private float prixUnitaire;
    private int stock;
    private String description;
    private Type type;
    private String image;
}
