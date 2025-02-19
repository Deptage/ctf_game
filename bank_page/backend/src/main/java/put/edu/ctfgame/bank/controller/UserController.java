package put.edu.ctfgame.bank.controller;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import put.edu.ctfgame.bank.dto.BankUserDTO;
import put.edu.ctfgame.bank.service.JwtService;
import put.edu.ctfgame.bank.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BankUserDTO> authenticatedUser() {
        return ResponseEntity.ok(userService.findAuthenticated());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BankUserDTO>> allUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

        
    @GetMapping("/flag")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> getFlag(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        var token = request.getHeader("Authorization").substring(7);
        var username = jwtService.extractUsername(token);
        if (!Objects.equals(username, "hacker")){
            response.put("status", "Nope.");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("status", "TGHM{1_g0_v4_b4nqu3}");
        return ResponseEntity.ok(response);
    }
}
