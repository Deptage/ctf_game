package put.edu.ctfgame.messenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import put.edu.ctfgame.messenger.TestDataFactory;
import put.edu.ctfgame.messenger.entity.Conversation;
import put.edu.ctfgame.messenger.repository.ConversationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ConversationService conversationService;

    @Test
    void testFindConversationById() {
        // Given
        var conversation = TestDataFactory.sampleConversation();

        // When
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(conversation));

        // Then
        var result = conversationService.findConversationById(1L);
        assertNotNull(result);
        assertEquals(conversation, result);
    }

    @Test
    void testAddConversation() {
        // Given
        var user1 = TestDataFactory.sampleUser();
        var user2 = TestDataFactory.sampleUser();

        // When
        when(userService.getUserByName("user1")).thenReturn(user1);
        when(userService.getUserByName("user2")).thenReturn(user2);

        // Then
        conversationService.addConversation("user1", "user2");
        verify(conversationRepository, times(1)).save(any(Conversation.class));
    }

    @Test
    void testFindConversationsByUsers() {
        // Given
        var conversation = TestDataFactory.sampleConversation();

        // When
        when(conversationRepository.findConversationByUsernames("user1", "user2")).thenReturn(Optional.of(conversation));

        // Then
        var result = conversationService.findConversationByUsers("user1", "user2");
        assertNotNull(result);
        assertEquals(conversation, result);
    }

    @Test
    void testFindConversationsByUser() {
        // Given
        var conversations = List.of(TestDataFactory.sampleConversation());

        // When
        when(conversationRepository.findConversationsByUsername("user1")).thenReturn(conversations);

        // Then
        var result = conversationService.findConversationsByUser("user1");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}