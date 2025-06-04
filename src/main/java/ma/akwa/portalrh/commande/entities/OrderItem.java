package ma.akwa.portalrh.commande.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.akwa.portalrh.produit.entities.Produit;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Produit produit;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
