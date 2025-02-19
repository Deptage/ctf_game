package put.edu.ctfgame.messenger.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import put.edu.ctfgame.messenger.entity.Message;

@Data
@AllArgsConstructor
@Builder
public class MessagePreviewDTO {
    private Long id;
    private String content;

    public static MessagePreviewDTO from(Message message) {
        return MessagePreviewDTO.builder()
                .id(message.getId())
                .content(message.getContent())
                .build();
    }
}
