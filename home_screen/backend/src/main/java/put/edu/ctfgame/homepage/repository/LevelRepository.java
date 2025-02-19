package put.edu.ctfgame.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.homepage.entity.Level;
import put.edu.ctfgame.homepage.enums.LevelName;

import java.util.Optional;


@Repository
public interface LevelRepository extends JpaRepository<Level, Long>{
    Optional<Level> findByName(LevelName name);
} 