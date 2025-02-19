package put.edu.ctfgame.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.homepage.dto.FlagSolvedInfo;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.mail.Mail;

import java.util.List;

@Repository
public interface FlagRepository extends JpaRepository<Flag, Long> {

    @Query("SELECT f.id as id, s.id is not null as solved " +
            "FROM Flag f left join Score s on f.id = s.flag.id and s.user.id = ?1")
    List<FlagSolvedInfo> getFlagsSolvedInfo(Long userId);
}
