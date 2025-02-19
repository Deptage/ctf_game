package put.edu.ctfgame.messenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import put.edu.ctfgame.messenger.TestDataFactory;
import put.edu.ctfgame.messenger.entity.User;
import put.edu.ctfgame.messenger.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserByName_UserExists() {
        // Given
        var username = "testuser";
        var user = TestDataFactory.sampleUser();

        // When
        when(userRepository.findById(username)).thenReturn(Optional.of(user));

        // Then
        var result = userService.getUserByName(username);
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetUserByName_UserDoesNotExist() {
        // Given
        var username = "nonexistentuser";

        // When
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        // Then
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByName(username));
    }
}