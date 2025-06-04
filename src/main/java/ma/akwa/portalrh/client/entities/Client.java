package ma.akwa.portalrh.client.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.akwa.portalrh.auth.entities.User;

@Entity
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Table(name = "clients")
public class Client extends User {

    @Column(name = "address")
    private String address;
}

