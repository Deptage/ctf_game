package put.edu.ctfgame.homepage.entity.hint;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlayerHintKey implements Serializable {
    private Long userId;
    private Long hintId;
}
