package put.edu.ctfgame.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.score.Score;
import put.edu.ctfgame.homepage.entity.score.ScoreKey;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, ScoreKey> {
    Boolean existsByFlagAndUser(Flag flag, CtfgameUser user);
    List<Score> findAllByUser(CtfgameUser user);
}
