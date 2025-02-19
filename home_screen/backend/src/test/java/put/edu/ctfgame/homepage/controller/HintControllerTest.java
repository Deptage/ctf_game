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
import put.edu.ctfgame.homepage.enums.LevelName;
import put.edu.ctfgame.homepage.service.FlagService;
import put.edu.ctfgame.homepage.service.HintService;
import put.edu.ctfgame.homepage.service.JwtService;
import put.edu.ctfgame.homepage.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HintController.class)
class HintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HintService hintService;

    @MockBean
    private FlagService flagService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void testFindAll() throws Exception {
        // Given
        List<HintDTO> hintDTOs = List.of(TestDataFactory.sampleHintDTO());
        when(hintService.findAll()).thenReturn(hintDTOs);

        // When & Then
        mockMvc.perform(get("/hints")
                        .header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"title\":\"title\",\"content\":\"hint\",\"flagId\":1,\"ordinal\":1,\"revealed\":true}]"));
    }

    @Test
    @WithMockUser
    void testFindAllByFlagId() throws Exception {
        // Given
        Long flagId = 1L;
        List<HintDTO> hintDTOs = List.of(TestDataFactory.sampleHintDTO());
        when(hintService.findAllByFlagId(flagId)).thenReturn(hintDTOs);

        // When & Then
        mockMvc.perform(get("/hints/flag/{flagId}", flagId)
                        .header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"title\":\"title\",\"content\":\"hint\",\"flagId\":1,\"ordinal\":1,\"revealed\":true}]"));
    }

    @Test
    @WithMockUser
    void testRevealHint() throws Exception {
        // Given
        Long flagId = 1L;
        var hintDTO = TestDataFactory.sampleHintDTO();

        // When
        when(hintService.revealHint(flagId)).thenReturn(hintDTO);

        // Then
        mockMvc.perform(post("/hints/flag/{flagId}/reveal", flagId)
                        .header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}