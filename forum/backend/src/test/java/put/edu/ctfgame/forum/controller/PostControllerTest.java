package put.edu.ctfgame.forum.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import put.edu.ctfgame.forum.TestDataFactory;
import put.edu.ctfgame.forum.dto.CommentDTO;
import put.edu.ctfgame.forum.dto.ForumUserDTO;
import put.edu.ctfgame.forum.dto.PostDTO;
import put.edu.ctfgame.forum.service.AuthService;
import put.edu.ctfgame.forum.service.JwtService;
import put.edu.ctfgame.forum.service.PostService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testFindAll() throws Exception {
        // Given
        var posts = List.of(
                PostDTO.from(TestDataFactory.samplePost()),
                PostDTO.from(TestDataFactory.samplePost())
        );

        // When
        when(postService.findAllExcept68()).thenReturn(posts);

        // Then
        mockMvc.perform(get("/posts").header("Instance-Id", "instance-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("role", "ROLE_ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    @WithMockUser
    void testFindById() throws Exception {
        // Given
        var post = PostDTO.from(TestDataFactory.samplePost());

        // When
        when(postService.findById(1L)).thenReturn(post);

        // Then
        mockMvc.perform(get("/posts/1").header("Instance-Id", "instance-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(post.getId().intValue())));
    }
}