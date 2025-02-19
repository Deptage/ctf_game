package put.edu.ctfgame.homepage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.homepage.entity.hint.Hint;
import put.edu.ctfgame.homepage.entity.mail.Mail;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Flag {
    @Id
    private Long id;

    @Column(nullable = false)
    private String flag;

    @OneToMany(mappedBy = "flag", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Hint> hints = new ArrayList<>();

    @ManyToMany(mappedBy = "precededBy")
    @Builder.Default
    private List<Mail> precedesMails = new ArrayList<>();

    @Column(nullable = false)
    private Long totalPoints;

    public void addPrecedingMail(Mail mail) {
        if (precedesMails == null) {
            precedesMails = new ArrayList<>();
        }
        precedesMails.add(mail);
    }
}
