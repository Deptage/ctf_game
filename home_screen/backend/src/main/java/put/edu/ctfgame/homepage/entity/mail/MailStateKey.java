package put.edu.ctfgame.homepage.entity.mail;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailStateKey implements Serializable {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long mailId;
}
