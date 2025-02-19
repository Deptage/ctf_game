package put.edu.ctfgame.forum.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import put.edu.ctfgame.forum.service.AuthService;
import put.edu.ctfgame.forum.service.JwtService;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlagController.class)
class FlagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testGetFlag() throws Exception {
        mockMvc.perform(get("/flag").header("Instance-Id", "instance-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("role", "ROLE_ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value("TGHM{admin_sh0uld_g0_0n_a_c00kie_di3t}"));
    }
}
