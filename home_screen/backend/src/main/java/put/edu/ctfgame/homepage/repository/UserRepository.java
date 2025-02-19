package put.edu.ctfgame.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.homepage.entity.CtfgameUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CtfgameUser, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM CtfgameUser u")
    Boolean existsAny();

    Optional<CtfgameUser> findByUsername(String username);
}
