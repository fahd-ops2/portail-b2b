package ma.akwa.portalrh.common.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthResource {

    private  final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    @Autowired
    public AuthResource(AuthenticationManager authenticationManager, UserDao userDao, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationtRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        final UserDetails userDetails = userDao.findByEmail(request.getEmail());
        if (userDetails != null){
            return ResponseEntity.ok(jwtUtils.generateToken(userDetails));
        }
        return ResponseEntity.status(400).body("some error has occured");
    }
}
