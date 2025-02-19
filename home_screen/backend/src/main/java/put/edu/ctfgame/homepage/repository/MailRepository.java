package put.edu.ctfgame.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.mail.Mail;

import java.util.List;
import java.util.Optional;


@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {

    @Query("SELECT m FROM Mail m JOIN m.mailStates ms WHERE ms.user = ?1 AND ms.visible = true order by ms.ordinal")
    List<Mail> findAllVisibleForUser(CtfgameUser user);

    @Query("SELECT m FROM Mail m JOIN m.mailStates ms WHERE ms.user = ?1 AND ms.visible = true AND m.id = ?2")
    Optional<Mail> findVisibleByIdForUser(CtfgameUser user, Long mailId);

    @Query("SELECT m FROM Mail m JOIN m.mailStates ms WHERE ms.user = ?1 AND ms.visible = false AND m.plotOnly = true ")
    List<Mail> findAllPlotOnlyNotVisible(CtfgameUser user);

    List<Mail> findAllByPrecededByContaining(Flag flag);

    @Query("SELECT max(ms.ordinal) FROM Mail m JOIN m.mailStates ms WHERE ms.user = ?1")
    Integer findMaxOrdinal(CtfgameUser user);
}
