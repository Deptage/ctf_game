package put.edu.ctfgame.homepage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import put.edu.ctfgame.homepage.TestDataFactory;
import put.edu.ctfgame.homepage.dto.HintDTO;
import put.edu.ctfgame.homepage.entity.hint.Hint;
import put.edu.ctfgame.homepage.entity.hint.PlayerHint;
import put.edu.ctfgame.homepage.entity.hint.PlayerHintKey;
import put.edu.ctfgame.homepage.enums.LevelName;
import put.edu.ctfgame.homepage.exception.HintAlreadyRevealedException;
import put.edu.ctfgame.homepage.exception.HintDoesNotExistException;
import put.edu.ctfgame.homepage.repository.HintRepository;
import put.edu.ctfgame.homepage.repository.PlayerHintRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HintServiceTest {

    @Mock
    private HintRepository hintRepository;

    @Mock
    private PlayerHintRepository playerHintRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private HintService hintService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Given
        var currentUser = TestDataFactory.sampleCtfgameUser();
        List<Hint> hints = List.of(TestDataFactory.sampleHint());

        // When
        when(userService.findAuthenticated()).thenReturn(currentUser);
        when(hintRepository.findAll()).thenReturn(hints);
        when(playerHintRepository.findByUserAndHint(currentUser, hints.get(0))).thenReturn(Optional.empty());

        // Then
        List<HintDTO> result = hintService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).getRevealed());
    }

    @Test
    void testFindAllByLevelName() {
        // Given
        var currentUser = TestDataFactory.sampleCtfgameUser();
        Long flagId = 1L;
        List<Hint> hints = List.of(TestDataFactory.sampleHint());

        // When
        when(userService.findAuthenticated()).thenReturn(currentUser);
        when(hintRepository.findAllByFlagId(flagId)).thenReturn(hints);
        when(playerHintRepository.findByUserAndHint(currentUser, hints.get(0))).thenReturn(Optional.empty());

        // Then
        List<HintDTO> result = hintService.findAllByFlagId(flagId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).getRevealed());
    }

    @Test
    void testRevealHint_Success() {
        // Given
        LevelName levelName = LevelName.BANK;
        Long flagId = 1L;
        var currentUser = TestDataFactory.sampleCtfgameUser();
        var hint = TestDataFactory.sampleHint();

        // When
        when(userService.findAuthenticated()).thenReturn(currentUser);
        when(hintRepository.findNextByFlagId(flagId, currentUser.getId())).thenReturn(Optional.of(hint));
        when(playerHintRepository.findByUserAndHint(currentUser, hint)).thenReturn(Optional.empty());

        // Then
        HintDTO result = hintService.revealHint(flagId);
        assertNotNull(result);
        verify(playerHintRepository, times(1)).save(any(PlayerHint.class));
    }

    @Test
    void testRevealHint_HintDoesNotExist() {
        // Given
        Long flagId = 1L;
        var user = TestDataFactory.sampleCtfgameUser();

        // WHen
        when(userService.findAuthenticated()).thenReturn(user);
        when(hintRepository.findNextByFlagId(flagId, user.getId())).thenReturn(Optional.empty());

        // Then
        assertThrows(HintDoesNotExistException.class, () -> hintService.revealHint(flagId));
    }
}