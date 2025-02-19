package put.edu.ctfgame.messenger.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import put.edu.ctfgame.messenger.DTO.LoginResponseDTO;
import put.edu.ctfgame.messenger.DTO.UserDTO;
import put.edu.ctfgame.messenger.DTO.UsernameDTO;
import put.edu.ctfgame.messenger.service.AuthService;
import put.edu.ctfgame.messenger.service.JwtService;
import put.edu.ctfgame.messenger.service.UserService;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UsernameDTO> me() {
        return ResponseEntity.ok(UsernameDTO.from(userService.findAuthenticated()));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody UserDTO input) {
        try {
            var user = authService.authenticate(input);
            var token = jwtService.generateToken((UserDetails) user);
            var response = LoginResponseDTO.builder()
                    .token(token)
                    .expiresIn(jwtService.getExpirationTime())
                    .build();

            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.error(e.getMessage());
            JSONObject object = new JSONObject();
            object.put("response", "Authentication error");
            return ResponseEntity.ok(object);
        }
    }
}
