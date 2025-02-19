package put.edu.ctfgame.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import put.edu.ctfgame.forum.dto.FlagDTO;
import put.edu.ctfgame.forum.service.AuthService;

@RestController
@RequestMapping("/flag")
@AllArgsConstructor
public class FlagController {

    private final AuthService authService;

    @GetMapping
    public FlagDTO getFlag(@CookieValue("role") String role) {
        authService.verifyAdminAccessByCookieRole(role);
        return FlagDTO.builder().flag("TGHM{admin_sh0uld_g0_0n_a_c00kie_di3t}").build();
    }
}
