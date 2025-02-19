package put.edu.ctfgame.messenger.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import put.edu.ctfgame.messenger.DTO.*;
import put.edu.ctfgame.messenger.TestDataFactory;
import put.edu.ctfgame.messenger.enums.ConversationType;
import put.edu.ctfgame.messenger.service.*;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ConversationController.class)
class MessageControllerTest {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private ConversationService conversationService;

    @Test
    @WithMockUser(username = "testuser")
    void getConversationsList() throws Exception {
        var conversationDTO = TestDataFactory.sampleConversation();
        var conversations = List.of(conversationDTO);

        Mockito.when(userService.findAuthenticated()).thenReturn(TestDataFactory.sampleUser());
        Mockito.when(conversationService.findConversationsByUser(anyString())).thenReturn(conversations);

        mockMvc.perform(get("/conversation")
                        .header("Instance-Id", "instance-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getConversationInfo() throws Exception {
        var conversationTypeInfo = new ConversationTypeInfo(ConversationType.SCRIPTED);

        Mockito.when(conversationService.getConversationInfo(anyLong())).thenReturn(conversationTypeInfo);

        mockMvc.perform(get("/conversation/1/info")
                        .header("Instance-Id", "instance-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").exists());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getSentMessages() throws Exception {
        var messageDTO = new MessageDTO();
        var messages = List.of(messageDTO);

        Mockito.when(messageService.getSentMessages(anyLong())).thenReturn(messages);

        mockMvc.perform(get("/conversation/1/sentMessages")
                        .header("Instance-Id", "instance-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getNextMessages() throws Exception {
        var messagePreviewDTO = new MessagePreviewDTO(1L, "content");
        var messages = Set.of(messagePreviewDTO);

        Mockito.when(conversationService.getNextMessagesPreviews(anyLong())).thenReturn(messages);

        mockMvc.perform(get("/conversation/1/nextMessages")
                        .header("Instance-Id", "instance-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "testuser")
    void sendNew() throws Exception {
        var messageDTO = TestDataFactory.sampleMessageDTO();

        Mockito.when(messageService.sendNew(anyLong(), Mockito.any(MessageToAddDTO.class))).thenReturn(messageDTO);

        mockMvc.perform(post("/conversation/1/sendNew")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Hello\"}")
                        .header("Instance-Id", "instance-id")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "testuser")
    void sendExisting() throws Exception {
        var messageDTO = new MessageDTO();
        var messages = List.of(messageDTO);

        Mockito.when(messageService.sendExisting(anyLong(), anyLong())).thenReturn(messages);

        mockMvc.perform(post("/conversation/1/sendExisting/1")
                        .header("Instance-Id", "instance-id")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}