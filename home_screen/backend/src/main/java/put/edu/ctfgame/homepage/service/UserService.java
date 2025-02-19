package put.edu.ctfgame.homepage.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.homepage.dto.RegisterDTO;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public List<RegisterDTO> findAll() {
        log.info("Fetching all users");
        var users = userRepository.findAll().stream()
                .map(RegisterDTO::from)
                .toList();
        log.info("Fetched {} users", users.size());
        return users;
    }

    public CtfgameUser findAuthenticated() {
        log.info("Fetching authenticated user");
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (CtfgameUser) authentication.getPrincipal();
        log.info("Authenticated user: {}", currentUser.getUsername());
        return currentUser;
    }

    public CtfgameUser findByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new RuntimeException("User not found");
                });
        log.info("Fetched user: {}", user.getUsername());
        return user;
    }
}