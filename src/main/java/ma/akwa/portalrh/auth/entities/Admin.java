package ma.akwa.portalrh.auth.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "admin")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("ROLE_ADMIN")
public class Admin extends User {
    private String telephone;
}
