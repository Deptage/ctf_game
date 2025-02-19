package put.edu.ctfgame.homepage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import put.edu.ctfgame.homepage.dto.RegisterDTO;
import put.edu.ctfgame.homepage.service.UserService;

@RequestMapping("/users")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RegisterDTO> authenticatedUser() {
        var user = userService.findAuthenticated();
        return ResponseEntity.ok(RegisterDTO.from(user));
    }
}