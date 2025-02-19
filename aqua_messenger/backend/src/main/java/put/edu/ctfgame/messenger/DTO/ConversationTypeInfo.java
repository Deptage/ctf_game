package put.edu.ctfgame.messenger.DTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import put.edu.ctfgame.messenger.enums.ConversationType;

@Data
@AllArgsConstructor
@Builder
public class ConversationTypeInfo {

    @Enumerated(EnumType.STRING)
    private ConversationType type;
}
