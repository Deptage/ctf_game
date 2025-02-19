package put.edu.ctfgame.bank.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import put.edu.ctfgame.bank.entity.BankUser;
import put.edu.ctfgame.bank.entity.Role;
import put.edu.ctfgame.bank.repository.RoleRepository;
import put.edu.ctfgame.bank.repository.UserRepository;

import java.util.Set;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        var adminRole = Role.builder()
                .name("ROLE_ADMIN")
                .build();
        var userRole = Role.builder()
                .name("ROLE_USER")
                .build();
        var savedAdmin = roleRepository.save(adminRole);
        var savedUser = roleRepository.save(userRole);
        var user = BankUser.builder()
                .username("hacker")
                .password(passwordEncoder.encode("dragon")) // user
                .roles(Set.of(savedUser))
                .build();
        var flag = BankUser.builder()
                .username("TGHM{m3m0ry_l34ks_th3_s4m3_as_y0ur_m0n3y}")
                .password(passwordEncoder.encode("po34podl;dfpkl;eswopikqw23p")) // user
                .roles(Set.of(savedUser))
                .build();
        var admin = BankUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("@@cde3$RFV@@")) // admin
                .roles(Set.of(savedAdmin))
                .build();
        userRepository.save(user);
        userRepository.save(admin);
        userRepository.save(flag);
    }
}
