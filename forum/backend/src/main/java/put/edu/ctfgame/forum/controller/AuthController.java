package put.edu.ctfgame.forum.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import put.edu.ctfgame.forum.dto.ForumUserDTO;
import put.edu.ctfgame.forum.dto.LoginResponse;
import put.edu.ctfgame.forum.dto.PostAuthorDTO;
import put.edu.ctfgame.forum.service.AuthService;
import put.edu.ctfgame.forum.service.JwtService;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<PostAuthorDTO> signup(@Valid @RequestBody ForumUserDTO forumUserDTO) {
        return ResponseEntity.ok(authService.signup(forumUserDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody ForumUserDTO forumUserDTO, HttpServletResponse httpRes) {
        var user = authService.authenticate(forumUserDTO);
        var token = jwtService.generateToken(user);
        var response = LoginResponse.builder()
                .token(token)
                .expiresIn(jwtService.getExpirationTime())
                .build();
        httpRes.addCookie(authService.userRoleCookie());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpRes) {
        httpRes.addCookie(authService.emptyRoleCookie());
        return ResponseEntity.ok().build();
    }
}
