package put.edu.ctfgame.homepage.entity.score;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.enums.LevelStateEnum;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Score {

    @EmbeddedId
    private ScoreKey id;

    @Column(nullable = false)
    @Builder.Default
    private Long points = 0L;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant submissionTimestamp;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private CtfgameUser user;

    @ManyToOne
    @MapsId("flagId")
    @JoinColumn(name = "flag_id")
    private Flag flag;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LevelStateEnum state = LevelStateEnum.UNLOCKED;
}
