package put.edu.ctfgame.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import put.edu.ctfgame.bank.TestDataFactory;
import put.edu.ctfgame.bank.dto.BankUserDTO;
import put.edu.ctfgame.bank.entity.BankUser;
import put.edu.ctfgame.bank.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private BankUser bankUser;

    @BeforeEach
    void setUp() {
        bankUser = TestDataFactory.sampleBankUser();
    }

    @Test
    void testFindAll() {
        // Given
        var users = List.of(bankUser);

        // When
        when(userRepository.findAll()).thenReturn(users);

        // Then
        var result = userService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BankUserDTO.from(bankUser), result.get(0));
    }

    @Test
    void testFindAuthenticated() {
        // When
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(bankUser);
        when(userRepository.findByUsername(bankUser.getUsername())).thenReturn(Optional.of(bankUser));

        // Then
        var result = userService.findAuthenticated();
        assertNotNull(result);
        assertEquals(BankUserDTO.from(bankUser), result);
    }

    @Test
    void testFindByUsername_UserFound() {
        // When
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(bankUser));

        // Then
        var result = userService.findByUsername("testuser");
        assertNotNull(result);
        assertEquals(bankUser, result);
    }

    @Test
    void testFindByUsername_UserNotFound() {
        // When
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        // Then
        assertThrows(RuntimeException.class, () -> userService.findByUsername("unknownuser"));
    }
}