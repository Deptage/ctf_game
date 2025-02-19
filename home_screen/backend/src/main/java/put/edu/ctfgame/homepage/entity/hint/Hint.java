package put.edu.ctfgame.homepage.entity.hint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.homepage.entity.Flag;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Hint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer ordinal;

    @Column(nullable = false)
    private Integer pointsCost;

    @ManyToOne
    @JoinColumn(name = "flag_id", nullable = false)
    private Flag flag;
}
