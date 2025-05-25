package ma.akwa.portalrh.order.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private Long companyId;

    private LocalDateTime createdAt;

    private String status; // VALIDATED, PREPARING, SHIPPED, DELIVERED

    private boolean recurring;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
}
