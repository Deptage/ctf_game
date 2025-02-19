package put.edu.ctfgame.bank.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.bank.dto.BankUserDTO;
import put.edu.ctfgame.bank.entity.BankUser;
import put.edu.ctfgame.bank.exception.UserNotFoundException;
import put.edu.ctfgame.bank.repository.UserRepository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public List<BankUserDTO> findAll() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(BankUserDTO::from)
                .toList();
    }

    public BankUserDTO findAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (BankUser) authentication.getPrincipal();
        log.info("Fetching authenticated user: {}", currentUser.getUsername());
        var user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> {
                    log.error("Authenticated user not found: {}", currentUser.getUsername());
                    return new UserNotFoundException("User not found");
                });
        return BankUserDTO.from(user);
    }

    public BankUser findByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found");
                });
    }
}
