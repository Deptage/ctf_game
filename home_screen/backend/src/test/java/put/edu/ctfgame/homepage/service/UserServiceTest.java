package put.edu.ctfgame.homepage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import put.edu.ctfgame.homepage.TestDataFactory;
import put.edu.ctfgame.homepage.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Given
        var user1 = TestDataFactory.sampleCtfgameUser();
        var user2 = TestDataFactory.sampleCtfgameUser();

        // When
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Then
        var result = userService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void testFindAuthenticated() {
        // Given
        var currentUser = TestDataFactory.sampleCtfgameUser();
        var authentication = mock(Authentication.class);

        // When
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Then
        var result = userService.findAuthenticated();
        assertEquals(currentUser, result);
    }

    @Test
    void testFindByUsername_Success() {
        // Given
        var username = "testUser";
        var user = TestDataFactory.sampleCtfgameUser();

        // When
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Then
        var result = userService.findByUsername(username);
        assertEquals(user, result);
    }

    @Test
    void testFindByUsername_ThrowsException() {
        // Given
        String username = "nonExistentUser";

        // When
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Then
        assertThrows(RuntimeException.class, () -> userService.findByUsername(username));
    }
}