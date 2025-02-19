package put.edu.ctfgame.homepage.entity.score;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScoreKey implements Serializable {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long flagId;
}
