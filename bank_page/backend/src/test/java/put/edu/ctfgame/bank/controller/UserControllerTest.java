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
import put.edu.ctfgame.bank.service.JwtService;
import put.edu.ctfgame.bank.service.UserService;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void testAuthenticatedUser() throws Exception {
        var user = TestDataFactory.sampleBankUserDTO();
        Mockito.when(userService.findAuthenticated()).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/me").header("Instance-Id", "instance-id"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAllUsers() throws Exception {
        var users = List.of(TestDataFactory.sampleBankUserDTO());
        Mockito.when(userService.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Instance-Id", "instance-id"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists());
    }
}