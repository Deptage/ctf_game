package put.edu.ctfgame.messenger.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.messenger.DTO.UserDTO;
import put.edu.ctfgame.messenger.entity.User;
import put.edu.ctfgame.messenger.repository.UserRepository;
import put.edu.ctfgame.messenger.config.AuthConfig.*;

import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> signUpBulk(List<UserDTO> inputs) {
        var users = inputs.stream()
                .map(input -> User.builder()
                        .username(input.getUsername())
                        .password(passwordEncoder.encode(input.getPassword()))
                        .isBot(input.getIsBot())
                        .build())
                .toList();
        var savedUsers = userRepository.saveAll(users);
        log.info("Users registered successfully");
        return savedUsers.stream()
                .map(UserDTO::from)
                .toList();
    }

    public User authenticate(UserDTO input){
        var user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", input.getUsername());
                    return new RuntimeException("User not found");
                });

        log.info("Attempting to authenticate user with username: {}", input.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        log.info("User authenticated successfully with username: {}", user.getUsername());
        return user;
    }
}
