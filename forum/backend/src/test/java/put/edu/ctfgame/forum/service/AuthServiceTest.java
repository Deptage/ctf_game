package put.edu.ctfgame.forum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import put.edu.ctfgame.forum.TestDataFactory;
import put.edu.ctfgame.forum.dto.ForumUserDTO;
import put.edu.ctfgame.forum.entity.ForumUser;
import put.edu.ctfgame.forum.entity.Role;
import put.edu.ctfgame.forum.exception.UserAlreadyExistsException;
import put.edu.ctfgame.forum.exception.UserNotFoundException;
import put.edu.ctfgame.forum.repository.RoleRepository;
import put.edu.ctfgame.forum.repository.UserRepository;

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

    private ForumUserDTO forumUserDTO;
    private ForumUser forumUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        forumUserDTO = TestDataFactory.sampleForumUserDTO();
        userRole = TestDataFactory.sampleRole();
        forumUser = TestDataFactory.sampleForumUser();
    }

    @Test
    void testSignup_UserAlreadyExists() {
        // Given
        when(userRepository.findByUsername(forumUserDTO.getUsername())).thenReturn(Optional.of(forumUser));

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> authService.signup(forumUserDTO));
    }

    @Test
    void testSignup_Success() {
        // Given
        when(userRepository.findByUsername(forumUserDTO.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(forumUserDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(ForumUser.class))).thenReturn(forumUser);

        // When
        var result = authService.signup(forumUserDTO);

        // Then
        assertNotNull(result);
        assertEquals(forumUserDTO.getUsername(), result.getUsername());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        // When & Then
        assertThrows(UserNotFoundException.class, () -> authService.authenticate(forumUserDTO));
    }

    @Test
    void testAuthenticate_Success() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByUsername(forumUserDTO.getUsername())).thenReturn(Optional.of(forumUser));

        // When
        var result = authService.authenticate(forumUserDTO);

        // Then
        assertNotNull(result);
        assertEquals(forumUserDTO.getUsername(), result.getUsername());
    }

    @Test
    void testVerifyAdminAccessByCookieRole_AccessDenied() {
        // Given
        var role = "ROLE_USER";

        // When & Then
        assertThrows(AccessDeniedException.class, () -> authService.verifyAdminAccessByCookieRole(role));
    }

    @Test
    void testVerifyAdminAccessByCookieRole_AccessGranted() {
        // Given
        var role = "ROLE_ADMIN";

        // When & Then
        assertDoesNotThrow(() -> authService.verifyAdminAccessByCookieRole(role));
    }
}