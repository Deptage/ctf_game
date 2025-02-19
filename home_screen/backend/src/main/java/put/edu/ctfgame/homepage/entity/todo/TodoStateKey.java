package put.edu.ctfgame.homepage.entity.todo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable

public class TodoStateKey implements Serializable {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long todoId;
    
    
}
