package ma.akwa.portalrh.livraison.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ma.akwa.portalrh.commande.entities.Order;
import ma.akwa.portalrh.common.enums.DeliveryStatus;
import ma.akwa.portalrh.livreur.entities.Livreur;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@Table(name = "livraisons")
public class Delivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull(message = "La commande ne peut pas être nulle")
    private Order order;

    @ManyToOne
    @NotNull(message = "Le livreur ne peut pas être nul")
    private Livreur livreur;

    @NotNull(message = "L'heure prévue ne peut pas être nulle")
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le statut ne peut pas être nul")
    private DeliveryStatus status = DeliveryStatus.SCHEDULED;

    @NotBlank(message = "L'adresse de livraison ne peut pas être vide")
    @Column(length = 255)
    private String deliveryAddress;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime deliveredAt;

    private Boolean isUrgent = false; // Priorité de livraison


   /* @Column(unique = true)
    private String trackingCode; // Code de suivi unique

    private Double latitude; // Coordonnée géographique
    private Double longitude;*/


}
