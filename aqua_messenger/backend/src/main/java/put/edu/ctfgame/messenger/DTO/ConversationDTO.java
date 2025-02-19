package put.edu.ctfgame.messenger.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import put.edu.ctfgame.messenger.entity.Conversation;
import put.edu.ctfgame.messenger.entity.User;

@Data
@AllArgsConstructor
@Builder
public class ConversationDTO {
    private Long id;
    private String contactName;

    public static ConversationDTO from(Conversation conversation, String currentUser) {
        return ConversationDTO.builder()
                .id(conversation.getId())
                .contactName(conversation.getUser2().getUsername().equals(currentUser) ?
                        conversation.getUser1().getUsername() : conversation.getUser2().getUsername())
                .build();
    }
}
