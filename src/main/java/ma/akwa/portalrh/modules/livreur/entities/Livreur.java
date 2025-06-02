package ma.akwa.portalrh.modules.livreur.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.akwa.portalrh.modules.auth.entities.User;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class Livreur extends User {
    private String telephone;
}
