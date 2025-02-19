package put.edu.ctfgame.homepage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import put.edu.ctfgame.homepage.dto.MailDTO;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.mail.Mail;
import put.edu.ctfgame.homepage.entity.mail.MailState;
import put.edu.ctfgame.homepage.entity.score.Score;
import put.edu.ctfgame.homepage.exception.NoSuchFlagException;
import put.edu.ctfgame.homepage.exception.NoSuchMailException;
import put.edu.ctfgame.homepage.repository.FlagRepository;
import put.edu.ctfgame.homepage.repository.MailRepository;
import put.edu.ctfgame.homepage.repository.ScoreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MailService {
    private final MailRepository mailRepository;
    private final UserService userService;
    private final FlagRepository flagRepository;
    private final ScoreRepository scoreRepository;

    public List<MailDTO> findAllVisibleForCurrentUser() {
        var currentUser = userService.findAuthenticated();
        var mails = mailRepository.findAllVisibleForUser(currentUser);
        return mails.stream()
                .map(mail -> {
                    var mailState = mail.getMailStates().stream()
                            .filter(mailState1 -> mailState1.getUser().getId().equals(currentUser.getId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("MailState for user " + currentUser.getId() + " not found"));
                    return MailDTO.from(mail, mailState);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void readMailById(Long mailId) {
        var currentUser = userService.findAuthenticated();
        var mail = mailRepository.findVisibleByIdForUser(currentUser, mailId).orElseThrow(
                () -> new NoSuchMailException("Mail with id " + mailId + " not found or could not be altered")
        );
        var mailState = mail.getMailStates().stream()
                .filter(mailState1 -> mailState1.getUser().getId().equals(currentUser.getId()))
                .findFirst()
                .orElseThrow();
        mailState.setRead(true);
        mailRepository.save(mail);
    }

    @Transactional
    public void handleRevealAfterFlagSubmitted(Flag flag) {
        revealRandom();

        List<Mail> mails = mailRepository.findAllByPrecededByContaining(flag);
        var currentUser = userService.findAuthenticated();
        var flagsSubmitted = scoreRepository.findAllByUser(currentUser).stream()
                .map(Score::getFlag)
                .collect(Collectors.toSet());

        mails.forEach(mail -> {
            if (flagsSubmitted.containsAll(mail.getPrecededBy())) {
                mail.getMailStates().stream()
                        .filter(mailState -> mailState.getUser().getId().equals(currentUser.getId()))
                        .findFirst()
                        .ifPresent(mailState -> {
                            mailState.setVisible(true);
                            mailState.setOrdinal(mailRepository.findMaxOrdinal(currentUser) + 1);
                        });
                mailRepository.save(mail);
            }
        });
    }

    void revealRandom() {
        var revealChance = Math.random();
        if (revealChance < 0.8) {
            var currentUser = userService.findAuthenticated();
            var mails = mailRepository.findAllPlotOnlyNotVisible(currentUser);
            var mail = mails.get((int) (Math.random() * mails.size()));
            var mailState = mail.getMailStates().stream()
                    .filter(mailState1 -> mailState1.getUser().getId().equals(currentUser.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("MailState for user " + currentUser.getId() + " not found"));
            mailState.setVisible(true);
            mailState.setOrdinal(mailRepository.findMaxOrdinal(currentUser) + 1);
            mailRepository.save(mail);
        }
    }
}
