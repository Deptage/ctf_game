package put.edu.ctfgame.messenger.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.messenger.entity.Message;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private String from;
    private String content;
    private LocalDateTime sentAt;

    public static MessageDTO from(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .from(message.getFrom().getUsername())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .build();
    }
      
}