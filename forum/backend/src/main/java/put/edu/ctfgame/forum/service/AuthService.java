package put.edu.ctfgame.forum.service;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.forum.dto.ForumUserDTO;
import put.edu.ctfgame.forum.dto.PostAuthorDTO;
import put.edu.ctfgame.forum.entity.ForumUser;
import put.edu.ctfgame.forum.exception.UserAlreadyExistsException;
import put.edu.ctfgame.forum.exception.UserNotFoundException;
import put.edu.ctfgame.forum.repository.RoleRepository;
import put.edu.ctfgame.forum.repository.UserRepository;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public PostAuthorDTO signup(ForumUserDTO input) {
        log.info("Signing up user {}", input.getUsername());
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            log.warn("Signup failed: User {} already exists", input.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }
        var userRole = roleRepository.findByName("USER").orElseThrow();
        var user = ForumUser.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .roles(Set.of(userRole))
                .build();
        var savedUser = userRepository.save(user);
        log.info("Signed up user {}", input.getUsername());

        return PostAuthorDTO.from(savedUser);
    }

    public ForumUser authenticate(ForumUserDTO input) {
        log.info("Authenticating user with username: {}", input.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            log.error("Authentication failed for username: {}", input.getUsername());
            throw new UserNotFoundException(e.getMessage());
        }

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> {
                    log.error("User with username {} not found in the database", input.getUsername());
                    return new UserNotFoundException("User not found");
                });
    }


    public Cookie userRoleCookie() {
        Cookie cookie = new Cookie("role", "ROLE_USER");
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        return cookie;
    }

    public Cookie emptyRoleCookie() {
        Cookie cookie = new Cookie("role", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        return cookie;
    }

    public void verifyAdminAccessByCookieRole(String role) throws AccessDeniedException {
        if (!role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Insufficient permissions");
        }
    }
}