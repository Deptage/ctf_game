package put.edu.ctfgame.homepage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import put.edu.ctfgame.homepage.entity.mail.MailState;
import put.edu.ctfgame.homepage.entity.score.Score;
import put.edu.ctfgame.homepage.entity.todo.TodoState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@Table(name = "ctfgame_user")
public class CtfgameUser implements UserDetails {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String university;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant registrationTimestamp;

    @OneToMany(mappedBy = "user")
    private List<TodoState> todoStates;

    @OneToMany(mappedBy = "user")
    private List<MailState> mailStates;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Score> scores = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String toString() {
        return "CtfgameUser{id=" + id + ", username='" + username + "'}";
    }
}