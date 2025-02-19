package put.edu.ctfgame.bank.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import put.edu.ctfgame.bank.TestDataFactory;
import put.edu.ctfgame.bank.dto.BankUserDTO;
import put.edu.ctfgame.bank.dto.LoginResponse;
import put.edu.ctfgame.bank.service.AuthService;
import put.edu.ctfgame.bank.service.JwtService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void testRegister() throws Exception {
        var userDTO = BankUserDTO.builder().username("testuser").password("password").build();
        when(authService.signup(any(BankUserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/auth/signup").header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser
    void testAuthenticate() throws Exception {
        var user = TestDataFactory.sampleBankUser();
        when(authService.authenticate(any(BankUserDTO.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("dummyToken");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(post("/auth/login").header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummyToken"))
                .andExpect(jsonPath("$.expiresIn").value(3600L));
    }
}