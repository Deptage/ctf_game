package put.edu.ctfgame.forum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import put.edu.ctfgame.forum.dto.CommentDTO;
import put.edu.ctfgame.forum.dto.PostDTO;
import put.edu.ctfgame.forum.entity.Comment;
import put.edu.ctfgame.forum.service.AuthService;
import put.edu.ctfgame.forum.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> findAll(@CookieValue("role") String role) {
        authService.verifyAdminAccessByCookieRole(role);
        var posts = postService.findAllExcept68();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> findById(@PathVariable Long postId) {
        var post = postService.findById(postId);
        return ResponseEntity.ok(post);
    }
}