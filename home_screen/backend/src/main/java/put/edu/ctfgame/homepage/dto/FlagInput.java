package put.edu.ctfgame.homepage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.homepage.enums.LevelName;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FlagInput {
    private String flag;
}
