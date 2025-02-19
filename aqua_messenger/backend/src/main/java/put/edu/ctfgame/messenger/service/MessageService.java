package put.edu.ctfgame.messenger.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import put.edu.ctfgame.messenger.DTO.MessageDTO;
import put.edu.ctfgame.messenger.DTO.MessagePreviewDTO;
import put.edu.ctfgame.messenger.DTO.MessageToAddDTO;
import put.edu.ctfgame.messenger.enums.ConversationType;
import put.edu.ctfgame.messenger.exception.ConversationScriptedException;
import put.edu.ctfgame.messenger.exception.MessageDoesNotBelongToConversationException;
import put.edu.ctfgame.messenger.exception.NoSuchMessageException;
import put.edu.ctfgame.messenger.exception.UserNotPartOfConversationException;
import put.edu.ctfgame.messenger.repository.MessageRepository;
import put.edu.ctfgame.messenger.entity.Conversation;
import put.edu.ctfgame.messenger.entity.Message;
import put.edu.ctfgame.messenger.entity.User;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final UserService userService;

    public List<MessageDTO> getSentMessages(Long conversationId) {
        return messageRepository.findAllByConversationIdAndSentTrueOrderBySentAtAsc(conversationId).stream().map(MessageDTO::from).toList();
    }

    @Transactional
    public MessageDTO addMessage(String senderName, String receiverName, String content) {
        log.info("Adding message from {} to {} with content: {}", senderName, receiverName, content);
        try {
            var conversation = conversationService.findConversationByUsers(senderName, receiverName);
            User sender = userService.getUserByName(senderName);
            User receiver = userService.getUserByName(receiverName);
            if (sender == null || receiver == null) {
                log.error("Sender or receiver not found: {} or {}", senderName, receiverName);
                throw new IllegalArgumentException("Sender or receiver not found");
            }
            return MessageDTO.from(messageRepository.save(
                    Message.builder()
                            .conversation(conversation)
                            .from(sender)
                            .content(content)
                            .sent(true)
                            .build())
            );

        } catch (Exception e) {
            log.error("Failed to add message from {} to {}: {}", senderName, receiverName, e.getMessage(), e);
            throw new RuntimeException("Failed to add message", e);
        }
    }

    @Transactional
    public MessageDTO sendNew(Long conversationId, MessageToAddDTO newMessage) {
        var sender = userService.findAuthenticated();
        var conversation = conversationService.findConversationById(conversationId);
        senderParticipationCheck(conversation, sender);

        if (conversation.getType().equals(ConversationType.SCRIPTED)) {
            throw new ConversationScriptedException("Cannot create new messages to scripted conversations");
        }
        var message = Message.builder()
                .conversation(conversation)
                .from(sender)
                .content(newMessage.getContent())
                .sentAt(LocalDateTime.now())
                .sent(true)
                .build();
        return MessageDTO.from(messageRepository.save(message));
    }

    @Transactional
    public List<MessageDTO> sendExisting(Long conversationId, Long messageId) {
        var conversation = conversationService.findConversationById(conversationId);
        var message = findById(messageId);
        var nextMessages = conversationService.getNextMessagesPreviews(conversationId);
        var currentUser = userService.findAuthenticated();
        if (!currentUser.getUsername().equals(message.getFrom().getUsername())) {
            throw new UserNotPartOfConversationException("User is not the sender of the message");
        }
        if (nextMessages.stream().noneMatch(m -> m.getId().equals(messageId))) {
            throw new NoSuchMessageException("Not a viable next message at this stage");
        }
        if (!conversation.getId().equals(message.getConversation().getId())) {
            throw new MessageDoesNotBelongToConversationException("Message does not belong to conversation");
        }
        message.setSent(true);
        message.setSentAt(LocalDateTime.now().minus(1, ChronoUnit.MICROS));
        var addedMessages = new ArrayList<Message>();

        Optional<Message> botMessage = message.getNextMessages().stream()
                .findFirst();
        botMessage.ifPresentOrElse(m -> {
            m.setSent(true);
            m.setSentAt(LocalDateTime.now());
            addedMessages.add(messageRepository.save(m));
            if (m.getNextMessages().isEmpty()) {
                conversation.setType(ConversationType.FREE);
            }
        }, () -> conversation.setType(ConversationType.FREE));

        var savedMessage = messageRepository.save(message);
        addedMessages.add(savedMessage);
        addedMessages.sort(Comparator.comparing(Message::getSentAt));

        log.info("Message sent successfully with id: {}", messageId);
        return addedMessages.stream().map(MessageDTO::from).collect(Collectors.toList());
    }

    public List<Message> getMessages(String user1, String user2) {
        log.info("Getting messages between {} and {}", user1, user2);
        try {
            Conversation conversation = conversationService.findConversationByUsers(user1, user2);
            List<Message> messages = messageRepository.findByConversation(conversation);
            log.info("Retrieved {} messages between {} and {}", messages.size(), user1, user2);
            return messages;
        } catch (Exception e) {
            log.error("Failed to get messages between {} and {}: {}", user1, user2, e.getMessage(), e);
            throw new RuntimeException("Failed to get messages", e);
        }
    }

    private Message findById(Long id) {
        return messageRepository.findById(id).orElseThrow(
                () -> new NoSuchMessageException("Message not found"));
    }

    private void senderParticipationCheck(Conversation conversation, User sender) {
        if (!conversation.getUser1().equals(sender) && !conversation.getUser2().equals(sender)) {
            log.error("User {} is not part of conversation {}", sender.getUsername(), conversation.getId());
            throw new UserNotPartOfConversationException(" "+ sender.getUsername() +" is not part of conversation");
        }
    }
}