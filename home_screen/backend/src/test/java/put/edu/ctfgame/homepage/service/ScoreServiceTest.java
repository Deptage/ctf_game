package put.edu.ctfgame.homepage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;
import put.edu.ctfgame.homepage.TestDataFactory;
import put.edu.ctfgame.homepage.dto.FlagInput;
import put.edu.ctfgame.homepage.dto.FlagResponse;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.score.Score;
import put.edu.ctfgame.homepage.exception.LevelAlreadyCompletedException;
import put.edu.ctfgame.homepage.exception.NoSuchFlagException;
import put.edu.ctfgame.homepage.repository.FlagRepository;
import put.edu.ctfgame.homepage.repository.LevelRepository;
import put.edu.ctfgame.homepage.repository.PlayerHintRepository;
import put.edu.ctfgame.homepage.repository.ScoreRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private UserService userService;

    @Mock
    private PlayerHintRepository playerHintRepository;

    @Mock
    private LevelRepository levelRepository;

    @Mock
    private FlagRepository flagRepository;

    @Mock
    private MailService mailService;

    @Spy
    @InjectMocks
    private ScoreService scoreService;

    @Mock
    private FlagService flagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitFlag_Success() {
        // Given
        var level = TestDataFactory.sampleLevel();
        var currentUser = TestDataFactory.sampleCtfgameUser();
        var flagInput = FlagInput.builder()
                .flag("flag")
                .build();
        var flagId = 1L;

        // When
        when(userService.findAuthenticated()).thenReturn(currentUser);
        when(playerHintRepository.countAllByUsernameAndFlagId(currentUser.getUsername(), flagId)).thenReturn(0L);
        when(flagService.findByIdOrElseThrow(flagId)).thenReturn(TestDataFactory.sampleFlag());
        doNothing().when(scoreService).submitToExternalServer(anyString(), anyString(), anyString(), anyString());
        // Then
        FlagResponse response = scoreService.submitFlag(flagId, flagInput);

        assertNotNull(response);
        assertTrue(response.getCorrect());
        verify(scoreRepository, times(1)).save(any(Score.class));
    }

    @Test
    void testSubmitFlag_FlagNotFound() {
        // Given
        var flagInput = new FlagInput("flag");

        // Mock flagService to throw exception
        when(flagService.findByIdOrElseThrow(anyLong())).thenThrow(new NoSuchFlagException("Flag not found"));

        // Then
        assertThrows(NoSuchFlagException.class, () -> scoreService.submitFlag(1L, flagInput));
    }

    @Test
    void testSubmitFlag_IncorrectFlag() {
        // Given
        var flagInput = new FlagInput("wrongFlag");
        var flag = TestDataFactory.sampleFlag();
        flag.setFlag("correctFlag");

        // When
        when(flagService.findByIdOrElseThrow(1L)).thenReturn(flag);

        // Then
        FlagResponse response = scoreService.submitFlag(1L, flagInput);

        assertNotNull(response);
        assertFalse(response.getCorrect());
        assertEquals("Incorrect!", response.getMessage());
        verify(scoreRepository, never()).save(any(Score.class));
    }
}