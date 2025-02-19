package put.edu.ctfgame.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.homepage.entity.todo.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
}
