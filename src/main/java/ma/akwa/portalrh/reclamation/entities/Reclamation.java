package ma.akwa.portalrh.reclamation.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ma.akwa.portalrh.commande.entities.Order;
import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.common.enums.ReclamationStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "reclamation")
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "La commande ne peut pas être nulle")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Le client ne peut pas être nul")
    private Client client;

    @NotBlank(message = "La description ne peut pas être vide")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Le type de réclamation ne peut pas être vide")
    @Column
    private String type;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le statut ne peut pas être nul")
    @Column
    private ReclamationStatus status = ReclamationStatus.EN_ATTENTE;

    @NotNull(message = "La date de création ne peut pas être nulle")
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column
    private String filePath; // Champ pour stocker le chemin du fichier

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}