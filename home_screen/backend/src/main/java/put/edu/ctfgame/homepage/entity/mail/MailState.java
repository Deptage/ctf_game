package put.edu.ctfgame.homepage.entity.mail;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.homepage.entity.CtfgameUser;

@Entity
@Table(name = "user_mail")
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class MailState {

    @EmbeddedId
    private MailStateKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private CtfgameUser user;

    @ManyToOne
    @MapsId("mailId")
    @JoinColumn(name = "mail_id")
    private Mail mail;

    @Column(nullable = false)
    private Boolean read;

    @Column(nullable = false)
    private Boolean visible;

    @Column
    private Integer ordinal;

    @Override
    public String toString() {
        return "MailState{id=" + id + ", read=" + read + ", visible=" + visible + ", ordinal=" + ordinal + "}";
    }
}
