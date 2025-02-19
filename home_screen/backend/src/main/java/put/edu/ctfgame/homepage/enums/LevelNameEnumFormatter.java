package put.edu.ctfgame.homepage.enums;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import put.edu.ctfgame.homepage.exception.InvalidLevelNameException;

import java.util.Locale;

@Component
public class LevelNameEnumFormatter implements Formatter<LevelName> {

    @Override
    public LevelName parse(String text, Locale locale) {
        try {
            return LevelName.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidLevelNameException("Invalid level name: " + text);
        }
    }

    @Override
    public String print(LevelName levelName, Locale locale) {
        return levelName.name();
    }
}
