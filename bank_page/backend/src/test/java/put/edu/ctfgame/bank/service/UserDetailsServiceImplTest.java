package put.edu.ctfgame.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import put.edu.ctfgame.bank.TestDataFactory;
import put.edu.ctfgame.bank.entity.BankUser;
import put.edu.ctfgame.bank.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private BankUser bankUser;

    @BeforeEach
    void setUp() {
        bankUser = TestDataFactory.sampleBankUser();
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // When
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(bankUser));

        // Then
        var userDetails = userDetailsService.loadUserByUsername("testuser");
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // When
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        // Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("unknownuser"));
    }
}