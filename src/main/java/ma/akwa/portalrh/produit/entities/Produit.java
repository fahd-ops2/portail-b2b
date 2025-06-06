package ma.akwa.portalrh.produit.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.akwa.portalrh.reclamation.entities.Reclamation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produits")
public class Produit  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String nom;
    @Column
    private float prixUnitaire;
    @Column
    private int stock;
}
