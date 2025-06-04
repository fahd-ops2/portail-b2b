package ma.akwa.portalrh.commande.entities;



import jakarta.persistence.*;
import lombok.*;
import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "scheduled_for")
    private LocalDateTime scheduledFor;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "total_amount")
    private Double totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
}
