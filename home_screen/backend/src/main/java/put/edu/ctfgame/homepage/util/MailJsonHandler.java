package put.edu.ctfgame.homepage.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.mail.Mail;
import put.edu.ctfgame.homepage.entity.mail.MailState;
import put.edu.ctfgame.homepage.entity.mail.MailStateKey;
import put.edu.ctfgame.homepage.repository.FlagRepository;
import put.edu.ctfgame.homepage.repository.MailRepository;
import put.edu.ctfgame.homepage.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MailJsonHandler {

    private final MailRepository mailRepository;
    private final UserService userService;
    private final FlagRepository flagRepository;

    @Transactional
    public void parseMailList(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        List<MailJson> jsonMails;
        try (InputStream inputStream = resource.getInputStream()) {
            jsonMails = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<MailJson>>() {}
            );
        }
        List<Flag> flags = flagRepository.findAll();
        List<Mail> mailsToSave = new ArrayList<>();
        jsonMails.forEach(jsonMail -> {
            Mail mail = Mail.builder()
                    .id(jsonMail.getId())
                    .topic(jsonMail.getTopic())
                    .content(jsonMail.getContent())
                    .sender(jsonMail.getSender())
                    .initRead(jsonMail.getRead())
                    .initOrdinal(jsonMail.getOrdinal())
                    .initVisibility(jsonMail.getInitVisibility())
                    .precededBy(new ArrayList<>())
                    .plotOnly(jsonMail.getPlotOnly())
                    .build();

            List<Flag> precedingFlags = flags.stream()
                    .filter(flag -> jsonMail.getFlagsPrecedingIds().contains(flag.getId().intValue()))
                    .collect(Collectors.toList());

            mail.setPrecededBy(precedingFlags);
            precedingFlags.forEach(flag -> flag.addPrecedingMail(mail));

            mailsToSave.add(mail);
        });
        mailRepository.saveAll(mailsToSave);
    }

    public void newUserInitMailStates(CtfgameUser user) {
        List<Mail> mails = mailRepository.findAll();
        var updatedMails = mails.stream().peek(mail -> {
            MailStateKey mailStateKey = MailStateKey.builder()
                    .userId(user.getId())
                    .mailId(mail.getId())
                    .build();
            MailState mailState = MailState.builder()
                    .id(mailStateKey)
                    .user(user)
                    .mail(mail)
                    .read(mail.getInitRead())
                    .visible(mail.getInitVisibility())
                    .ordinal(mail.getInitOrdinal())
                    .build();
            mail.addState(mailState);
        }).collect(Collectors.toSet());
        mailRepository.saveAll(updatedMails);
    }

    @Data
    static class MailJson {
        private Long id;
        private Boolean read;
        private String topic;
        private String content;
        private String sender;
        private Boolean initVisibility;
        private Integer ordinal;
        private List<Integer> flagsPrecedingIds = new ArrayList<>();
        private Boolean plotOnly = false;
    }
}
