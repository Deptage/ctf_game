package put.edu.ctfgame.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.entity.hint.Hint;
import put.edu.ctfgame.homepage.entity.hint.PlayerHint;
import put.edu.ctfgame.homepage.entity.hint.PlayerHintKey;
import put.edu.ctfgame.homepage.enums.LevelName;

import java.util.Optional;

public interface PlayerHintRepository extends JpaRepository<PlayerHint, PlayerHintKey> {

    @Query("SELECT COUNT(ph) FROM PlayerHint ph WHERE ph.user.username = ?1 AND ph.hint.flag.id = ?2")
    Long countAllByUsernameAndFlagId(String username, Long flagId);

    @Query(value = "SELECT f.total_points - COALESCE(SUM(CASE WHEN ph.hint_id IS NOT NULL THEN h.points_cost ELSE 0 END), 0) AS remaining_points " +
            "FROM flag f " +
            "         LEFT JOIN hint h ON f.id = h.flag_id " +
            "         LEFT JOIN player_hint ph ON h.id = ph.hint_id " +
            "         LEFT JOIN ctfgame_user u ON ph.user_id = u.id " +
            "WHERE f.id = ?2 and (u.username = ?1 or u.username is null) " +
            "GROUP BY f.id, f.total_points;", nativeQuery = true)
    Long calculateRemainingPoints(String username, Long flagId);


    Optional<PlayerHint> findByUserAndHint(CtfgameUser username, Hint hint);
}
