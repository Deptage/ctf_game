package put.edu.ctfgame.messenger;

import put.edu.ctfgame.messenger.DTO.ConversationDTO;
import put.edu.ctfgame.messenger.DTO.MessageDTO;
import put.edu.ctfgame.messenger.DTO.MessagePreviewDTO;
import put.edu.ctfgame.messenger.DTO.UserDTO;
import put.edu.ctfgame.messenger.entity.Conversation;
import put.edu.ctfgame.messenger.entity.Message;
import put.edu.ctfgame.messenger.entity.User;
import put.edu.ctfgame.messenger.enums.ConversationType;

import java.time.LocalDateTime;

public class TestDataFactory {
    public static User sampleUser() {
        return User.builder()
                .username("user1")
                .build();
    }

    public static UserDTO sampleUserDTO() {
        return UserDTO.from(sampleUser());
    }

    public static Conversation sampleConversation() {
        return Conversation.builder()
                .id(1L)
                .user1(sampleUser())
                .user2(sampleUser())
                .build();
    }

    public static ConversationDTO sampleConversationDTO() {
        return ConversationDTO.from(sampleConversation(), "user");
    }

    public static Message sampleMessage() {
        return Message.builder()
                .id(1L)
                .conversation(sampleConversation())
                .from(sampleUser())
                .sentAt(LocalDateTime.parse("2021-01-01T00:00:00"))
                .content("Hello!")
                .build();
    }

    public static MessageDTO sampleMessageDTO() {
        return MessageDTO.from(sampleMessage());
    }

    public static MessagePreviewDTO sampleMessagePreviewDTO() {
        return new MessagePreviewDTO(1L, "content");
    }
}