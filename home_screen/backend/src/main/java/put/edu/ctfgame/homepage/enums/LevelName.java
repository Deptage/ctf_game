package put.edu.ctfgame.homepage.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum LevelName {
    MESSENGER, FORUM, BANK, COMPANY;
}