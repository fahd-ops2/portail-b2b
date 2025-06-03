package ma.akwa.portalrh.common.security.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDao {

    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final static List<UserDetails> APPLICATION_USERS = Arrays.asList(
            new User(
                    "fahdboua@gmail.com",
                    encoder.encode("password"),
                    Collections.emptyList()
            ),
            new User(
                    "zyadboua@gmail.com",
                    encoder.encode("password"),
                    Collections.emptyList()
            )
    );

    public UserDetails findByEmail(String email){
        return APPLICATION_USERS
                .stream()
                .filter(u -> u.getUsername().equals(email))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("userName doesnt exist"));
    }
}
