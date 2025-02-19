package put.edu.ctfgame.forum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import put.edu.ctfgame.forum.TestDataFactory;
import put.edu.ctfgame.forum.dto.CommentDTO;
import put.edu.ctfgame.forum.dto.PostDTO;
import put.edu.ctfgame.forum.entity.Comment;
import put.edu.ctfgame.forum.entity.Post;
import put.edu.ctfgame.forum.exception.PostNotFoundException;
import put.edu.ctfgame.forum.exception.UserNotFoundException;
import put.edu.ctfgame.forum.repository.CommentRepository;
import put.edu.ctfgame.forum.repository.PostRepository;
import put.edu.ctfgame.forum.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser
    void testFindAll() {
        // Given
        var postId68 = TestDataFactory.samplePostId68();
        var posts = List.of(TestDataFactory.samplePost(), TestDataFactory.samplePost(), postId68);

        // When
        when(postRepository.findAllExcept68()).thenReturn(posts);

        // Then
        var result = postService.findAllExcept68();
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    @WithMockUser
    void testFindById() {
        // Given
        var post = TestDataFactory.samplePost();

        // When
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Then
        var result = postService.findById(1L);
        assertNotNull(result);
    }

    @Test
    @WithMockUser
    void testFindByIdNotFound() {
        // When
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(PostNotFoundException.class, () -> postService.findById(1L));
    }

    @Test
    @WithMockUser
    void testSave() {
        // Given
        var user = TestDataFactory.sampleForumUser();
        var post = TestDataFactory.samplePost();
        var postDTO = PostDTO.from(post);

        // When
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(TestDataFactory.sampleForumUser());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Then
        var result = postService.save(postDTO);
        assertNotNull(result);
    }

    @Test
    @WithMockUser
    void testAddCommentToPost() {
        // Given
        var commentDTO = CommentDTO.from(TestDataFactory.sampleComment());
        var post = TestDataFactory.samplePost();
        var user = TestDataFactory.sampleForumUser();
        var comment = TestDataFactory.sampleComment();

        // When
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(TestDataFactory.sampleForumUser());
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Then
        var result = postService.addCommentToPost(1L, commentDTO);
        assertNotNull(result);
    }

    @Test
    @WithMockUser
    void testAddCommentToPostPostNotFound() {
        // Given
        var commentDTO = new CommentDTO();

        // When
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(PostNotFoundException.class, () -> postService.addCommentToPost(1L, commentDTO));
    }
}