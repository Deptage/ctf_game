package put.edu.ctfgame.forum.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import put.edu.ctfgame.forum.TestDataFactory;
import put.edu.ctfgame.forum.dto.ForumUserDTO;
import put.edu.ctfgame.forum.dto.LoginResponse;
import put.edu.ctfgame.forum.dto.PostAuthorDTO;
import put.edu.ctfgame.forum.entity.ForumUser;
import put.edu.ctfgame.forum.service.AuthService;
import put.edu.ctfgame.forum.service.JwtService;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletResponse response;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private PostAuthorDTO simpleForumUserDTO;
    private ForumUserDTO forumUserDTO;
    private ForumUser forumUser;

    @BeforeEach
    void setUp() {
        simpleForumUserDTO = TestDataFactory.sampleSimpleForumUserDTO();
        forumUserDTO = TestDataFactory.sampleForumUserDTO();
        forumUser = TestDataFactory.sampleForumUser();
    }

    @Test
    @WithMockUser
    void testSignup_Success() {
        // When
        when(authService.signup(forumUserDTO)).thenReturn(simpleForumUserDTO);

        // Then
        ResponseEntity<PostAuthorDTO> result = authController.signup(forumUserDTO);
        assertNotNull(result);
        assertEquals(forumUserDTO.getUsername(), Objects.requireNonNull(result.getBody()).getUsername());
    }

    @Test
    @WithMockUser
    void testAuthenticate_Success() {
        // When
        when(authService.authenticate(forumUserDTO)).thenReturn(forumUser);
        when(authService.userRoleCookie()).thenReturn(new Cookie("role", "ROLE_USER"));

        // Then
        ResponseEntity<LoginResponse> result = authController.authenticate(forumUserDTO, response);
        assertNotNull(result);
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    @WithMockUser
    void testLogout_Success() {
        // Given
        Cookie emptyRoleCookie = new Cookie("role", null);
        emptyRoleCookie.setPath("/");
        emptyRoleCookie.setHttpOnly(true);
        emptyRoleCookie.setMaxAge(0);

        // When
        when(authService.emptyRoleCookie()).thenReturn(emptyRoleCookie);

        // Then
        ResponseEntity<Void> result = authController.logout(response);
        assertEquals(200, result.getStatusCodeValue());
        verify(response).addCookie(emptyRoleCookie);
    }
}