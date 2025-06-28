package ma.akwa.portalrh.produit.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.akwa.portalrh.common.enums.Type;
import ma.akwa.portalrh.reclamation.entities.Reclamation;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produits")
public class Produit  {

    @Id
    private String id;

    @Column
    private String nom;
    @Column
    private float prixUnitaire;
    @Column
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column
    private Type type;
    @Column
    private String description;
    @Column
    private String image;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
