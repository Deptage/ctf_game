package put.edu.ctfgame.forum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import put.edu.ctfgame.forum.dto.CommentDTO;
import put.edu.ctfgame.forum.dto.PostDTO;
import put.edu.ctfgame.forum.entity.Comment;
import put.edu.ctfgame.forum.entity.ForumUser;
import put.edu.ctfgame.forum.entity.Post;
import put.edu.ctfgame.forum.exception.PostNotFoundException;
import put.edu.ctfgame.forum.exception.UserNotFoundException;
import put.edu.ctfgame.forum.repository.CommentRepository;
import put.edu.ctfgame.forum.repository.PostRepository;
import put.edu.ctfgame.forum.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public List<PostDTO> findAll() {
        log.info("Fetching all posts");
        var posts = postRepository.findAll();
        return posts.stream().map(PostDTO::from).toList();
    }

    public List<PostDTO> findAllExcept68() {
        log.info("Fetching all posts except 68");
        var posts = postRepository.findAllExcept68();
        return posts.stream().map(PostDTO::from).toList();
    }

    public PostDTO findById(Long postId) {
        log.info("Fetching post by id: {}", postId);
        var post = postRepository.findById(postId).orElseThrow(
                () -> {
                    log.error("Post not found with id: {}", postId);
                    return new PostNotFoundException("Post not found");
                }
        );

        var dto = PostDTO.from(post);
        var userPostsCount = userRepository.countPostsByUsername(post.getAuthor().getUsername());
        var userCommentsCount = userRepository.countCommentsByUsername(post.getAuthor().getUsername());
        dto.getAuthor().setPostsCount(userPostsCount);
        dto.getAuthor().setCommentsCount(userCommentsCount);
        return dto;
    }

    @Transactional
    public PostDTO save(PostDTO postDTO) {
        log.info("Saving post: {}", postDTO);
        var post = Post.from(postDTO);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var author = (ForumUser) authentication.getPrincipal();
        post.setAuthor(author);
        var entity = postRepository.save(post);
        log.info("Post saved with id: {}", entity.getId());
        return PostDTO.from(entity);
    }

    @Transactional
    public CommentDTO addCommentToPost(Long postId, CommentDTO commentDTO) {
        log.info("Adding comment to post with id: {}", postId);
        var post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post not found with id: {}", postId);
                    return new PostNotFoundException("Post not found with id " + postId);
                });
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var author = (ForumUser) authentication.getPrincipal();
        var comment = Comment.from(commentDTO);
        comment.setAuthor(author);
        comment.setPost(post);
        var savedComment = commentRepository.save(comment);
        log.info("Comment added with id: {}", savedComment.getId());

        return CommentDTO.from(savedComment);
    }
}
