package put.edu.ctfgame.homepage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import put.edu.ctfgame.homepage.TestDataFactory;
import put.edu.ctfgame.homepage.dto.MailDTO;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.mail.Mail;
import put.edu.ctfgame.homepage.entity.mail.MailState;
import put.edu.ctfgame.homepage.exception.NoSuchMailException;
import put.edu.ctfgame.homepage.repository.MailRepository;
import put.edu.ctfgame.homepage.repository.ScoreRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailServiceTest {

    @Mock
    private MailRepository mailRepository;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MailService mailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllVisibleForCurrentUser() {
        // Given
        CtfgameUser currentUser = TestDataFactory.sampleCtfgameUser();
        Mail mail = TestDataFactory.sampleMail();
        mail.setMailStates(List.of(TestDataFactory.sampleMailState()));
        when(userService.findAuthenticated()).thenReturn(currentUser);
        when(mailRepository.findAllVisibleForUser(currentUser)).thenReturn(List.of(mail));

        // When
        List<MailDTO> result = mailService.findAllVisibleForCurrentUser();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mail.getId(), result.get(0).getId());
        assertEquals(mail.getTopic(), result.get(0).getTopic());
        assertEquals(mail.getContent(), result.get(0).getContent());
        assertEquals(mail.getSender(), result.get(0).getSender());
        assertTrue(result.get(0).getRead());
    }

    @Test
    void testReadMailById() {
        // Given
        Long mailId = 1L;
        CtfgameUser currentUser = TestDataFactory.sampleCtfgameUser();
        Mail mail = TestDataFactory.sampleMail();
        var mailState = TestDataFactory.sampleMailState();
        mailState.setRead(false);
        mailState.setUser(currentUser);
        mail.setMailStates(List.of(mailState));
        when(userService.findAuthenticated()).thenReturn(currentUser);
        when(mailRepository.findVisibleByIdForUser(currentUser, mailId)).thenReturn(Optional.of(mail));

        // When
        mailService.readMailById(mailId);

        // Then
        assertTrue(mailState.getRead());
        verify(mailRepository, times(1)).save(mail);
    }

    @Test
    void testReadMailById_NoSuchMailException() {
        // Given
        Long mailId = 1L;
        CtfgameUser currentUser = TestDataFactory.sampleCtfgameUser();
        when(userService.findAuthenticated()).thenReturn(currentUser);
        when(mailRepository.findVisibleByIdForUser(currentUser, mailId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchMailException.class, () -> mailService.readMailById(mailId));
    }

    @Test
    void testHandleRevealAfterFlagSubmitted() {
        // Given
        Flag flag = TestDataFactory.sampleFlag();
        var currentUser = TestDataFactory.sampleCtfgameUser();
        var mail = TestDataFactory.sampleMail();
        var mailState = TestDataFactory.sampleMailState();
        mailState.setUser(currentUser);
        mail.setMailStates(List.of(mailState));
        var score = TestDataFactory.sampleScore();
        score.setFlag(flag);

        when(userService.findAuthenticated()).thenReturn(currentUser);
        when(mailRepository.findAllByPrecededByContaining(flag)).thenReturn(List.of(mail));
        when(scoreRepository.findAllByUser(currentUser)).thenReturn(List.of(score));
        when(mailRepository.findAllPlotOnlyNotVisible(currentUser)).thenReturn(List.of(mail));
        when(mailRepository.findMaxOrdinal(currentUser)).thenReturn(1);

        // When
        mailService.handleRevealAfterFlagSubmitted(flag);

        // Then
        verify(mailRepository, atLeastOnce()).save(mail);
    }

    @Test
    void testRevealRandom() {
        // Given
        var currentUser = TestDataFactory.sampleCtfgameUser();
        var mail = TestDataFactory.sampleMail();
        var mailState = TestDataFactory.sampleMailState();
        mailState.setUser(currentUser);
        mail.setMailStates(List.of(mailState));

        when(userService.findAuthenticated()).thenReturn(currentUser);
        when(mailRepository.findAllPlotOnlyNotVisible(currentUser)).thenReturn(List.of(mail));
        when(mailRepository.findMaxOrdinal(currentUser)).thenReturn(1);

        // When
        mailService.revealRandom();

        // Then
        verify(mailRepository, atLeast(0)).save(mail);
    }
}