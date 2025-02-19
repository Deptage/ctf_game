package put.edu.ctfgame.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.homepage.entity.hint.Hint;

import java.util.List;
import java.util.Optional;

@Repository
public interface HintRepository extends JpaRepository<Hint, Long> {
    List<Hint> findAllByFlagId(Long flagId);

    @Query("SELECT h FROM Hint h WHERE h.ordinal = coalesce((select max(h2.ordinal) from Hint h2 inner join PlayerHint ph on h2.id = ph.hint.id where h2.flag.id = h.flag.id and ph.user.id = ?2 and h2.flag.id = ?1), 0)+1 and h.flag.id = ?1")
    Optional<Hint> findNextByFlagId(Long flagId, Long userId);
}
