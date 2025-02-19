package put.edu.ctfgame.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.forum.entity.ForumUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ForumUser, Long> {
    Optional<ForumUser> findByUsername(String username);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.author.username = :username")
    Long countPostsByUsername(String username);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.author.username = :username")
    Long countCommentsByUsername(String username);
}
