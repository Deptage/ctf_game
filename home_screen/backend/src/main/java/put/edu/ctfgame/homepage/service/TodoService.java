package put.edu.ctfgame.homepage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.homepage.repository.TodoRepository;

@Service
@AllArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
}
