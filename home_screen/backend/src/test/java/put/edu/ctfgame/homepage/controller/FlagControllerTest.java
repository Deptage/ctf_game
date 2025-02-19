package put.edu.ctfgame.homepage.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import put.edu.ctfgame.homepage.TestDataFactory;
import put.edu.ctfgame.homepage.dto.FlagInput;
import put.edu.ctfgame.homepage.dto.FlagResponse;
import put.edu.ctfgame.homepage.dto.FlagSolvedInfo;
import put.edu.ctfgame.homepage.service.FlagService;
import put.edu.ctfgame.homepage.service.JwtService;
import put.edu.ctfgame.homepage.service.ScoreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlagController.class)
class FlagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlagService flagService;

    @MockBean
    private ScoreService scoreService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void testGetSolvedInfo() throws Exception {
        // Given
        List<FlagSolvedInfo> solvedInfoList = List.of(TestDataFactory.sampleFlagSolvedInfo());
        Mockito.when(flagService.getFlagsSolvedInfo()).thenReturn(solvedInfoList);

        // When & Then
        mockMvc.perform(get("/flag/solvedInfo")
                        .header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void testSubmitFlag() throws Exception {
        // Given
        Long flagId = 1L;
        FlagInput input = new FlagInput("flag");
        FlagResponse response = TestDataFactory.sampleFlagResponse();
        Mockito.when(scoreService.submitFlag(anyLong(), any(FlagInput.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/flag/submit/{flagId}", flagId)
                        .header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"flag\":\"flag\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true));
    }
}