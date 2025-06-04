package ma.akwa.portalrh.livreur.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.akwa.portalrh.auth.entities.User;



@Entity
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Table(name = "livreurs")
public class Livreur extends User {

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "is_active")
    private Boolean available = true;
}
