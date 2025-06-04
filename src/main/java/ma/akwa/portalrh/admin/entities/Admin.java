package ma.akwa.portalrh.admin.entities;

import jakarta.persistence.Entity;
import lombok.*;
import ma.akwa.portalrh.auth.entities.User;
@Entity
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Admin extends User {
    private String telephone;
}
