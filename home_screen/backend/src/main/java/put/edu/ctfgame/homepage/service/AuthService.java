package put.edu.ctfgame.homepage.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.homepage.dto.LoginInputDTO;
import put.edu.ctfgame.homepage.dto.RegisterDTO;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.entity.score.Score;
import put.edu.ctfgame.homepage.entity.score.ScoreKey;
import put.edu.ctfgame.homepage.repository.LevelRepository;
import put.edu.ctfgame.homepage.repository.UserRepository;
import put.edu.ctfgame.homepage.exception.UserAlreadyExistsException;
import put.edu.ctfgame.homepage.util.MailJsonHandler;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final LevelRepository levelRepository;
    private final MailJsonHandler mailJsonHandler;

    public RegisterDTO signup(RegisterDTO input) {
        log.info("Attempting to register user with username: {}", input.getUsername());

        if (userRepository.existsAny()) {
            log.error("User already exists");
            throw new UserAlreadyExistsException("User already exists");
        }

        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            log.error("User already exists with username: {}", input.getUsername());
            throw new UserAlreadyExistsException("User already exists with username: " + input.getUsername());
        }

        var user = CtfgameUser.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .university(input.getUniversity())
                .build();

        var savedUser = userRepository.save(user);
        mailJsonHandler.newUserInitMailStates(savedUser);
        log.info("User registered successfully with username: {}", savedUser.getUsername());
        return RegisterDTO.from(savedUser);
    }

    public CtfgameUser authenticate(LoginInputDTO input) {
        log.info("Attempting to authenticate user with username: {}", input.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );
        var user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", input.getUsername());
                    return new RuntimeException("User not found");
                });
        log.info("User authenticated successfully with username: {}", user.getUsername());
        return user;
    }
}