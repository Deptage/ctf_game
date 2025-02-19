package put.edu.ctfgame.homepage.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import put.edu.ctfgame.homepage.TestDataFactory;
import put.edu.ctfgame.homepage.dto.LoginInputDTO;
import put.edu.ctfgame.homepage.dto.RegisterDTO;
import put.edu.ctfgame.homepage.repository.LevelRepository;
import put.edu.ctfgame.homepage.service.AuthService;
import put.edu.ctfgame.homepage.service.JwtService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private LevelRepository levelRepository;

    @Test
    @WithMockUser
    void testRegister() throws Exception {
        var userDTO = RegisterDTO.builder().username("testuser").password("password").build();
        when(levelRepository.findAll()).thenReturn(List.of(TestDataFactory.sampleLevel()));
        when(authService.signup(any(RegisterDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/auth/signup")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"username\":\"testuser@test.test\",\"password\":\"zaq1@WSX\",\"university\":\"testuniversity\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser
    void testAuthenticate() throws Exception {
        var user = TestDataFactory.sampleCtfgameUser();
        when(authService.authenticate(any(LoginInputDTO.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("dummyToken");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummyToken"))
                .andExpect(jsonPath("$.expiresIn").value(3600L));
    }
}
