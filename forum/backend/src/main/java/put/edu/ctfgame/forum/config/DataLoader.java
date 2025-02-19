package put.edu.ctfgame.forum.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import put.edu.ctfgame.forum.entity.Comment;
import put.edu.ctfgame.forum.entity.ForumUser;
import put.edu.ctfgame.forum.entity.Post;
import put.edu.ctfgame.forum.entity.Role;
import put.edu.ctfgame.forum.enums.RoleEnum;
import put.edu.ctfgame.forum.repository.CommentRepository;
import put.edu.ctfgame.forum.repository.PostRepository;
import put.edu.ctfgame.forum.repository.RoleRepository;
import put.edu.ctfgame.forum.repository.UserRepository;
import put.edu.ctfgame.forum.util.PostJsonParser;

import java.util.Set;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostJsonParser postJsonParser;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initUsers();
        initPosts();
    }

    private void initRoles() {
        var adminRole = Role.builder()
                .name(RoleEnum.ADMIN.name())
                .build();
        var userRole = Role.builder()
                .name(RoleEnum.USER.name())
                .build();
        roleRepository.save(adminRole);
        roleRepository.save(userRole);
    }

    private void initUsers() {
        var adminRole = roleRepository.findByName(RoleEnum.ADMIN.name()).orElseThrow();
        var userRole = roleRepository.findByName(RoleEnum.USER.name()).orElseThrow();
        var user = ForumUser.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .roles(Set.of(userRole))
                .build();
        var admin = ForumUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("aintnosunshinewhenshesgone"))
                .roles(Set.of(userRole, adminRole))
                .build();
        userRepository.save(user);
        userRepository.save(admin);
    }

    private void initPosts() throws Exception {
        postJsonParser.parsePosts("forum_posts.json");
    }

    private void initMockPosts() {
        var user = userRepository.findByUsername("user").orElseThrow();
        var post = Post.builder()
                .id(1L)
                .author(user)
                .title("Post title")
                .content("Post content")
                .build();
        var post2 = Post.builder()
                .id(2L)
                .author(user)
                .title("Post title 2")
                .content("Post content 2")
                .build();
        var post68 = Post.builder()
                .id(68L)
                .author(user)
                .title("Post title 68")
                .content("Post content 68")
                .build();
        postRepository.save(post);
        postRepository.save(post2);
        postRepository.save(post68);
    }

    private void initComments() {
        var user = userRepository.findByUsername("user").orElseThrow();
        var admin = userRepository.findByUsername("admin").orElseThrow();
        var post = postRepository.findById(1L).orElseThrow();
        var comment = Comment.builder()
                .author(user)
                .post(post)
                .content("Comment content")
                .build();
        var adminComment = Comment.builder()
                .author(admin)
                .post(post)
                .content("Admin comment content")
                .build();
        commentRepository.save(comment);
        commentRepository.save(adminComment);
    }
}
