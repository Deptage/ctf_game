package put.edu.ctfgame.bank.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import put.edu.ctfgame.bank.dto.BankUserDTO;
import put.edu.ctfgame.bank.dto.ExistsResponse;
import put.edu.ctfgame.bank.dto.LoginResponse;
import put.edu.ctfgame.bank.dto.UsernameDTO;
import put.edu.ctfgame.bank.service.AuthService;
import put.edu.ctfgame.bank.service.JwtService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<BankUserDTO> register(@Valid @RequestBody BankUserDTO input) {
        return ResponseEntity.ok(authService.signup(input));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody BankUserDTO input) {
        var user = authService.authenticate(input);
        var token = jwtService.generateToken(user);
        var response = LoginResponse.builder()
                .token(token)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists")
    public ResponseEntity<ExistsResponse> exists(@RequestParam String username) {
        return ResponseEntity.ok(authService.exists(username));
    }
}
