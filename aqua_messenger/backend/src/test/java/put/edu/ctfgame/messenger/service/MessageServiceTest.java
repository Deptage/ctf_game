package put.edu.ctfgame.messenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import put.edu.ctfgame.messenger.DTO.MessageToAddDTO;
import put.edu.ctfgame.messenger.TestDataFactory;
import put.edu.ctfgame.messenger.entity.Conversation;
import put.edu.ctfgame.messenger.entity.Message;
import put.edu.ctfgame.messenger.entity.User;
import put.edu.ctfgame.messenger.enums.ConversationType;
import put.edu.ctfgame.messenger.exception.ConversationScriptedException;
import put.edu.ctfgame.messenger.exception.MessageDoesNotBelongToConversationException;
import put.edu.ctfgame.messenger.repository.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ConversationService conversationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessageService messageService;

    @Test
    void testGetSentMessages() {
        // Given
        var conversationId = 1L;
        var messages = List.of(TestDataFactory.sampleMessage());

        // When
        when(messageRepository.findAllByConversationIdAndSentTrueOrderBySentAtAsc(conversationId)).thenReturn(messages);

        // Then
        var result = messageService.getSentMessages(conversationId);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(messages.size(), result.size());
    }

    @Test
    void testAddMessage() {
        // Given
        var senderName = "user1";
        var receiverName = "user2";
        var content = "Hello!";
        var conversation = TestDataFactory.sampleConversation();
        var sender = TestDataFactory.sampleUser();
        var receiver = TestDataFactory.sampleUser();
        var message = TestDataFactory.sampleMessage();

        // When
        when(conversationService.findConversationByUsers(senderName, receiverName)).thenReturn(conversation);
        when(userService.getUserByName(senderName)).thenReturn(sender);
        when(userService.getUserByName(receiverName)).thenReturn(receiver);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Then
        var result = messageService.addMessage(senderName, receiverName, content);
        assertNotNull(result);
        assertEquals(message.getContent(), result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testSendNew() {
        // Given
        var conversationId = 1L;
        var newMessage = new MessageToAddDTO("Hello!");
        var sender = TestDataFactory.sampleUser();
        var conversation = TestDataFactory.sampleConversation();
        conversation.setType(ConversationType.FREE);
        var message = TestDataFactory.sampleMessage();

        // When
        when(userService.findAuthenticated()).thenReturn(sender);
        when(conversationService.findConversationById(conversationId)).thenReturn(conversation);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Then
        var result = messageService.sendNew(conversationId, newMessage);
        assertNotNull(result);
        assertEquals(message.getContent(), result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testSendExisting() {
        // Given
        var conversationId = 1L;
        var messageId = 1L;
        var conversation = TestDataFactory.sampleConversation();
        var message = TestDataFactory.sampleMessage();
        var nextMessages = Set.of(TestDataFactory.sampleMessagePreviewDTO());

        // When
        when(conversationService.findConversationById(conversationId)).thenReturn(conversation);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(conversationService.getNextMessagesPreviews(conversationId)).thenReturn(nextMessages);
        when(userService.findAuthenticated()).thenReturn(TestDataFactory.sampleUser());
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Then
        var result = messageService.sendExisting(conversationId, messageId);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testGetMessages() {
        // Given
        var user1 = "user1";
        var user2 = "user2";
        var conversation = TestDataFactory.sampleConversation();
        var messages = List.of(TestDataFactory.sampleMessage());

        // When
        when(conversationService.findConversationByUsers(user1, user2)).thenReturn(conversation);
        when(messageRepository.findByConversation(conversation)).thenReturn(messages);

        // Then
        var result = messageService.getMessages(user1, user2);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(messages.size(), result.size());
    }

    @Test
    void testSendNew_ScriptedConversation() {
        // Given
        var conversationId = 1L;
        var newMessage = new MessageToAddDTO("Hello!");
        var sender = TestDataFactory.sampleUser();
        var conversation = TestDataFactory.sampleConversation();
        conversation.setType(ConversationType.SCRIPTED);

        // When
        when(userService.findAuthenticated()).thenReturn(sender);
        when(conversationService.findConversationById(conversationId)).thenReturn(conversation);

        // Then
        assertThrows(ConversationScriptedException.class, () -> messageService.sendNew(conversationId, newMessage));
    }
}