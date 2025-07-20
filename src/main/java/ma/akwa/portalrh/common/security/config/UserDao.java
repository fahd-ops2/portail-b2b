package ma.akwa.portalrh.common.security.config;

import lombok.RequiredArgsConstructor;
import ma.akwa.portalrh.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDao {

    private final UserRepository userRepository;


    public UserDetails findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
