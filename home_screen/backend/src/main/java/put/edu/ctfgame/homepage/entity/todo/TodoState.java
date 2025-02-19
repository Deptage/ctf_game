package put.edu.ctfgame.homepage.entity.todo;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import put.edu.ctfgame.homepage.entity.CtfgameUser;


@Entity
@Table(name = "user_todo")
public class TodoState {

    @EmbeddedId
    private TodoStateKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private CtfgameUser user;

    @ManyToOne
    @MapsId("todoId")
    @JoinColumn(name = "todo_id")
    private Todo todo;

    private boolean isDone;
}
