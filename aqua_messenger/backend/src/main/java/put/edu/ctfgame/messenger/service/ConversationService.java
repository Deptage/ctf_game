package put.edu.ctfgame.messenger.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import put.edu.ctfgame.messenger.DTO.ConversationTypeInfo;
import put.edu.ctfgame.messenger.DTO.MessagePreviewDTO;
import put.edu.ctfgame.messenger.entity.Conversation;
import put.edu.ctfgame.messenger.enums.ConversationType;
import put.edu.ctfgame.messenger.exception.NoSuchConversationException;
import put.edu.ctfgame.messenger.repository.ConversationRepository;
import put.edu.ctfgame.messenger.entity.User;
import put.edu.ctfgame.messenger.repository.MessageRepository;

@Service
@AllArgsConstructor
@Slf4j
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;

    public ConversationTypeInfo getConversationInfo(Long conversationId) {
        Conversation conversation = findConversationById(conversationId);
        ConversationType type = conversation.getType();
        return ConversationTypeInfo.builder()
                .type(type)
                .build();
    }

    public Conversation findConversationById(Long id) {
        log.info("Finding conversation by ID: {}", id);
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Conversation not found with id: {}", id);
                    return new NoSuchConversationException("Conversation not found with id: " + id);
                });
        log.info("Found conversation: {}", conversation);
        return conversation;
    }

    public void addConversation(String user1Name, String user2Name) {
        log.info("Adding conversation between users: {} and {}", user1Name, user2Name);
        User user1 = userService.getUserByName(user1Name);
        User user2 = userService.getUserByName(user2Name);

        if (user1 == null || user2 == null) {
            log.error("One or both users not found: {} or {}", user1Name, user2Name);
            throw new IllegalArgumentException("One or both users not found");
        }

        var conversation = Conversation.builder()
                .user1(user1)
                .user2(user2)
                .build();
        conversationRepository.save(conversation);
        log.info("Conversation added successfully between users: {} and {}", user1Name, user2Name);
    }

    public Conversation findConversationByUsers(String user1Name, String user2Name) {
        log.info("Finding conversation between users: {} and {}", user1Name, user2Name);
        return conversationRepository.findConversationByUsernames(user1Name, user2Name).orElseThrow(
                () -> new NoSuchConversationException("No conversation found between users: " + user1Name + " and " + user2Name)
        );
    }

    public List<Conversation> findConversationsByUser(String username) {
        log.info("Finding conversations for user: {}", username);
        List<Conversation> conversations = conversationRepository.findConversationsByUsername(username);
        if (conversations.isEmpty()) {
            log.error("No conversations found for user: {}", username);
            throw new IllegalArgumentException("No conversations found for user: " + username);
        }
        log.info("Found conversations: {}", conversations);
        return conversations;
    }

    public Set<MessagePreviewDTO> getNextMessagesPreviews(Long conversationId) {
        var currentUser = userService.findAuthenticated();
        if (currentUser.getIsBot()) {
            return Set.of();
        }
        return messageRepository.findNextMessagesOfLastSentInConversation(findConversationById(conversationId)).stream()
                .map(MessagePreviewDTO::from)
                .collect(Collectors.toSet());
    }
}