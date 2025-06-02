package ma.akwa.portalrh.modules.client.entities;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.akwa.portalrh.modules.auth.entities.User;
@Entity
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Client extends User {
    private String address;

}

