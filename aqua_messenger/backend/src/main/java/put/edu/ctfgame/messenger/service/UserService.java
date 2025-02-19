package put.edu.ctfgame.messenger.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import put.edu.ctfgame.messenger.entity.User;
import put.edu.ctfgame.messenger.repository.UserRepository;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository usersRepo;

    public User getUserByName(String username) {
        log.info("Getting user by username: {}", username);
        return usersRepo.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
    }

    public User findAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        log.info("Fetching authenticated user: {}", currentUser.getUsername());
        return usersRepo.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> {
                    log.error("Authenticated user not found: {}", currentUser.getUsername());
                    return new RuntimeException("User not found"); //TODO special exception
                });
    }
}