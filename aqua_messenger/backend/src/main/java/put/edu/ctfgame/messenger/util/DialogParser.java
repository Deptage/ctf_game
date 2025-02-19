package put.edu.ctfgame.messenger.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import put.edu.ctfgame.messenger.entity.Conversation;
import put.edu.ctfgame.messenger.entity.Message;
import put.edu.ctfgame.messenger.entity.User;
import put.edu.ctfgame.messenger.repository.ConversationRepository;
import put.edu.ctfgame.messenger.repository.MessageRepository;
import put.edu.ctfgame.messenger.repository.UserRepository;
import put.edu.ctfgame.messenger.service.ConversationService;

import java.io.InputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DialogParser {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ConversationService conversationService;

    @Transactional
    public void parseMessageSequenceFromJson(String jsonFileName, String sender, String receiver) throws IOException {
        var conversation = conversationService.findConversationByUsers(sender, receiver);
        ClassPathResource resource = new ClassPathResource(jsonFileName);
        ObjectMapper objectMapper = new ObjectMapper();
        List<MessageJson> messageJsonList;
        try (InputStream inputStream = resource.getInputStream()) {
            messageJsonList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<MessageJson>>() {}
            );
        }

        Map<Long, Message> messageMap = new HashMap<>();

        for (MessageJson jsonMessage : messageJsonList) {
            User messageSender = switch (jsonMessage.getSender()) {
                case "bot" -> userRepository.findByUsername(receiver).orElseThrow();
                case "user" -> userRepository.findByUsername("antiquascarlet1697").orElseThrow();
                default -> userRepository.findByUsername(jsonMessage.getSender()).orElseThrow();
            };

            Message message = Message.builder()
                    .content(jsonMessage.getTxt())
                    .from(messageSender)
                    .conversation(conversation)
                    .sent(jsonMessage.getSent() != null)
                    .sentAt(jsonMessage.getSent() != null ? LocalDateTime.now() : null)
                    .build();

            messageMap.put(jsonMessage.getId(), message);
        }

        for (MessageJson jsonMessage : messageJsonList) {
            Message currentMessage = messageMap.get(jsonMessage.getId());

            for (Long nextMessageId : jsonMessage.getNext()) {
                Message nextMessage = messageMap.get(nextMessageId);
                if (nextMessage != null) {
                    currentMessage.addNextMessage(nextMessage);
                }
            }
        }

        messageRepository.saveAll(messageMap.values());
    }

    @Data
    static class MessageJson {
        private Long id;
        private String sender;
        private String txt;
        private Boolean sent;
        private List<Long> next;
    }
}
