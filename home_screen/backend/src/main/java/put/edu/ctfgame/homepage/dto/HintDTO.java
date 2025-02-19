package put.edu.ctfgame.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.homepage.entity.hint.Hint;
import put.edu.ctfgame.homepage.enums.LevelName;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HintDTO {
    private String title;
    private String content;
    private Long flagId;
    private Integer ordinal;
    private Boolean revealed;
    private Integer pointsCost;

    public static HintDTO from(Hint hint, Boolean revealed) {
        return HintDTO.builder()
                .title(revealed ? hint.getTitle() : null)
                .flagId(hint.getFlag().getId())
                .ordinal(hint.getOrdinal())
                .pointsCost(hint.getPointsCost())
                .content(revealed ? hint.getContent() : null)
                .revealed(revealed)
                .build();
    }
}
