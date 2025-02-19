package put.edu.ctfgame.homepage.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import put.edu.ctfgame.homepage.entity.hint.Hint;
import put.edu.ctfgame.homepage.entity.score.Score;
import put.edu.ctfgame.homepage.enums.LevelName;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "level")
public class Level {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private LevelName name;

    @Column 
    private String description;
}
