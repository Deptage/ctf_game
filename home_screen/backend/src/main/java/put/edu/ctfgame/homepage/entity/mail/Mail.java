package put.edu.ctfgame.homepage.entity.mail;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.entity.Flag;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mail")
public class Mail {
    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private Boolean initRead;

    @Column(nullable = false)
    private Boolean initVisibility;

    @Column(nullable = false)
    private Boolean plotOnly;

    @Column(nullable = true)
    private Integer initOrdinal;

    @ManyToMany
    @JoinTable(
            name = "mail_preceding_flags",
            joinColumns = @JoinColumn(name = "mail_id"),
            inverseJoinColumns = @JoinColumn(name = "flag_id")
    )
    private List<Flag> precededBy;

    @OneToMany(mappedBy = "mail", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MailState> mailStates = new ArrayList<>();

    public void addState(MailState state) {
        mailStates.add(state);
    }

    @Override
    public String toString() {
        return "Mail{id=" + id + ", topic='" + topic + "', content='" + content + "', sender='" + sender + "'}";
    }
}
