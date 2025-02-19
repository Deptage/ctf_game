package put.edu.ctfgame.homepage;

import put.edu.ctfgame.homepage.dto.*;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.Level;
import put.edu.ctfgame.homepage.entity.hint.Hint;
import put.edu.ctfgame.homepage.entity.hint.PlayerHint;
import put.edu.ctfgame.homepage.entity.hint.PlayerHintKey;
import put.edu.ctfgame.homepage.entity.mail.Mail;
import put.edu.ctfgame.homepage.entity.mail.MailState;
import put.edu.ctfgame.homepage.entity.mail.MailStateKey;
import put.edu.ctfgame.homepage.entity.score.Score;
import put.edu.ctfgame.homepage.entity.score.ScoreKey;
import put.edu.ctfgame.homepage.enums.LevelName;

import java.util.List;

public class TestDataFactory {

    public static CtfgameUser sampleCtfgameUser() {
        return CtfgameUser.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .university("University")
                .build();
    }

    public static RegisterDTO sampleCtfgameUserDTO() {
        return RegisterDTO.from(sampleCtfgameUser());
    }

    public static Level sampleLevel() {
        return Level.builder()
                .id(1L)
                .name(LevelName.BANK)
                .build();
    }

    public static Hint sampleHint() {
        return Hint.builder()
                .id(1L)
                .ordinal(1)
                .title("title")
                .flag(sampleFlag())
                .content("hint")
                .build();
    }

    public static HintDTO sampleHintDTO() {
        return HintDTO.from(sampleHint(), true);
    }

    public static PlayerHint samplePlayerHint() {
        return PlayerHint.builder()
                .id(PlayerHintKey.builder()
                        .userId(1L)
                        .hintId(1L)
                        .build())
                .hint(sampleHint())
                .user(sampleCtfgameUser())
                .build();
    }

    public static FlagSolvedInfo sampleFlagSolvedInfo() {
        return new FlagSolvedInfo() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public Boolean getSolved() {
                return true;
            }
        };
    }

    public static FlagResponse sampleFlagResponse() {
        return FlagResponse.builder()
                .message("xd")
                .correct(true)
                .build();
    }

    public static Flag sampleFlag() {
        return Flag.builder()
                .id(1L)
                .flag("flag")
                .totalPoints(100L)
                .build();
    }

    public static Mail sampleMail() {
        return Mail.builder()
                .id(1L)
                .topic("topic")
                .content("content")
                .sender("sender")
                .initRead(true)
                .initVisibility(true)
                .precededBy(List.of(sampleFlag()))
                .plotOnly(false)
                .initOrdinal(1)
                .build();
    }

    public static MailDTO sampleMailDTO() {
        return MailDTO.from(sampleMail(), sampleMailState());
    }

    public static MailState sampleMailState() {
        CtfgameUser user = sampleCtfgameUser();
        Mail mail = sampleMail();
        MailState mailState = MailState.builder()
                .id(new MailStateKey(user.getId(), mail.getId()))
                .user(user)
                .mail(mail)
                .read(true)
                .visible(true)
                .build();
        mail.setMailStates(List.of(mailState));
        return mailState;
    }

    public static Score sampleScore() {
        var scoreKey = ScoreKey.builder()
                .userId(1L)
                .flagId(1L)
                .build();
        return Score.builder()
                .id(scoreKey)
                .user(sampleCtfgameUser())
                .flag(sampleFlag())
                .points(100L)
                .build();
    }
}
