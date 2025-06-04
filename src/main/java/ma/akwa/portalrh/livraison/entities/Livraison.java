package ma.akwa.portalrh.livraison.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.akwa.portalrh.commande.entities.Order;
import ma.akwa.portalrh.common.enums.DeliveryStatus;
import ma.akwa.portalrh.livreur.entities.Livreur;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@Table(name = "livraisons")
public class Livraison {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Order order;

    @ManyToOne
    private Livreur livreur;

    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.SCHEDULED;

    private String deliveryAddress;

    private String notes;

    private LocalDateTime deliveredAt;


}
