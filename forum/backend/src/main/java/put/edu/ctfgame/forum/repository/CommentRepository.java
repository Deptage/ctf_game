package put.edu.ctfgame.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.forum.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
