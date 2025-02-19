package put.edu.ctfgame.homepage.entity.hint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.homepage.entity.CtfgameUser;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlayerHint {

    @EmbeddedId
    private PlayerHintKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private CtfgameUser user;

    @ManyToOne
    @MapsId("hintId")
    @JoinColumn(name = "hint_id", nullable = false)
    private Hint hint;
}
