package put.edu.ctfgame.homepage.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import put.edu.ctfgame.homepage.TestDataFactory;
import put.edu.ctfgame.homepage.dto.HintDTO;
import put.edu.ctfgame.homepage.dto.MailDTO;
import put.edu.ctfgame.homepage.service.FlagService;
import put.edu.ctfgame.homepage.service.HintService;
import put.edu.ctfgame.homepage.service.JwtService;
import put.edu.ctfgame.homepage.service.MailService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MailController.class)
class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailService mailService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void testFindAllVisibleForCurrentUser() throws Exception {
        // Given
        List<MailDTO> mailList = List.of(TestDataFactory.sampleMailDTO());
        when(mailService.findAllVisibleForCurrentUser()).thenReturn(mailList);

        // When & Then
        mockMvc.perform(get("/mail")
                        .header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"topic\":\"topic\",\"content\":\"content\",\"read\":true}]"));
    }

    @Test
    @WithMockUser
    void testReadMail() throws Exception {
        // Given
        Long mailId = 1L;

        // When & Then
        mockMvc.perform(put("/mail/{mailId}/read", mailId)
                        .header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}