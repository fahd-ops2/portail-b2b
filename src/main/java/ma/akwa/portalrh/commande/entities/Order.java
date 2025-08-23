package ma.akwa.portalrh.commande.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.EN_ATTENTE;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "scheduled_for")
    private LocalDateTime scheduledFor;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}