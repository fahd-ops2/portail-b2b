package ma.akwa.portalrh.commande.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.akwa.portalrh.produit.entities.Produit;

import java.util.UUID;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(name = "quantity")
    private Integer quantity; // Changé en Integer pour les quantités entières

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}