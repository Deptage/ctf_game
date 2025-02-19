package put.edu.ctfgame.bank.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.bank.dto.BankUserDTO;
import put.edu.ctfgame.bank.dto.ExistsResponse;
import put.edu.ctfgame.bank.entity.BankUser;
import put.edu.ctfgame.bank.exception.UserAlreadyExistsException;
import put.edu.ctfgame.bank.exception.UserNotFoundException;
import put.edu.ctfgame.bank.repository.RoleRepository;
import put.edu.ctfgame.bank.repository.SusyUserRepository;
import put.edu.ctfgame.bank.repository.UserRepository;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final SusyUserRepository susyUserRepository;

    public BankUserDTO signup(BankUserDTO input) {
        log.info("Signing up user {}", input.getUsername());
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            log.warn("Signup failed: User {} already exists", input.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }
        var userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
        var user = BankUser.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .roles(Set.of(userRole))
                .build();
        var savedUser = userRepository.save(user);
        log.info("Signed up user {}", input.getUsername());

        return BankUserDTO.from(savedUser);
    }

    public BankUser authenticate(BankUserDTO input) {
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

    // Used for SQLin, deprecated
    public BankUser susyAuthenticate(BankUserDTO input) {
        log.info("Attempting to authenticate user with username: {}", input.getUsername());
        var user = susyUserRepository.findByCredentials(input.getUsername(), input.getPassword())
                .orElseThrow(() -> {
                    log.error("Invalid login attempt for username: {}", input.getUsername());
                    return new UserNotFoundException("Invalid username or password");
                });
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            log.error("Authentication failed for username: {}", input.getUsername());
            throw new UserNotFoundException(e.getMessage());
        }
        return user;
    }

    public ExistsResponse exists(String username) {
        log.info("Checking if user with username {} exists", username);
        return new ExistsResponse(susyUserRepository.existsByUsername(username));
    }
}