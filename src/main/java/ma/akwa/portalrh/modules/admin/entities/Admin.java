package ma.akwa.portalrh.modules.admin.entities;

import jakarta.persistence.Entity;
import lombok.*;
import ma.akwa.portalrh.modules.auth.entities.User;
@Entity
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Admin extends User {
    private String telephone;
}
