package put.edu.ctfgame.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import put.edu.ctfgame.bank.TestDataFactory;
import put.edu.ctfgame.bank.dto.BankUserDTO;
import put.edu.ctfgame.bank.entity.BankUser;
import put.edu.ctfgame.bank.entity.Role;
import put.edu.ctfgame.bank.exception.UserAlreadyExistsException;
import put.edu.ctfgame.bank.exception.UserNotFoundException;
import put.edu.ctfgame.bank.repository.RoleRepository;
import put.edu.ctfgame.bank.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthService authService;

    private BankUserDTO bankUserDTO;
    private BankUser bankUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        bankUserDTO = TestDataFactory.sampleBankUserDTO();
        userRole = TestDataFactory.sampleRole();
        bankUser = TestDataFactory.sampleBankUser();
    }

    @Test
    void testSignup_UserAlreadyExists() {
        // Given
        when(userRepository.findByUsername(bankUserDTO.getUsername())).thenReturn(Optional.of(bankUser));

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> authService.signup(bankUserDTO));
    }

    @Test
    void testSignup_Success() {
        // Given
        when(userRepository.findByUsername(bankUserDTO.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(bankUserDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(BankUser.class))).thenReturn(bankUser);

        // When
        var result = authService.signup(bankUserDTO);

        // Then
        assertNotNull(result);
        assertEquals(bankUserDTO.getUsername(), result.getUsername());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        // When & Then
        assertThrows(UserNotFoundException.class, () -> authService.authenticate(bankUserDTO));
    }

    @Test
    void testAuthenticate_Success() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByUsername(bankUserDTO.getUsername())).thenReturn(Optional.of(bankUser));

        // When
        var result = authService.authenticate(bankUserDTO);

        // Then
        assertNotNull(result);
        assertEquals(bankUserDTO.getUsername(), result.getUsername());
    }
}