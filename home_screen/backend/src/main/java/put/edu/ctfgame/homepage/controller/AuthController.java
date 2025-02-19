package put.edu.ctfgame.homepage.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import put.edu.ctfgame.homepage.dto.LoginInputDTO;
import put.edu.ctfgame.homepage.dto.LoginResponse;
import put.edu.ctfgame.homepage.dto.RegisterDTO;
import put.edu.ctfgame.homepage.service.AuthService;
import put.edu.ctfgame.homepage.service.JwtService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<RegisterDTO> register(@Valid @RequestBody RegisterDTO input) {
        var registeredUser = authService.signup(input);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginInputDTO input) {
        var user = authService.authenticate(input);
        var token = jwtService.generateToken(user);
        var response = LoginResponse.builder()
                .token(token)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return ResponseEntity.ok(response);
    }
}
