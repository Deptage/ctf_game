package put.edu.ctfgame.homepage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import put.edu.ctfgame.homepage.TestDataFactory;
import put.edu.ctfgame.homepage.dto.LoginInputDTO;
import put.edu.ctfgame.homepage.dto.RegisterDTO;
import put.edu.ctfgame.homepage.entity.CtfgameUser;
import put.edu.ctfgame.homepage.repository.LevelRepository;
import put.edu.ctfgame.homepage.repository.UserRepository;
import put.edu.ctfgame.homepage.util.MailJsonHandler;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LevelRepository levelRepository;

    @Mock
    private MailJsonHandler mailJsonHandler;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup() {
        RegisterDTO input = new RegisterDTO("user", "password", "polish");
        CtfgameUser user = CtfgameUser.builder()
                .username("user")
                .password("encodedPassword")
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(CtfgameUser.class))).thenReturn(user);
        when(levelRepository.findAll()).thenReturn(List.of(TestDataFactory.sampleLevel()));

        RegisterDTO result = authService.signup(input);

        assertEquals("user", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(CtfgameUser.class));
    }

    @Test
    void testAuthenticate() {
        var input = new LoginInputDTO("user", "password");
        CtfgameUser user = CtfgameUser.builder()
                .username("user")
                .password("encodedPassword")
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CtfgameUser result = authService.authenticate(input);

        assertEquals("user", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticateUserNotFound() {
        var input = new LoginInputDTO("user", "password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.authenticate(input));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}